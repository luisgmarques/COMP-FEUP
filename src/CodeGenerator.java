import java.util.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class CodeGenerator {
    private SimpleNode root;
    private String filename;
    private StringBuilder builder;
    private SymbolTable symbolTable;
    private int labelCounter;
    private String currConditionMetLabel;
    private Stack<String> currentWhileLabelStack;

    private boolean optimizationO;
    /** Stack */
    private int stack;
    private int maxStackSize;

    public CodeGenerator(SimpleNode root, SymbolTable symbolTable, boolean optimization){
        // Search for imports
        this.root = (SimpleNode) root.jjtGetChild(0);
        this.symbolTable = symbolTable;
        this.labelCounter = 0;
        this.currentWhileLabelStack = new Stack<String>();
        this.optimizationO = optimization;
        currConditionMetLabel = "L";

        // Skip import nodes
        int i = 1;
        while (this.root.toString().equals("Import")) {
            this.root = (SimpleNode) root.jjtGetChild(i++);
        }

        this.builder = new StringBuilder();

        stack = 0;
        maxStackSize = 0;

        init();

        try {
            generateJasminFile(this.root);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    /* Creates the file containing the generated Jasmin instructions */
    public void generateJasminFile(SimpleNode node) throws IOException {
        this.filename = "jvm/" + symbolTable.getClassName() + ".j"; // <class_name>.j

        FileWriter fw = new FileWriter(filename, false);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out = new PrintWriter(bw);

        out.append(builder);
        out.close();
    }

    /* Starts the process of building the jasmin file */
    public void init(){
        buildFileHeader();
        buildGlobalVariables();
        buildMethods();
    }

    /* Prints the string containing the file content */
    public void printBuilder(){
        System.out.println(builder.toString());
    }

    /* Fills the file with the header instructions, containing a reference to the class name and parent class */
    public void buildFileHeader(){
        builder.append(".class public " + symbolTable.getClassName() + "\n");
        builder.append(".super " + (symbolTable.getExtendsClass().equals("") ? "java/lang/Object" : symbolTable.getExtendsClass()) + "\n\n");
    }

    /* Generates the instructions for the initializer method */
    public void buildInitializer() {
        builder.append(".method public <init>()V\n");
        builder.append("\taload_0\n");

        builder.append("\tinvokespecial " + (symbolTable.getExtendsClass().equals("") ? "java/lang/Object" : symbolTable.getExtendsClass()) + "/<init>()V\n");

        builder.append("\treturn\n");
        builder.append(".end method\n\n");
    }

    public void buildGlobalVariables() {
        for (int i = 0; i < root.jjtGetNumChildren(); i++) {
            SimpleNode child = (SimpleNode) root.jjtGetChild(i);
            if (child.toString().equals("VarDeclaration")) {
                buildVarDeclaration(child);
            }
        }
        builder.append('\n');
    }

    /* Builds the class body related instructions */
    public void buildMethods(){
        for (int i = 0; i < root.jjtGetNumChildren(); i++) {
            SimpleNode child = (SimpleNode) root.jjtGetChild(i);
            switch(child.toString()){
                case "Main":
                    buildMain(child);
                    break;
                case "Method":
                    buildMethod(child);
                    break;
            }
        }
    }

    /* Generates the instructions associated with class vars' declaration */
    public void buildVarDeclaration(SimpleNode node){
        VarDeclaration var = (VarDeclaration) node.getImage();

        String varType = "";

        Symbol symbol = symbolTable.getGlobalVarSymbol((var.name).toString());

        switch (var.type){
            case "int":
                varType = "I";
                break;
            case "int[]":
                varType = "[I";
                break;
        }

        builder.append(".field public " + symbol.getName() + " " + varType +"\n");
    }

    /* Generates the instructions for method declarations */
    public void buildMethod(SimpleNode node){
        MethodDeclaration methodDeclaration = (MethodDeclaration) node.getImage();

        // Build Method Header
        int num_args = buildMethodHeader(methodDeclaration);

        // Build Method Body
        buildMethodBody(node, num_args);

        // Build Method .End
        buildMethodEnd(methodDeclaration);
    }


    /* Generates the instructions for the method arguments and return type */
    public int buildMethodHeader(MethodDeclaration methodDeclaration){
        String returnType = "";
        String argsType = "";

        int num_args = 0;

        for (Arg arg : methodDeclaration.args) {
            switch (arg.type.toString()) {
                case "int":
                    argsType += "I";
                    break;
                case "int[]":
                    argsType += "[I";
                    break;
                case "boolean":
                    argsType += "Z";
            }
            num_args++;
        }

        switch (methodDeclaration.returnType) {
            case "int":
                returnType = "I";
                break;
            case "boolean":
                returnType = "Z";
                break;
            case "int[]":
                returnType = "[I";
                break;
            case "void":
                returnType = "V";
                break;
        }

        builder.append(".method public " + methodDeclaration.name + "(" + argsType + ")" + returnType + "\n");

        return num_args;
    }

    /* Generates the instructions for the method end */
    private void buildMethodEnd(MethodDeclaration methodDeclaration) {
        String returnType = "";
        switch (methodDeclaration.returnType) {
            case "int":
            case "boolean":
                returnType = "i";
                break;
            case "int[]":
                returnType = "a";
                break;
            case "void":
                returnType = "";
                break;
        }

        /** Stack */
        if (!returnType.equals("")) {
            stack -= 1; // returned value [-1]
            update_max_stack_size();
        }

        /** Updates .limit stack */
        String string = ".limit stack 99";
        int index = builder.indexOf(string);
        builder.replace(index, index + string.length(), ".limit stack " + maxStackSize);

        // Reset stack
        stack = 0;
        maxStackSize = 0;

        builder.append('\t' + returnType + "return\n");
        builder.append(".end method\n\n");
    }


    /* Generates the instructions for the method body */
    private void buildMethodBody(SimpleNode functionNode, int num_args) {

        int num_vars = 1;

        // VarDeclaration counter
        for (int i = 0; i < functionNode.jjtGetNumChildren(); i++) {
            SimpleNode functionChild = (SimpleNode) functionNode.jjtGetChild(i);
            if (functionChild.toString().equals("VarDeclaration"))
                num_vars++;
        }


        builder.append("\t.limit stack 99" + '\n');
        int var_index = builder.length();
        builder.append("\t.limit locals " + (num_vars + num_args) + '\n');
        int var_index_end = builder.length();

        int init_index = builder.length();

        for (int i = 0; i < functionNode.jjtGetNumChildren(); i++) {
            SimpleNode functionChild = (SimpleNode) functionNode.jjtGetChild(i);
            generateExpStat(functionChild);
        }

        int end_index = builder.length();

        if (this.optimizationO) {

            VarOptimization optO = new VarOptimization(builder.substring(init_index, end_index), num_vars - 1, (num_vars+num_args));

            String opt = optO.getOptimization();
            int new_num_vars = optO.getNewVars();

            builder.replace(var_index, var_index_end, "\t.limit locals " + (new_num_vars + 1 + num_args) + '\n');
            builder.replace(init_index, end_index, opt);
        }
     }

    /* Generates the instructions for the main function */
    public void buildMain(SimpleNode node){

        buildInitializer();

        // Build Main Header
        builder.append(".method public static main([Ljava/lang/String;)V\n");

        // Build Main Body
        buildMethodBody(node, 0);

        /** Updates .limit stack */
        String string = ".limit stack 99";
        int index = builder.indexOf(string); // index
        builder.replace(index, index + string.length(), ".limit stack " + maxStackSize);

        stack = 0;
        maxStackSize = 0;
        /** ---- */

        // Build Main .End
        builder.append("\treturn\n");
        builder.append(".end method\n");

        builder.append("\n");
    }

    /* Generates the instructions related to the assignments of variables */
    private void generateAssign(SimpleNode node){
        // Checking if left side is identifier
        SimpleNode leftSide = (SimpleNode) node.jjtGetChild(0);
        SimpleNode rightSide = (SimpleNode) node.jjtGetChild(1);

        if(leftSide.jjtGetNumChildren() == 1) { // int[]
            loadIdentifier(leftSide);
            generateArrayAccess(leftSide);
            generateRightExpression(rightSide);
            builder.append("\tiastore\n");

            /** Stack */
            stack -= 3; // arrayref[-1] + index[-1] + value[-1]
            update_max_stack_size();

        } else { // Simple assign
            int init_index = builder.length();
            int current_max_stack = maxStackSize;

            generateRightExpression(rightSide);
            generateLeftExpression(leftSide);

            if(!rightSide.toString().equals("MultDiv")) {

                int final_index = builder.length();

                String portion = builder.substring(init_index, final_index);

                String iinc_code = generateiinc(portion);

                if (!iinc_code.equals("")) {
                    builder.replace(init_index, final_index, iinc_code);
                    maxStackSize = current_max_stack;
                }
            }
        }
    }

    /* Generates the instructions related to the left hand side of an assign */
    private void generateLeftExpression(SimpleNode leftSide) {
        storeIdentifier(leftSide);
    }

    /* Generates the instructions related to the access of an array*/
    private void generateArrayAccess(SimpleNode leftSide) {
        SimpleNode index = (SimpleNode) (leftSide.jjtGetChild(0)).jjtGetChild(0);

        switch (index.toString()){
            case "Integer":
                loadInteger(index);
                break;
            case "Identifier":
                generateIdentifier(index);
                break;
            case "This":
                generateThis(index);
                break;
            case "AddSub":
            case "MultDiv":
                generateArithmeticOp(index);
                break;
        }
    }

    /* Generates the instructions related to the right hand side of an assign */
    private void generateRightExpression(SimpleNode node) {
        if (node.toString().equals("AddSub") || node.toString().equals("MultDiv")) {
            generateArithmeticOp(node);
        } else {
            switch (node.toString()) {
                case "New":
                    generateNew(node);
                    break;
                case "Identifier":
                    generateIdentifier(node);
                    break;
                case "Integer":
                    loadInteger(node);
                    break;
                case "Boolean":
                    loadLiteralBool(node);
                    break;
                case "And":
                    generateLogicalOp(node);
                    break;
                case "Not":
                    generateNotOp(node);
                    break;
                case "LessThan":
                    generateLessThanAssign(node);
                    break;
                case "This":
                    generateThis(node);
                    break;
            }
        }
    }

    /* Generates the instructions for when a variable is declared with the keyword "new" */
    private void generateNew(SimpleNode node){
        SimpleNode child = (SimpleNode) node.jjtGetChild(0);
        // Can be an object or array initialization
        if(child.toString().equals("IntArray")){
            SimpleNode grandChild = (SimpleNode) child.jjtGetChild(0);

            switch(grandChild.toString()){
                case "Integer":
                    loadInteger(grandChild);
                    break;
                case "Identifier":
                    loadInt(grandChild);
                    break;
            }

            builder.append("\tnewarray int\n");
        } else {
            ObjCreation obj = (ObjCreation) child.getImage();

            builder.append("\tnew " + obj.className + "\n");
            builder.append("\tdup\n");
            builder.append("\tinvokespecial " + obj.className + "/<init>()V\n");

            if (child.jjtGetNumChildren() > 0) {
                SimpleNode grandChild = (SimpleNode) child.jjtGetChild(0);
                if (grandChild.toString().equals("CallMethod")) {
                    generateCallMethod(child);
                }
            }


            /** Stack */
            stack += 2; // new[+1] + dup[+1]
            update_max_stack_size();
        }

    }

    /* Generates the instructions for when an integer value is being assigned */
    private void loadInteger(SimpleNode node){
        IntegerLiteral img = (IntegerLiteral) node.getImage();
        int value = Integer.parseInt(img.value);

        if ((value >= 0) && (value <= 5)) {
            builder.append("\ticonst_" + value + "\n");
        } else if (value == -1) {
            builder.append("\ticonst_m1" + "\n");
        } else if (value >= -128 && value <= 127) {
            builder.append("\tbipush " + value + "\n");
        } else if (value >= -32768 && value <= 32767) {
            builder.append("\tsipush " + value + "\n");
        } else {
            builder.append("\tldc " + value + "\n");
        }

        /** Stack */
        stack += 1; // load const [+1]
        update_max_stack_size();
    }

    /* Generates the instructions for when a boolean var is being assigned */
    public void loadLiteralBool(SimpleNode node) {
        Boolean bool = (Boolean) node.getImage();
        String value = bool.value;
        if (value.equals("true")) {
            builder.append("\ticonst_1\n");
        } else {
            builder.append("\ticonst_0\n");
        }

        /** Stack */
        stack += 1; // load const [+1]
        update_max_stack_size();
    }

    /* Generates the instructions for when a variable value is being assigned */
    public void loadIdentifier(SimpleNode node){
        MethodForm methodForm = SemanticAnalyzer.getCurrentMethod(node);
        Identifier id = (Identifier) (node.getImage());

        Symbol symbol;

        if(symbolTable.getLocalVarSymbol(id.toString(), methodForm) != null){
            symbol = symbolTable.getVarSymbol(id.toString(), methodForm);
            loadLocalVariable(symbol);
        } else if(symbolTable.getGlobalVarSymbol(id.toString()) != null) {
            symbol = symbolTable.getGlobalVarSymbol(id.toString());
            loadGlobalVariable(symbol, node);
        }
    }

    /* Generates the instructions for when a local variable is being loaded */
    private void loadLocalVariable(Symbol symbol) {
        String type = "";

        switch (symbol.getType()){
            case "int":
            case "boolean":
                type = "i";
                break;
            case "int[]":
            default:
                type = "a";
                break;
        }

        if (symbol.getIndex() < 4)
            builder.append("\t" + type + "load_" + symbol.getIndex() +  "\n");
        else
            builder.append("\t" + type + "load " + symbol.getIndex() +  "\n");

        /** Stack */
        stack += 1; // load var [+1]
        update_max_stack_size();
    }

    /* Generates the instructions for when a global variable is being loaded */
    public void loadGlobalVariable(Symbol symbol, SimpleNode node) {
        String type = "";

        switch (symbol.getType()){
            case "int":
                type = "I";
                break;
            case "int[]":
                type = "[I";
                break;
            case "boolean":
                type = "Z";
                break;
        }
        // Loads objectref
        builder.append("\taload_0\n");

        /** Stack */
        stack += 1; // aload_0 [+1]
        update_max_stack_size();

        builder.append("\tgetfield " + symbolTable.getClassName() + "/" + symbol.getName() + " " + type + "\n");
    }


    /* Generates the instructions for when an identifier is being stored */
    public void storeIdentifier(SimpleNode node){
        MethodForm methodForm = SemanticAnalyzer.getCurrentMethod(node);
        Identifier id = (Identifier) (node.getImage());
        Symbol symbol;


        if(symbolTable.getLocalVarSymbol(id.toString(), methodForm) != null){ // local variable
            symbol = symbolTable.getVarSymbol(id.toString(), methodForm);
            storeLocalVariable(symbol);
        }
        else
        if(symbolTable.getGlobalVarSymbol(id.toString()) != null){
            symbol = symbolTable.getGlobalVarSymbol(id.toString());
            storeGlobalVariable(symbol);
        }
    }

    /* Generates the instructions for when a local variable is being stored */
    private void storeLocalVariable(Symbol symbol) {
        String type = "";

        switch (symbol.getType()){
            case "int":
            case "boolean":
                type = "i";
                break;
            case "int[]":
            default:
                type = "a";
                break;
        }

        if (symbol.getIndex() < 4)
            builder.append("\t" + type + "store_" + symbol.getIndex() + "\n");
        else
            builder.append("\t" + type + "store " + symbol.getIndex() + "\n");

        /** Stack */
        stack -= 1; // store var [-1]
        update_max_stack_size();
    }

    /* Generates the instructions for when a global variable is being stored */
    private void storeGlobalVariable(Symbol symbol) {
        String type = "";

        switch (symbol.getType()){
            case "int":
                type = "I";
                break;
            case "int[]":
                type = "[I";
                break;
            case "boolean":
                type = "Z";
                break;
        }

        // Loads objectref and swap top values to perform putfield
        builder.append("\taload_0\n");
        builder.append("\tswap\n");
        builder.append("\tputfield " + ((ClassDeclaration) this.root.getImage()).name + "/" + symbol.getName() + " " + type + "\n");

        /** Stack */
        stack -= 1; // store global var [-1]
        update_max_stack_size();
    }


    /* Generates the instructions for arithmetic operations */
    private void generateArithmeticOp(SimpleNode node) {
        String op ="";
        if (node.getImage() instanceof AddSub) {
            op = ((AddSub) node.getImage()).operator;
        }
        else if (node.getImage() instanceof MulDiv) {
            op = ((MulDiv) node.getImage()).operator;
        }

        SimpleNode lhs = (SimpleNode) node.jjtGetChild(0);
        SimpleNode rhs = (SimpleNode) node.jjtGetChild(1);

        loadInt(lhs);
        loadInt(rhs);

        switch (op) {
            case "+":
                builder.append("\tiadd\n");
                break;
            case "-":
                builder.append("\tisub\n");
                break;
            case "*":
                builder.append("\timul\n");
                break;
            case "/":
                builder.append("\tidiv\n");
        }
        /** Stack */
        stack -= 1; // lhs[-1] + rhs[-1] + result[+1]
        update_max_stack_size();
    }

    /* Generates the instructions to load the value from a comparison */
    private void generateLessThanAssign(SimpleNode node) {
        generateLessThanCond(node);
        String conditionNotMetLabel = createLabel();
        String conditionMetLabel = createLabel();
        builder.append("\tif_icmpge " + conditionNotMetLabel + "\n");
        builder.append("\ticonst_1\n");
        builder.append("\tgoto " + conditionMetLabel + "\n");
        builder.append(conditionNotMetLabel + ":\n");
        builder.append("\ticonst_0\n");
        builder.append(conditionMetLabel + ":\n");

        /** Stack */
        stack -= 1; // if_icmpge[-2] + i_const_x[+1]
        update_max_stack_size();
    }

    /* Generates the instructions for "not" operation */
    private void generateNotOp(SimpleNode node) {
        SimpleNode child = (SimpleNode) node.jjtGetChild(0);

        loadBool(child);
        builder.append("\ticonst_1\n");
        builder.append("\tixor\n");
    }

    /* Generates the instructions for a logical operation */
    private void generateLogicalOp(SimpleNode node) {
        SimpleNode lhs = (SimpleNode) node.jjtGetChild(0);
        SimpleNode rhs = (SimpleNode) node.jjtGetChild(1);

        String lhsIsTrue = createLabel();
        String lhsIsFalse = createLabel();

        checkLhsIsTrue(lhs, lhsIsTrue);

        /* lhs has false value */
        loadBool(lhs);
        builder.append("\tgoto " + lhsIsFalse + "\n");

        /* lhs has true value -> regular and operation */
        builder.append(lhsIsTrue + ":\n");
        loadBool(lhs);
        loadBool(rhs);
        builder.append("\tiand\n");

        /** Stack */
        stack -= 1; // lhs[-1] + rhs[-1] + result[+1]
        update_max_stack_size();

        builder.append(lhsIsFalse + ":\n");
    }


    private void checkLhsIsTrue(SimpleNode lhs, String isTrueLabel) {
        String label = createLabel();
        builder.append(label + ":\n");

        loadBool(lhs);
        loadLiteralTrue();

        builder.append("\tif_icmpeq " + isTrueLabel + "\n"); /* if (true==true) then generate AND operation */
    }

    /* Loads a literal boolean with true value */
    private void loadLiteralTrue(){
        builder.append("\ticonst_1\n");

        stack += 1;
        update_max_stack_size();

        /** Stack */
            stack -= 1; // lhs[-1] + rhs[-1] + result[+1]
            update_max_stack_size();
    }

    /* Generates the instructions for an identifier */
    private void generateIdentifier(SimpleNode node) {
        loadIdentifier(node);

        if (node.jjtGetNumChildren() > 0) {
            SimpleNode child = (SimpleNode) node.jjtGetChild(0);

            switch (child.toString()) {
                case "CallMethod":
                    generateCallMethod(node);
                    break;
                case "Length":
                    builder.append("\tarraylength\n");
                    break;
                case "ArrayIndex":
                    generateArrayAccess(node);
                    builder.append("\tiaload\n");
                    break;
            }
            return;
        }

    }

    /* Generates the instructions for call methods */
    private void generateCallMethod(SimpleNode node) { //node's image is either lib or obj identifier
        SimpleNode child = (SimpleNode) node.jjtGetChild(0);
        String className = "";
        String idName = "";

        MethodForm usedForm = new SemanticAnalyzer(symbolTable).getUsedForm(child);
        Method method = symbolTable.getMethod(usedForm);

        if (method == null) {
            if(node.toString().equals("This")) /* class extends another: matching classExtends with imports */
                idName = symbolTable.getExtendsClass();
            else
                idName = ((Identifier) node.getImage()).name;

            Symbol var = symbolTable.getVarSymbol(idName, SemanticAnalyzer.getCurrentMethod(node));
            String library = (var != null) ? var.getType() : idName;
            method = symbolTable.getImportedMethod(usedForm, library);
            className = library;
        }
         else {
            className = symbolTable.getClassName();
        }

        generateCallMethodArguments(child);
        generateCallMethodInvoke(child, method, className);
    }

    /* Generates the instructions to load arguments from call method */
    private void generateCallMethodArguments(SimpleNode node) {
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            SimpleNode argument = (SimpleNode) node.jjtGetChild(i);

            switch (argument.toString()) {
                case "AddSub":
                case "MultDiv":
                    generateArithmeticOp(argument);
                    break;
                case "Integer":
                    loadInteger(argument);
                    break;
                case "Identifier":
                    generateIdentifier(argument);
                    break;
                default:
                    break;
            }
        }
    }

    /* Builds call method invocation */
    private void generateCallMethodInvoke(SimpleNode node, Method method, String className) {
        String methodName = className + "/" + method.getName();
        String methodReturnType = "";
        String methodArgs = "";

        switch(method.getReturnType()){
            case "int":
                methodReturnType = "I";
                break;
            case "boolean":
                if(method.isStatic())
                    methodReturnType = "Z";
                else
                    methodReturnType = "Z";
                break;
            case "int[]":
                methodReturnType = "[I";
                break;
            case "void":
                methodReturnType = "V";
                break;
        }

        for(String type: method.getForm().argumentTypes){
            switch(type){
                case "int":
                    methodArgs += "I";
                    break;
                case "int[]":
                    methodArgs += "[I";
                    break;
                case "boolean":
                    methodArgs += "Z";
                    break;
            }

            /** Stack */
            stack -= 1; // arg(s) used [-1]
            update_max_stack_size();
        }

        if(method.isStatic()){
            builder.append("\t" + "invokestatic " + methodName + "(" + methodArgs + ")" + methodReturnType + "\n");

            /** Stack */
            stack += 1; // result [+1]
            update_max_stack_size();
        }
        else {
            builder.append("\t" + "invokevirtual " + methodName + "(" + methodArgs + ")" + methodReturnType + "\n");
        }
        if (!node.checkIfPredecessorExists("Assign") && !method.getReturnType().equals("void")
            && node.getLastChild().toString().equals("EndMethod") && !node.isCondExpression()
            && !node.checkIfPredecessorExists("CallMethod") && !node.checkIfPredecessorExists("Return")) {
            builder.append("\tpop\n");
        }
    }

    /* Generates the instructions for return */
    private void generateReturn(SimpleNode node) {
        if(node.jjtGetNumChildren() > 0){
            for(int i=0; i < node.jjtGetNumChildren(); i++){
                SimpleNode child = (SimpleNode) node.jjtGetChild(i);
                switch(child.toString()){
                    case "Identifier":
                        generateIdentifier(child);
                        break;
                    case "Integer":
                        loadInteger(child);
                        break;
                    case "AddSub":
                    case "MultDiv":
                        generateArithmeticOp(child);
                        break;
                    case "Boolean":
                    case "LessThan":
                    case "Not":
                    case "And":
                        loadBool(child);
                        break;
                    case "This":
                        builder.append("\taload_0\n");

                        /** Stack */
                        stack += 1; // aload_0 [+1]
                        update_max_stack_size();

                        generateCallMethod(child);
                        break;
                }
            }
        }
    }

    /* Generates the instructions for a node of int type */
    private void loadInt(SimpleNode node) {
        switch(node.toString()) {
            case "Identifier":
                generateIdentifier(node);
                break;
            case "AddSub":
            case "MultDiv":
                generateArithmeticOp(node);
                break;
            case "Integer":
                loadInteger(node);
                break;
            case "This":
                builder.append("\taload_0\n");

                /** Stack */
                stack += 1; // aload_0 [+1]
                update_max_stack_size();

                generateCallMethod(node);
                break;
            default:
                System.err.println("Node does not match an integer type!");
        }
    }

    /* Generates the instructions for a node of boolean type */
    private void loadBool(SimpleNode node) {
        switch(node.toString()) {
            case "Identifier":
                generateIdentifier(node);
                break;
            case "And":
                generateLogicalOp(node);
                break;
            case "Boolean":
                loadLiteralBool(node);
                break;
            case "This":
                builder.append("\taload_0\n");

                /** Stack */
                stack += 1; // aload_0 [+1]
                update_max_stack_size();

                generateCallMethod(node);
                break;
            case "Not":
                generateNotOp(node);
                break;
            case "LessThan":
                generateLessThanAssign(node);
                break;
            default:
                System.err.println("Node does not match a boolean type!");
        }
    }

    /* Generates the instructions for the if and while condition body */
    private void generateConditionBody(SimpleNode node) {
        int i = (node.toString().equals("If") || node.toString().equals("While")) ? 1 : 0;

        for(; i < node.jjtGetNumChildren(); i++) {
            generateExpStat((SimpleNode) node.jjtGetChild(i));
        }
    }

    /* Creates a label with unused index */
    private String createLabel() {
        return "L" + ++labelCounter;
    }

    /* Generates the instructions for an expression/statement node */
    private void generateExpStat(SimpleNode node) {

        String nodeName = node.toString();
        switch (nodeName) {
            case "Assign":
                generateAssign(node);
                break;
            case "Identifier":
                generateIdentifier(node);
                break;
            case "If":
                generateIfCondition(node);
                break;
            case "Else":
                generateConditionBody(node);
                builder.append(currConditionMetLabel + ":\n");
                break;
            case "Return": //to load needed variables
                generateReturn(node);
                break;
            case "This":
                generateThis(node);
                break;
            case "While":
                if(optimizationO)
                    generateWhileOptimization(node);
                else
                    generateWhileCondition(node);
                break;
            case "Continue":
                generateContinue(node);
                break;
            case "Break":
                generateBreak(node);
                break;
            default:
                break;
        }
    }

    /* Generates the instructions for 'continue' */
    private void generateContinue(SimpleNode node) {
        String currentWhileLabel = currentWhileLabelStack.peek();
        builder.append("\tgoto " + currentWhileLabel + "\n");
    }

    /* Generates the instructions for 'break' */
    private void generateBreak(SimpleNode node) {
        String currentWhileLabel = currentWhileLabelStack.peek();
        builder.append("\tgoto " + currentWhileLabel + "_end\n");
    }

    /* Generates the instructions for a class' method call with "this" */
    private void generateThis(SimpleNode node) {
        builder.append("\taload_0\n");

        /** Stack */
        stack += 1; // aload_0 [+1]
        update_max_stack_size();

        if (node.jjtGetNumChildren() > 0) {
            try {
                generateCallMethod(node);
            } catch (Exception e) {
                System.err.println("Error occurred!");
            }
        }
    }

    /* Generates the instructions associated with while statement */
    private void generateWhileCondition(SimpleNode whileNode){
        String label = createLabel();
        currentWhileLabelStack.push(label);

        builder.append(label + ":\n");
        SimpleNode condition = (SimpleNode) whileNode.jjtGetChild(0);

        switch(condition.toString()){
            case "LessThan":
                generateLessThanCond(condition);
                generateWhileComparison(whileNode, label);
                break;
            case "Boolean":
                generateBooleanCond(whileNode, label);
                break;
            case "And":
                generateLogicalOp(condition);
                generateWhileComparison(whileNode, label);
                break;
            case "Not":
                generateNotOp(condition);
                generateWhileComparison(whileNode, label);
                break;
            case "Identifier":
                generateIdentifier(condition);
                generateWhileComparison(whileNode, label);
                break;
            case "This":
                generateThis(condition);
                generateWhileComparison(whileNode, label);
                break;
        }

        builder.append(label +"_end:\n");
        currentWhileLabelStack.pop();
    }

    /* Generates the instructions associated with an if statement */
    private void generateIfCondition(SimpleNode node) {
        SimpleNode conditionNode = (SimpleNode) node.jjtGetChild(0);

        switch(conditionNode.toString()) {
            case "Boolean":
                generateBooleanCond(node, currConditionMetLabel);
                break;
            case "Not":
                generateNotOp(conditionNode);
                generateComparison(node);
                break;
            case "And":
                generateLogicalOp(conditionNode);
                generateComparison(node);
                break;
            case "LessThan":
                generateLessThanCond(conditionNode);
                generateComparison(node);
                break;
            case "Identifier":
                loadIdentifier(conditionNode);
                generateComparison(node);
                break;
            case "This":
                generateThis(conditionNode);
                generateComparison(node);
                break;
        }
    }

    /* Generates the conditional instructions associated with a while loop, starting with the condition node */
    private void generateWhileComparison(SimpleNode node, String label) {
        SimpleNode conditionNode = (SimpleNode) node.jjtGetChild(0);

        if (conditionNode.toString().equals("LessThan")) {
            builder.append("\tif_icmpge " + label + "_end\n");
        } else {
            builder.append("\ticonst_1\n");
            builder.append("\tif_icmplt " + label + "_end\n");

            stack += 1; // iconst_1 [+1]
            update_max_stack_size();
        }

        generateConditionBody(node);

        /** Stack */
        stack -= 2; // if_cmp-- [-2]
        update_max_stack_size();

        builder.append("\tgoto " + label + "\n");
    }



    /* Generates the conditional instructions associated with an if or if-else statement, starting with the condition node */
    private void generateComparison(SimpleNode node) {
        SimpleNode conditionNode = (SimpleNode) node.jjtGetChild(0);
        boolean hasElse = false;
        boolean elseNext = false;

        String conditionNotMetLabel = createLabel();

        SimpleNode parentNode = (SimpleNode) node.jjtGetParent();
        if(!parentNode.toString().equals("Else")) {
            currConditionMetLabel = createLabel();
        }

        if(node.getNextSibling() != null && node.getNextSibling().toString().equals("Else")) {
            hasElse = true;
        }

        if(parentNode.getNextSibling() != null && parentNode.getNextSibling().toString().equals("Else")) {
            elseNext = true;
        }

        if (conditionNode.toString().equals("LessThan")) {
            builder.append("\tif_icmpge " + conditionNotMetLabel + "\n");
        } else {
            builder.append("\ticonst_1\n");
            builder.append("\tif_icmplt " + conditionNotMetLabel + "\n");

            /** Stack */
            stack += 1; // iconst_1 [+1]
            update_max_stack_size();
        }

        generateConditionBody(node);

        /** Stack */
        stack -= 2; // if_icmp-- [-2]
        update_max_stack_size();

        if(hasElse || elseNext) {
            builder.append("\tgoto " + currConditionMetLabel + "\n");
        }
        builder.append(conditionNotMetLabel + ":\n");
    }

    /* Generates the common instructions related to a less-than operation */
    private void generateLessThanCond(SimpleNode conditionNode) {
        SimpleNode lhs = (SimpleNode) conditionNode.jjtGetChild(0);
        SimpleNode rhs = (SimpleNode) conditionNode.jjtGetChild(1);
        loadInt(lhs);
        loadInt(rhs);
    }

    /* Generates the common instructions related to condition with booleans */
    private void generateBooleanCond(SimpleNode whileNode, String label){
        SimpleNode conditionNode = (SimpleNode) whileNode.jjtGetChild(0);

        Boolean b = (Boolean) conditionNode.getImage();
        if(b.value.equals("true")) {
            generateConditionBody(whileNode);
            builder.append("\tgoto " + label + "\n");
        }
    }

    /* Replaces i = i + 1 operations with iinc */
    private String generateiinc(String portion) {
        String[] instructions = portion.split("\n");

        if (instructions.length != 4)
            return "";

        String var = instructions[0].substring(instructions[0].length() - 1);
        String var2 = instructions[3].substring(instructions[3].length() - 1);
        String constant = "";
        String op = "";

        if (var.equals(var2)) {
            constant = instructions[1].substring(instructions[1].length() - 1);
            if (instructions[2].trim().equals("isub"))
                op = "-";

            return "\tiinc " + var + " " + op + constant + '\n';
        }
        return "";
    }

    /* Updates the max stack size when necessary */
    private void update_max_stack_size() {
        if (stack < 0) {
            System.err.println("Stack drop below 0!");
        }
        if (stack > maxStackSize) maxStackSize = stack;
    }

    /** OPTIMIZATION -o **/
    /* Generates the conditional instructions associated with a while loop, starting with the condition body if condition is true. */
    private void generateWhileOptimization(SimpleNode whileNode){
        SimpleNode condition = (SimpleNode) whileNode.jjtGetChild(0);
        if(!whileConditionIsTrue(condition)) { /* no optimization should be done */
            generateWhileCondition(whileNode); /* generates regular while */
            return;
        }

        String label = createLabel();
        currentWhileLabelStack.push(label);
        builder.append(label + ":\n");

        generateConditionBody(whileNode);

        switch(condition.toString()){
            case "LessThan":
                generateLessThanCond(condition);
                break;
            case "Boolean":
                Boolean b = (Boolean) condition.getImage();
                if(b.value.equals("true")) {
                    loadLiteralTrue();
                }
                break;
            case "And":
                generateLogicalOp(condition);
                break;
            case "Not":
                generateNotOp(condition);
                break;
            case "Identifier":
                generateIdentifier(condition);
                break;
            case "This":
                generateThis(condition);
                break;
        }

        builder.append("\tif_icmplt " + label + "\n");
        currentWhileLabelStack.pop();
    }

    /* Checks if condition of a while loop is true */
    private boolean whileConditionIsTrue(SimpleNode condition) {
        String returnVal = "";
        switch (condition.toString()){
            case "LessThan":
                returnVal = SemanticAnalyzer.getLessThanValue(condition, symbolTable);
                break;
            case "Boolean":
                returnVal = SemanticAnalyzer.getBooleanValue(condition, symbolTable);
                break;
            case "And":
                returnVal = SemanticAnalyzer.getLogicalOpValue(condition, symbolTable);
                break;
            case "Not":
                returnVal = SemanticAnalyzer.getNotValue(condition, symbolTable);
                break;
        }

        return (returnVal.equals("true"));
    }

}

