import java.util.*;


public class Method {
    /* Name of the method */
    private String name;
    /* Contains information about the method's name and argument types */
    private MethodForm methodForm;
    /* HashMap containing the method arguments */
    private HashMap<String, Symbol> argumentsTable;
    /* HashMap containing the variables defined inside the method */
    private HashMap<String, Symbol> variablesTable;
    /* Return type of the method */
    private String returnType;
    /* Whether the method is static */
    private boolean isStatic;
    /* Current index counter */
    private int indexCounter;

    public Method(String methodName, String returnType) {
        this.name = methodName;
        this.returnType = returnType;
        this.isStatic = false;
        this.methodForm = new MethodForm(methodName);
        this.argumentsTable = new HashMap<String, Symbol>();
        this.variablesTable = new HashMap<String, Symbol>();

        this.indexCounter = 0;
    }

    /* Adds a method variable and returns true if it hasn't been defined before, false otherwise */
    public boolean addVariable(String name, String type) {
        if (this.variablesTable.containsKey(name)) // checks if the variable already exists in the method's scope
            return false;

        Symbol s = new Symbol(name, type, false);
        s.setIndex(++indexCounter);

        this.variablesTable.put(name, s);
        return true;
    }

    /* Returns the variable with the name passed as argument */
    public Symbol getVariable(String name) {
        return variablesTable.get(name);
    }

    /* Returns the method's argument with the specified name */
    public Symbol getArgument(String name) {
        return argumentsTable.get(name);
    }

    /* Returns the list of variables defined within the method scope */
    public Collection<Symbol> getVariablesList() {
        return variablesTable.values();
    }

    /* Returns the method's return type */
    public String getReturnType() {
        return returnType;
    }

    /* Returns the argument's table */
    public HashMap<String, Symbol> getArgumentsTable() {
        return argumentsTable;
    }

    /* Adds an argument to the arguments table  */
    public boolean addArgument(String name, String type) {
        if (this.argumentsTable.containsKey(name)) // returns false if the argument was already existent
            return false;
        Symbol s = new Symbol(name, type, false);

        s.setInited();
        s.setIndex(++indexCounter);

        /* useful for optimization -o while */
        switch (type){
            case "int":
            case "int[]":
                s.setValue("0");
                break;
            case "boolean":
                s.setValue("true");
                break;
        }

        this.argumentsTable.put(name, s);
        this.methodForm.addType(type);
        return true;
    }

    /* Returns the name of the method */
	public String getName() {
		return name;
    }

    /* Sets the method as static */
    public void setStatic() {
        this.isStatic = true;
    }

    /* Returns whether the method is static */
    public boolean isStatic() {
        return isStatic;
    }

    /* Returns the information about the method's name and argument types */
    public MethodForm getForm() {
        return this.methodForm;
    }

    /* Auxiliary function to print the method */
    public void print() {
        System.out.println("------ Method ------");
        System.out.println(" Name: " + name);
        System.out.println(" Return Type: " + returnType);
        System.out.println(" Static: " + isStatic);
        System.out.println(" Arguments:");
        for (Map.Entry<String, Symbol> entry : argumentsTable.entrySet()) {
            System.out.println("   " + entry.getValue());
        }
        System.out.println(" Variables:");
        for (Map.Entry<String, Symbol> entry : variablesTable.entrySet()) {
            System.out.println("   " + entry.getValue());
        }
    }

}