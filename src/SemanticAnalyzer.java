import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class SemanticAnalyzer {

    /* Class that holds the information that needs to be stored */
    public SymbolTable symbolTable = new SymbolTable();
    /* List of semantic errors found */
    private List<SemanticError> errors = new ArrayList<>();

    /* Declares if the use of not initialized variables should be handled as an error or warning */
    private boolean treatNotInitiedVarAsError;

    /* Constructors */
    public SemanticAnalyzer(SimpleNode root, boolean treatNotInitedVarAsError) throws ParseException {
        this.treatNotInitiedVarAsError = treatNotInitedVarAsError;
        start(root);
        /* Displaying semantic errors */
        displayErrors();
    }

    private void displayErrors() throws ParseException {
        if(errors.size() > 0) {
            int currLine = -1;
            for (SemanticError error : errors) {
                if (error.line != currLine) {
                    currLine = error.line;
                    System.err.println("\n" + error);
                }
            }
            throw new ParseException("\nOne or more semantic errors found!", 0);
        }
    }

    public SemanticAnalyzer(SymbolTable table) {
        this.symbolTable = table;
    }

    /* Initializes the analyzing process */
    public void start(SimpleNode root) throws ParseException {
        buildSymbolsTable(root);
        checkSemantic(root);
    }

    /* Builds the symbol table */
    public void buildSymbolsTable(SimpleNode root) throws ParseException {
        for(int i = 0; i < root.jjtGetNumChildren(); i++) { // Cycles through every child of the root node
            SimpleNode currentNode = (SimpleNode) root.jjtGetChild(i);
            switch(currentNode.toString()) {
                case "Import":
                    fillImportInformation(currentNode);
                    break;
                case "Class":
                    fillClassInformation(currentNode);
                    break;
                default:
                    break;
            }
        }
    }

    /* Fills the symbol table with the information about an import node */
    public void fillImportInformation(SimpleNode importNode)  {
        Import importDeclaration = (Import) importNode.getImage();
        symbolTable.addImport(importDeclaration);
    }

    /* Fills the symbol table with the information about the class node */
    public void fillClassInformation(SimpleNode classNode) throws ParseException {
        ClassDeclaration cd = (ClassDeclaration) classNode.getImage();
        String className = cd.name;
        String extendsClass = cd.extendsClass;
        symbolTable.setClassName(className);
        symbolTable.setExtendsClass(extendsClass);
        for(int i = 0; i < classNode.jjtGetNumChildren(); i++) { // Cycles through every child of the class node
            SimpleNode currentNode = (SimpleNode) classNode.jjtGetChild(i);
            switch(currentNode.toString()) {
                case "VarDeclaration": // Stores the class variables
                    VarDeclaration varDec = (VarDeclaration) currentNode.getImage();
                    symbolTable.addVariable(varDec.name, varDec.type);
                    Symbol symb = symbolTable.getGlobalVarSymbol(varDec.name);
                    symb.setInited();
                    break;
                case "Method": // Stores the class methods
                    MethodDeclaration methodDec = (MethodDeclaration) currentNode.getImage();
                    Method method = new Method(methodDec.name, methodDec.returnType);
                    List<Arg> args = methodDec.args;
                    if(!symbolTable.checkIfTypeExists(methodDec.returnType)) {
                        errors.add(new SemanticError("Function return type does not exist: " + methodDec.returnType + "!", currentNode.token, false));
                    }
                    for(Arg arg : args) { // Stores the method arguments
                        if(!symbolTable.checkIfTypeExists(arg.type)) {
                            errors.add(new SemanticError("Argument " + arg.name + " is of non-existent type: " + arg.type + "!", currentNode.token, false));
                        }
                        method.addArgument(arg.name, arg.type);
                    }
                    for(int j = 0; j < currentNode.jjtGetNumChildren(); j++) {
                        SimpleNode methodChild = (SimpleNode) currentNode.jjtGetChild(j);
                        if(methodChild.toString().equals("VarDeclaration")) { // Stores the method variables
                            VarDeclaration methodVar = (VarDeclaration) methodChild.getImage();
                            if (!method.addVariable(methodVar.name, methodVar.type))
                                errors.add(new SemanticError("Redefinition of variable " + methodVar.name, methodChild.token, false));
                        }
                    }
                    symbolTable.addMethod(method);
                    break;
                case "Main": // Stores the class main method
                    Method method2 = new Method("main", "void");
                    for(int j = 0; j < currentNode.jjtGetNumChildren(); j++) {
                        SimpleNode methodChild = (SimpleNode) currentNode.jjtGetChild(j);
                        if(methodChild.toString().equals("VarDeclaration")) {
                            VarDeclaration methodVar = (VarDeclaration) methodChild.getImage();
                            if (!method2.addVariable(methodVar.name, methodVar.type))
                                errors.add(new SemanticError("Redefinition of variable " + methodVar.name, methodChild.token, false));
                        }
                    }
                    symbolTable.addMethod(method2);
                    break;
                default:

            }
        }
    }

    /* Recursive function that checks the semantic of the code being analyzed. If a semantic error is found, an exception is thrown */
    public void checkSemantic(SimpleNode node) throws ParseException {
        String nodeName = node.toString();
        switch(nodeName) {
            case "VarDeclaration":
                checkVarType(node);
                break;
            case "Identifier":
                checkIfVarExists(node);
                break;
            case "CallMethod":
                checkIfMethodExists(node);
                checkThisInited(node);
                break;
            case "LessThan":
            case "AddSub":
            case "MulDiv":
                checkOperCompatibility(node, "int");
                break;
            case "And":
                checkOperCompatibility(node, "boolean");
                break;
            case "Not":
                checkOperNot(node);
                break;
            case "ArrayIndex":
                checkIfVarIsArray(node);
            case "IntArray":
                checkArrayIndex(node);
                break;
            case "Length":
                checkIfVarIsArray(node);
                break;
            case "Assign":
                handleAssign(node);
                break;
            case "Return":
                checkReturnType(node);
                break;
            case "If":
            case "While":
                checkCondition(node);
                break;
            case "Break":
            case "Continue":
                checkWithinLoopScope(node);
                break;
        }

        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            SimpleNode n = (SimpleNode) node.jjtGetChild(i);
            if (n != null) {
                checkSemantic(n);
            }
        }
    }

    /* Check if break and continue are being called inside a while scope */
    private void checkWithinLoopScope(SimpleNode node) {
        if (!node.checkIfPredecessorExists("While")) {
            errors.add(new SemanticError(node.toString() + " must be called inside a while scope!", node.token, true));
        }
    }

    /* Checks if the returned value/expression matches the method signature */
    private void checkReturnType(SimpleNode node) throws ParseException {
        MethodForm methodForm = getCurrentMethod(node);
        String methodRetType = symbolTable.getMethod(methodForm).getReturnType();

        if(node.jjtGetNumChildren() > 0) {
            SimpleNode returnNode = (SimpleNode) node.jjtGetChild(0);
            String returnedType = getVarType(returnNode);
            if(!returnedType.equals(methodRetType) && !returnedType.equals("")) {
                errors.add(new SemanticError("Invalid return type! Function must return a variable of " + methodRetType + " type!", returnNode.token, true));
            }
        } else {
            if(!methodRetType.equals("void")) { // In principle, void is not a valid return type in java--
                errors.add(new SemanticError("Invalid return type! Function must return void!"));
            }
        }
    }

    /* Checks whether a variable exists */
    public boolean checkIfVarExists(SimpleNode node) throws ParseException {
        Identifier id = (Identifier) node.getImage();
        String varName = id.name;
        MethodForm methodForm = getCurrentMethod(node);
        if(symbolTable.getVarSymbol(varName, methodForm) == null) {
            if(!symbolTable.containsImportedClass(varName)) {
                errors.add(new SemanticError("Variable " + varName + " was not declared!", node.token, true));
                return false;
            }
        }
        return true;
    }

    /* Returns the method of which the node is a descendant */
    public static MethodForm getCurrentMethod(SimpleNode node) {
        SimpleNode parent = (SimpleNode) node.jjtGetParent();
        while(!(parent.toString().equals("Method") || parent.toString().equals("Main"))) { // While a method node is not found, search the parent node
            parent = (SimpleNode) parent.jjtGetParent();
        }
        if (parent.toString().equals("Method")) { // In case the method is not main
            MethodDeclaration md = (MethodDeclaration) parent.getImage();
            MethodForm methodForm = new MethodForm(md.name);
            for(Arg arg : md.args) {
                methodForm.addType(arg.type);
            }
            return methodForm;
        }
        return new MethodForm("main"); // In case the method is main
    }

    /* Checks if an array index is an integer */
    public void checkArrayIndex(SimpleNode node) throws ParseException{
        SimpleNode child = (SimpleNode) node.jjtGetChild(0);
        Token t;
        if (child.getImage() != null) {
            t = child.token;
        } else {
            t = null;
        }

        String type = getVarType(child);
        if(!type.equals("int")) {
            errors.add(new SemanticError("Array index is not an integer!", t, true));
        }
    }

    /* Checks if a method call is being well executed */
    public void checkIfMethodExists(SimpleNode node) throws ParseException {
        CallMethod callMethod = (CallMethod) node.getImage();
        String methodName = callMethod.method;
        SimpleNode parent = (SimpleNode) node.jjtGetParent();

        if(symbolTable.getMethod(getUsedForm(node)) == null) { // Method does not exist within the defined class
            if(parent.toString().equals("Identifier")) {
                String identifierName = ((Identifier) parent.getImage()).name;
                Symbol var = symbolTable.getVarSymbol(identifierName, getCurrentMethod(node));
                String lib = identifierName;
                if(var != null) { // The variable is defined within the class scope
                    lib = var.getType();
                    Method method = symbolTable.getImportedMethod(getUsedForm(node), lib);
                    if(method != null) {
                        if(!method.isStatic()) return; // Method cannot be static since it's being called using a variable
                        else errors.add(new SemanticError("Method " + method.getName() + " is static!", node.token, true));
                    }
                } else {
                    Method method = symbolTable.getImportedMethod(getUsedForm(node), lib);
                    if(method != null) {
                        if(method.isStatic()) return; // Method must be static since it's being called using the name of the imported class
                        else errors.add(new SemanticError("Method " + method.getName() + " is non-static!", node.token, true));
                    }
                }
            }
            if(parent.toString().equals("This")) {
                Method method = symbolTable.getImportedMethod(getUsedForm(node), "");
                if(method != null) { // The method can only be from the extended class that is imported
                    if(!method.isStatic()) return;
                    else errors.add(new SemanticError("Method " + method.getName() + " is static!", node.token, true));
                }
            }
            errors.add(new SemanticError("Method " + methodName + " was not declared!", node.token, true));
        } else {
            MethodForm mf = getCurrentMethod(node);
            switch(parent.toString()) {
                case "This":
                    if (mf.name.equals("main"))
                        errors.add(new SemanticError("Token 'this' cannot be invoked within the main method!", node.token, false));
                    break;
                case "Identifier":
                    String identifierName = ((Identifier) parent.getImage()).name;
                    Symbol var = symbolTable.getVarSymbol(identifierName, getCurrentMethod(node));
                    if (!var.getType().equals(symbolTable.getClassName())) {
                        errors.add(new SemanticError("Method " + methodName + " cannot be called for variable " + identifierName + "!", parent.token, true));
                    }
                    break;
                case "ObjCreation":
                    break;
                case "CallMethod":
                    String methodReturnType = symbolTable.getMethod(getUsedForm(parent)).getReturnType();
                    if (!methodReturnType.equals(symbolTable.getClassName())) {
                        errors.add(new SemanticError("Method " + methodName + " cannot be called for the type " + methodReturnType + "!", parent.token, true));
                    }
                    break;
                default:
                    errors.add(new SemanticError("Method " + methodName + " cannot be called for a simple type!", parent.token, true));
            }
        }
    }

    /* Throws an exception if the specified node corresponds to a var that is not initialized */
    public void checkInited(SimpleNode node) throws ParseException {
        if(node.jjtGetNumChildren() > 0) {
            SimpleNode child = (SimpleNode) node.jjtGetChild(0);
            if(!child.toString().equals("ArrayIndex"))
                return;
        }
        MethodForm methodForm = getCurrentMethod(node);
        if(node.toString().equals("Identifier")) {
            checkIfVarExists(node);
            Identifier id = (Identifier) node.getImage();
            Symbol symb = symbolTable.getVarSymbol(id.name, methodForm);
            if (symb == null || !isInitedWithinScope(symb, node)) {
                String message = "Variable " + id.name + " might not be initialized!";
                if(treatNotInitiedVarAsError)
                    errors.add(new SemanticError(message, node.token, true));
                else {
                    if (!getVarType(node).equals(""))
                        new Warning(message, node.token, true);
                }
            }
        }
    }

    /* Checks whether an arithmetic operation is being executed between integers */
    public void checkOperCompatibility(SimpleNode node, String opType) throws ParseException {
        SimpleNode firstChild = (SimpleNode) node.jjtGetChild(0);
        SimpleNode secondChild = (SimpleNode) node.jjtGetChild(1);

        String firstChildType = getVarType(firstChild);
        String secondChildType = getVarType(secondChild);

        checkInited(firstChild);
        if(!firstChildType.equals(opType)) {
            errors.add(new SemanticError("Invalid first operation argument type (" + firstChildType + ")!", firstChild.token, true));
        } else {
            checkInited(firstChild);
        }
        checkInited(secondChild);
        if(!secondChildType.equals(opType)) {
            errors.add(new SemanticError("Invalid second operation argument type (" + secondChildType + ")!", secondChild.token, true));
        } else {
            checkInited(secondChild);
        }
    }

    /* Checks that when trying to access an array value, it is in fact an array  */
    public void checkIfVarIsArray(SimpleNode node) throws ParseException {
        SimpleNode parent = (SimpleNode) node.jjtGetParent();
        if(parent.toString().equals("CallMethod")) {
            MethodForm methodForm = getUsedForm(parent);
            Method method = symbolTable.getMethod(methodForm);
            if(method == null) {
                SimpleNode grandParent = (SimpleNode) parent.jjtGetParent();
                Identifier id = (Identifier) grandParent.getImage();
                method = symbolTable.getImportedMethod(methodForm, id.name);
                if (method == null) {
                    return;
                }
            }
            if (!method.getReturnType().equals("int[]")) {
                errors.add(new SemanticError("Invalid type! Method " + method.getName() + " does not return an array!", parent.token, true));
            }
            return;
        } else if(parent.toString().equals("Identifier")) {
            String varName = ((Identifier) parent.getImage()).name;
            MethodForm methodForm = getCurrentMethod(parent);
            Symbol var = symbolTable.getVarSymbol(varName, methodForm);
            if(var == null){
                return;
            }
            if (!var.getType().equals("int[]")) // Checks if it is an array
                errors.add(new SemanticError("Invalid variable type! " + varName + " is not an array!", parent.token, true));
        } else {
            errors.add(new SemanticError("Length property is only applicable to arrays!", parent.token, false));
        }
    }

    /* Checks whether an assign is being well executed and sets the left hand side as initialized for later verifications */
    public void handleAssign(SimpleNode node) throws ParseException {
        SimpleNode firstChild = (SimpleNode) node.jjtGetChild(0);
        SimpleNode secondChild = (SimpleNode) node.jjtGetChild(1);
        if(!checkIfVarExists(firstChild)) {
            return;
        }
        checkInited(secondChild);
        String firstChildType = getVarType(firstChild);
        SimpleNode scopeNode = getScope(node);

        /* Handling the case where a variable is initialized with "new" keyword */
        if(secondChild.toString().equals("New") && secondChild.getGrandchild() == null) {
            Identifier id1 = (Identifier) firstChild.getImage();
            Symbol symb1 = symbolTable.getVarSymbol(id1.name, getCurrentMethod(node));
            symb1.setInited();

            if(secondChild.firstChildName().equals("ObjCreation")) {
                ObjCreation objCreation = (ObjCreation) ((SimpleNode) secondChild.jjtGetChild(0)).getImage();
                if (symb1.getType().equals(symbolTable.getExtendsClass())) {
                    String assignedClass = objCreation.className;
                    symb1.setType(assignedClass);
                    if (assignedClass.equals(symbolTable.getClassName())) {
                        setInitedWithinScope(symb1, scopeNode);
                        return;
                    }
                }
                if (!symb1.getType().equals(objCreation.className)) {
                    errors.add(new SemanticError("Initialization is valid! " + firstChildType + " type on the left and " + objCreation.className + " on the right!", node.token, true));
                }
                return;
            }
        }
        String secondChildType = getVarType(secondChild);
        if (secondChildType.equals("")) return;

        if(!firstChildType.equals(secondChildType)) { // Throws an exception if types are not compatible
            errors.add(new SemanticError("Incompatible types! " + firstChildType + " on the left and " + secondChildType + " on the right!", node.token, true));
        }

        Identifier id1 = (Identifier) firstChild.getImage();
        Symbol symb1 = symbolTable.getVarSymbol(id1.name, getCurrentMethod(node));
        setInitedWithinScope(symb1, scopeNode);

        /* Sets lhs = rhs value */
        setValue(firstChild, secondChild, node);
    }

    /* Sets the value of lhs = rhs based on rhs' value */
    public void setValue(SimpleNode lhs, SimpleNode rhs, SimpleNode assignNode) {
        Identifier id1 = (Identifier) lhs.getImage();
        Symbol symb1 = symbolTable.getVarSymbol(id1.name, getCurrentMethod(assignNode));

        switch(rhs.toString()){
            case "Integer":
                symb1.setValue(getIntegerValue(rhs));
                break;
            case "Boolean":
                symb1.setValue(getBooleanValue(rhs, symbolTable));
                break;
            case "Identifier":
                symb1.setValue(getIdentifierValue(rhs, assignNode, symbolTable));
                break;
            case "AddSub":
            case "MultDiv":
                symb1.setValue(getOperationValue(rhs, symbolTable));
                break;
            case "And":
                symb1.setValue(getLogicalOpValue(rhs, symbolTable));
                break;
            case "Not":
                symb1.setValue(getNotValue(rhs, symbolTable));
                break;
            case "New":  /* just to set array length */
                if (rhs.getGrandchild().toString().equals("Integer"))
                    symb1.setLength(Integer.parseInt(rhs.getGrandchild().getImage().toString()));
                if (rhs.getGrandchild().toString().equals("Identifier")) {
                    String identifier_value = getIdentifierValue(rhs.getGrandchild(), assignNode, symbolTable);
                    try {
                        symb1.setLength(Integer.parseInt(identifier_value));
                    } catch (NumberFormatException e) {
                        break;
                    }
                }
                break;
        }
    }

    /* Sets the variable associated with the specified symbol as initialized for the given scope */
    public void setInitedWithinScope(Symbol symb, SimpleNode scopeNode) {
        if(symbolTable.getGlobalVarSymbol(symb.getName()) != null) { // Global variable
            symb.setInited();

        } else {
            symb.addScopeNode(scopeNode); // Sets the left hand side variable as initialized for this scope
            handleIfElseInitialization(symb, scopeNode);
        }
    }

    /* Returns the variable type associated with the input node or an empty string if type cannot be found */
    public String getVarType(SimpleNode node) {
        String nodeName = node.toString();
        switch(nodeName) {
            case "Length":
            case "ArrayIndex":
            case "Integer": // Integers and arithmetic
            case "AddSub":  // operations always return
            case "MultDiv": // an integer value
                return "int";
            case "Boolean": // Booleans and
            case "And":     // logical operations
            case "Or":      // always return
            case "Not":     // a boolean value
            case "LessThan":
                return "boolean";
            case "Identifier": // order matters
                Identifier id = (Identifier) ((SimpleNode) node).getImage();
                String firstChildName = node.firstChildName();
                if(firstChildName.equals("ArrayIndex") || firstChildName.equals("Length")) { // element of array
                    return "int";
                }
                MethodForm methodForm = getCurrentMethod(node);
                Symbol var = symbolTable.getVarSymbol(id.name, methodForm);
                if(firstChildName.equals("CallMethod")) { // import or class method
                    Symbol symb = symbolTable.getVarSymbol(id.name, methodForm);
                    if(symb != null && symb.getType().equals(symbolTable.getClassName())) { // class method
                        SimpleNode child = (SimpleNode) node.jjtGetChild(0);
                        return getVarType(child);
                    }
                    SimpleNode child = (SimpleNode) node.jjtGetChild(0);
                    SimpleNode grandChild = child.getLastChild();
                    String grandChildName = grandChild.toString();
                    if(grandChildName.equals("Length") || grandChildName.equals("ArrayIndex")) {
                        return "int";
                    }
                    MethodForm usedForm = getUsedForm(child);
                    String library = (var != null)? var.getType() : id.name;
                    Method method = symbolTable.getImportedMethod(usedForm, library);
                    if(method != null) {
                        return method.getReturnType();
                    }
                }
                if (var != null) { // variable
                    return var.getType();
                }
                break;
            case "CallMethod": // class method
                SimpleNode lastChild = (SimpleNode) node.jjtGetChild(node.jjtGetNumChildren() - 1);
                if (!lastChild.toString().equals("EndMethod")) {
                    return getVarType(lastChild);
                }

                MethodForm usedForm = getUsedForm(node);
                Method method = symbolTable.getMethod(usedForm);
                if(method != null) {
                    return method.getReturnType();
                } else {
                    method = symbolTable.getImportedMethod(usedForm, "");
                    if(method != null) {
                        return method.getReturnType();
                    }
                }
                break;
            case "This": // class or extended class method
                if(node.firstChildName().equals("CallMethod")) {
                    SimpleNode child = (SimpleNode) node.jjtGetChild(0);
                    return getVarType(child);
                } else {
                    return symbolTable.getClassName();
                }
            case "New":
                SimpleNode grandChild = node.getGrandchild();
                if(node.firstChildName().equals("IntArray"))
                    return "int[]";
                if(grandChild != null) {
                    return getVarType(grandChild);
                }
                break;
            default:
                return "";
        }
        return "";
    }

    /* Returns the form (type of arguments) that one is looking for when calling a function */
    public MethodForm getUsedForm(SimpleNode callMethod) {
        MethodForm usedForm = new MethodForm(((CallMethod) callMethod.getImage()).method);
        for(int i = 0; i < callMethod.jjtGetNumChildren(); i++) {
            SimpleNode child = (SimpleNode) callMethod.jjtGetChild(i);
            if(child.toString().equals("EndMethod"))
                break;
            usedForm.addType(getVarType(child));
        }
        return usedForm;
    }

    /* Checks if the variable is being declared with a correct type */
    public void checkVarType(SimpleNode node) throws ParseException {
        VarDeclaration vd = (VarDeclaration) node.getImage();
        if (!symbolTable.checkIfTypeExists(vd.type))
            errors.add(new SemanticError("Type " + vd.type + " cannot be instantiated!", node.token, true));
    }

    /* Checks if the logical operand "not" is being called with the boolean type */
    public void checkOperNot(SimpleNode node) throws ParseException {
        SimpleNode child = (SimpleNode) node.jjtGetChild(0);
        String childType = getVarType(child);
        if (!childType.equals("boolean")) {
            errors.add(new SemanticError("Logical operation 'not' should be called with a boolean type!", child.token, false));
        }
    }

    /* Checks if the condition of an if statement is a boolean */
    public void checkCondition(SimpleNode node) throws ParseException {
        SimpleNode child = (SimpleNode) node.jjtGetChild(0);
        String childType = getVarType(child);
        if (!childType.equals("boolean")) {
            errors.add(new SemanticError("The condition for an if statement should be of boolean type!", child.token, false));
        }
    }

    /* Checks if the object that is the target of a function call is initialized */
    private void checkThisInited(SimpleNode node) throws ParseException {
        SimpleNode parent = (SimpleNode) node.jjtGetParent();
        if(!parent.toString().equals("Identifier") || parent == null)
            return;
        Identifier id = (Identifier) parent.getImage();
        Symbol s = symbolTable.getVarSymbol(id.name, getCurrentMethod(node));
        if(s == null) { // static
            return;
        }
        if(!isInitedWithinScope(s, node)) {
            String message = "Variable " + s.getName() + " might not be initialized!";
            if(treatNotInitiedVarAsError)
                errors.add(new SemanticError(message, node.token, true));
            else {
                if (!getVarType(node).equals(""))
                    new Warning(message, node.token, true);
            }
        }
    }

    /* Returns the scope node, i.e. the closest If, Else, While, Main or MethodDeclaration to the specified node */
    public static SimpleNode getScope(SimpleNode node) {
        SimpleNode parent = (SimpleNode) node.jjtGetParent();
        List<String> scopeNodesNames = new ArrayList<String>() {{
            add("Method"); add("Main"); add("If"); add("Else"); add("While");
        }};
        while(!scopeNodesNames.contains(parent.toString())) { // While a scope node is not found, search the parent node
            parent = (SimpleNode) parent.jjtGetParent();
        }
        return parent;
    }

    /* Verifies whether the variable is initialized within the specified scope */
    public boolean isInitedWithinScope(Symbol variable, SimpleNode node) {
        if (variable.isInited())
            return true;

        SimpleNode scopeNode = getScope(node);

        while(!variable.containsScopeNode(scopeNode)) {
            if(scopeNode.toString().equals("Main") || scopeNode.toString().equals("Method")) {
                return false;
            }
            scopeNode = getScope(scopeNode);
        }
        return true;
    }

    /* Sets the parent of the specified node as a scope node for the specified symbol, handling the case when a variable is declared in both if and else body
     */
    public void handleIfElseInitialization(Symbol symb, SimpleNode node) {
        if(node.toString() != "Else") { // The following code only applies when a variable is initialized within an else scope
            return;
        }
        // Verifying if there already exists the respective IF scope node for that symbol
        SimpleNode parent = (SimpleNode) node.jjtGetParent();
        for (int i = 0; i < parent.jjtGetNumChildren(); i++) {
            SimpleNode child = (SimpleNode) parent.jjtGetChild(i);
            if (node.equals(child)) {
                SimpleNode prevChild = (SimpleNode)parent.jjtGetChild(i-1);
                if (symb.containsScopeNode(prevChild)) {
                    symb.addScopeNode(getScope(prevChild));
                    break;
                }
            }
        }
    }

    /* Gets the result of a operation less than */
    public static String getLessThanValue(SimpleNode node, SymbolTable symbolTable){
        SimpleNode lhs = (SimpleNode) node.jjtGetChild(0);
        SimpleNode rhs = (SimpleNode) node.jjtGetChild(1);

        String v1 = "";
        String v2 = "";

        switch (lhs.toString()){
            case "Identifier":
                v1 = getIdentifierValue(lhs, node, symbolTable);
                break;
            case "This":
                v1 = getThisValue(lhs, symbolTable);
                break;
            case "Integer":
                v1 = getIntegerValue(lhs);
                break;
            case "AddSub":
            case "MultDiv":
                v1 = getOperationValue(lhs, symbolTable);
                break;
        }

        switch(rhs.toString()){
            case "Identifier":
                v2 = getIdentifierValue(rhs, node, symbolTable);
                break;
            case "This":
                v2 = getThisValue(rhs, symbolTable);
                break;
            case "Integer":
                v2 = getIntegerValue(rhs);
                break;
            case "AddSub":
            case "MultDiv":
                v2 = getOperationValue(rhs, symbolTable);
                break;
        }

        if(Integer.parseInt(v1) < Integer.parseInt(v2)) return "true";
        return "false";
    }

    /* Gets the result of a call method using "this" */
    private static String getThisValue(SimpleNode node, SymbolTable symbolTable) {
        SimpleNode child = (SimpleNode) node.jjtGetChild(0);
        switch(child.toString()){
            case "CallMethod":
                MethodForm usedForm = new SemanticAnalyzer(symbolTable).getUsedForm(child);
                Method method = symbolTable.getMethod(usedForm);

                switch (method.getReturnType()){
                    case "int":
                    case "int[]":
                        return "0";
                    case "boolean":
                        return "true";
                }
        }
        return "";
    }

    /* Gets the result of a logical operation NOT */
    public static String getNotValue(SimpleNode node, SymbolTable symbolTable){
        String v = "";
        switch(node.toString()){
            case "Boolean":
                v = getBooleanValue(node, symbolTable);
                break;
            case "Identifier":
                v = getIdentifierValue(node, node, symbolTable);
                break;
            case "This":
                v = getThisValue(node, symbolTable);
                break;
            case "And":
                v = getLogicalOpValue(node, symbolTable);
                break;
            case "LessThan":
                v = getLessThanValue(node, symbolTable);
                break;
        }

        if(v.equals("true")) return "false";
        return "true";
    }

    /* Gets the value of an integer */
    public static String getIntegerValue(SimpleNode node){
        IntegerLiteral integer = (IntegerLiteral) node.getImage();
        return integer.toString();
    }

    /* Gets the value of a boolean */
    public static String getBooleanValue(SimpleNode node, SymbolTable symbolTable){
        Boolean bool = (Boolean) node.getImage();
        return (bool.toString());
    }

    /* Gets the value of an identifier */
    public static String getIdentifierValue(SimpleNode node, SimpleNode scopeNode, SymbolTable symbolTable){
        Identifier id = (Identifier) node.getImage();

        if(node.jjtGetNumChildren() == 0) { /* regular identifier like a = b */
            Symbol symb = symbolTable.getVarSymbol(id.name, getCurrentMethod(scopeNode));
            MethodForm methodForm = getCurrentMethod(scopeNode);
            Method method = symbolTable.getMethod(methodForm);

            if (symb != null) /* symbol has to be an argument of a function or inited within scope */
                if ((method.getArgument(symb.getName()) != null) || new SemanticAnalyzer(symbolTable).isInitedWithinScope(symb, scopeNode))
                    return (symb.getValue());

            return null;
        }

        SimpleNode child = (SimpleNode) node.jjtGetChild(0);
        switch(child.toString()){
            case "CallMethod": {
                MethodForm usedForm = new SemanticAnalyzer(symbolTable).getUsedForm(child);
                Method method = symbolTable.getMethod(usedForm);
                if (method == null) { /* handle imported methods */
                    Symbol var = symbolTable.getVarSymbol(id.name, SemanticAnalyzer.getCurrentMethod(node));
                    String library = (var != null) ? var.getType() : id.name;
                    method = symbolTable.getImportedMethod(usedForm, library);
                }
                switch (method.getReturnType()) { /* return generic values */
                    case "int":
                    case "int[]":
                        return "0";
                    case "boolean":
                        return "true";
                }
            }
                break;
            case "Length": /* node is an array */
                Symbol symb = symbolTable.getVarSymbol(id.name, getCurrentMethod(scopeNode));
                return Integer.toString(symb.getLength());
            case "ArrayIndex":
                return "0";
        }


        return "";
    }

    /* Gets the result of a operation like add, sub, mul or div */
    public static String getOperationValue(SimpleNode node, SymbolTable symbolTable){
        SimpleNode child1 = (SimpleNode) node.jjtGetChild(0);
        SimpleNode child2 = (SimpleNode) node.jjtGetChild(1);

        String v1 = "0";
        String v2 = "0";

        switch(child1.toString()){
            case "Integer":
                v1 = getIntegerValue(child1);
                break;
            case "Boolean":
                v1 = getBooleanValue(child1, symbolTable);
                break;
            case "Identifier":
                v1 = getIdentifierValue(child1, node, symbolTable);
                break;
            case "This":
                v1 = getThisValue(child1, symbolTable);
                break;
            case "AddSub":
            case "MulDiv":
                v1 = getOperationValue(child1, symbolTable);
                break;
        }

        switch(child2.toString()){
            case "Integer":
                v2 = getIntegerValue(child2);
                break;
            case "Boolean":
                v2 = getBooleanValue(child2, symbolTable);
                break;
            case "Identifier":
                v2 = getIdentifierValue(child2, node, symbolTable);
                break;
            case "This":
                v2 = getThisValue(child2, symbolTable);
                break;
            case "AddSub":
            case "MulDiv":
                v2 = getOperationValue(child2,symbolTable);
                break;
        }

        switch (node.toString()){
            case "AddSub":
            {
                AddSub operator = (AddSub) node.getImage();
                try {
                    if(operator.toString().equals("+"))
                        return Integer.toString(Integer.parseInt(v1) + Integer.parseInt(v2));
                    else
                        return Integer.toString(Integer.parseInt(v1) - Integer.parseInt(v2));
                } catch (NumberFormatException e){
                    break;
                }
            }
            case "MultDiv":
            {
                MulDiv operator = (MulDiv) node.getImage();
                if(operator.toString().equals("*"))
                    return Integer.toString(Integer.parseInt(v1) * Integer.parseInt(v2));
                else {
                    if(v2.equals("0")) v2 = "1";
                    return Integer.toString(Integer.parseInt(v1) / Integer.parseInt(v2));
                }
            }
        }
        return "0";
    }

    /* Gets the result of a logical operation AND */
    public static String getLogicalOpValue(SimpleNode node, SymbolTable symbolTable) {
        SimpleNode child1 = (SimpleNode) node.jjtGetChild(0);
        SimpleNode child2 = (SimpleNode) node.jjtGetChild(1);

        String v1="";
        String v2="";

        switch (child1.toString()){
            case "Boolean":
                v1 = getBooleanValue(child1, symbolTable);
                break;
            case "And":
                v1 = getLogicalOpValue(child1, symbolTable);
                break;
            case "Not":
                v1 = getNotValue(child1, symbolTable);
                break;
            case "Identifier":
                v1 = getIdentifierValue(child1, node, symbolTable);
                break;
            case "This":
                v1 = getThisValue(child1, symbolTable);
                break;
            case "LessThan":
                v1 = getLessThanValue(child1, symbolTable);
                break;
        }

        switch (child2.toString()){
            case "Boolean":
                v2 = getBooleanValue(child2, symbolTable);
                break;
            case "And":
                v2 = getLogicalOpValue(child2, symbolTable);
                break;
            case "Not":
                v2 = getNotValue(child2, symbolTable);
                break;
            case "Identifier":
                v2 = getIdentifierValue(child2, node, symbolTable);
                break;
            case "This":
                v2 = getThisValue(child2, symbolTable);
                break;
            case "LessThan":
                v2 = getLessThanValue(child2, symbolTable);
                break;
        }
        if (v1 == null || v2 == null) return "false";

        boolean b1 = v1.equals("true");
        boolean b2 = v2.equals("true");

        if(b1 && b2) return "true";
        return "false";
    }
}
