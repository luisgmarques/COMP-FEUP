import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class SymbolTable {

    /* Stores the class variables */
    private HashMap<String, Symbol> variablesTable;
    /* Stores the class methods */
    private HashMap<MethodForm, Method> methodsTable;
    /* Stores the imports */
    private HashMap<String, ImportSymbolTable> importTables;
    /* Name of the class */
    private String className;
    /* Name of the class that is extended (in case there is one) */
    private String extendsClass;

    public SymbolTable() {
        this.variablesTable = new HashMap<String, Symbol>();
        this.methodsTable = new HashMap<MethodForm, Method>();
        this.importTables = new HashMap<String, ImportSymbolTable>();
    }

    /* Sets the class name */
    public void setClassName(String className) {
        this.className = className;
    }

    /* Sets the name of the class that is extended */
    public void setExtendsClass(String extendsClass) {
        this.extendsClass = extendsClass;
    }

    /* Returns the class name */
    public String getClassName() {
        return className;
    }

    /* Adds a class variable to the HashMap */
    public void addVariable(String name, String type) {
        Symbol symbol = new Symbol(name, type, true);
        variablesTable.put(symbol.getName(), symbol);
    }

    /* Adds a class method to the HashMap */
    public void addMethod(Method method) {
        methodsTable.put(method.getForm(), method);
    }

    /* Adds an import */
    public void addImport(Import importDeclaration){
        String className = importDeclaration.className;
        if(!importTables.containsKey(className)) { // checks if there are any imports with the given class name
            importTables.put(className, new ImportSymbolTable(className));
        }
        ImportSymbolTable importTable = importTables.get(className);
        if(importDeclaration.md != null) { // the import contains a method
            Method method = new Method(importDeclaration.md.name, importDeclaration.md.returnType);
            List<Arg> args = importDeclaration.md.args;
            for(Arg arg : args) {
                method.addArgument(arg.name, arg.type);
            }
            if(importDeclaration.isStatic)
                method.setStatic();
            importTable.addMethod(method);
        } else { // the import is a constructor
            Constructor constructor = new Constructor(importDeclaration.constructorArgs);
            importTable.addConstructor(constructor);
        }
    }

    /* Auxiliary function that prints the symbol table */
    public void print() {
        System.out.println("****** Imports ******");
        for (Map.Entry<String, ImportSymbolTable> entry : importTables.entrySet()) {
            entry.getValue().print();
        }

        System.out.println("****** End of imports ******\n");
        System.out.println("****** Class ******");
        System.out.println(" Name: " + className);
        System.out.println(" Variables:");
        for (Map.Entry<String, Symbol> entry : variablesTable.entrySet()) {
            System.out.println("   " + entry.getValue());
        }
        System.out.println(" Methods:");
        for (Map.Entry<MethodForm, Method> entry : methodsTable.entrySet()) {
            entry.getValue().print();
        }
    }

    /* Returns a variable symbol. Returns null if the variable was not declared in the current scope */
    public Symbol getVarSymbol(String varName, MethodForm methodForm) {
        //methodForm.print();
        Symbol ret;

        if(methodsTable.get(methodForm) != null) { // Local variables have the priority over global ones
            ret = methodsTable.get(methodForm).getVariable(varName);
            if (ret != null) { // Checks if the variable is declared in the method where it is used and returns it if so
                return ret;
            }
            ret = methodsTable.get(methodForm).getArgument(varName);
            if (ret != null) { // Returns the variable if it is an argument of the current method
                return ret;
            }
        }

        if(Symbol.hasToBeRenamed(varName)) varName = varName.concat(Symbol.ESCAPE_CHAR);
        ret = variablesTable.get(varName);

        if(ret != null) { // Checks if the variable is contained in the class variables and returns it if exists
            return ret;
        }

        return null;
    }

    /* Returns a local variable symbol if it exists. Returns null if the variable was not declared */
    public Symbol getLocalVarSymbol(String varName, MethodForm methodForm) {
        //methodForm.print();
        Symbol ret;

        if(methodsTable.get(methodForm) != null) { // Local variables have the priority over global ones
            ret = methodsTable.get(methodForm).getVariable(varName);
            if (ret != null) { // Checks if the variable is declared in the method where it is used and returns it if so
                return ret;
            }
            ret = methodsTable.get(methodForm).getArgument(varName);
            if (ret != null) { // Returns the variable if it is an argument of the current method
                return ret;
            }
        }
        return null;
    }

    /* Returns a class method. Null if it is non-existent */
    public Method getMethod(MethodForm methodForm) {
        return methodsTable.get(methodForm);
    }

    /* Returns an import method. Null if it is non-existent */
    public Method getImportedMethod(MethodForm methodForm, String library) {
        if(importTables.containsKey(library)) // Checks for a method from the given library
            return importTables.get(library).getMethod(methodForm.name, methodForm);
        if(importTables.containsKey(extendsClass)) // Checks for a method from the class that is being extended
            return importTables.get(extendsClass).getMethod(methodForm.name, methodForm);

        return null;
    }

    /* Checks if an object can be created with the type specified as argument */
    public boolean checkIfTypeExists(String name) {
        if (name.equals("int") || name.equals("int[]") || name.equals("boolean")) // primitive type
            return true;
        if (name.equals(className) || name.equals(extendsClass))
            return true;
        ImportSymbolTable ist = importTables.get(name);
        return ist != null && ist.isInstantiatable();
    }

    /* Checks if a given class exists within the imports */
    public boolean containsImportedClass(String className) {
        return importTables.containsKey(className);
    }

    /* Returns the class that is extended */
    public String getExtendsClass() {
        return extendsClass;
    }


    /* Returns the class variable that has the name passed as argument, null otherwise */
    public Symbol getGlobalVarSymbol(String varName){
        if(Symbol.hasToBeRenamed(varName)) varName = varName.concat(Symbol.ESCAPE_CHAR);

        Symbol ret = variablesTable.get(varName);
        if(ret != null) {
            return ret;
        }
        return null;
    }

    /* Return methods hash table*/
    public HashMap<MethodForm, Method> getMethodsTable() {
        return methodsTable;
    }

}

/* Class that stores the method's name and arguments' types */
class MethodForm {
    public String name;
    public List<String> argumentTypes;

    public MethodForm(String name) {
        this.argumentTypes = new ArrayList<>();
        this.name = name;
    }

    public void addType(String argType) {
        argumentTypes.add(argType);
    }

    public void print() {
        System.out.println("Method form:");
        System.out.println("Method name: " + name);
        for(String arg : argumentTypes) {
            System.out.println("   Arg type: " + arg);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MethodForm)) {
            return false;
        }
        MethodForm mf = (MethodForm) o;
        return argumentTypes.equals(mf.argumentTypes) && name.equals(mf.name);
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public String toString() {
        return name + argumentTypes.toString();
    }


}