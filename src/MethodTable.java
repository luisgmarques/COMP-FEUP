import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MethodTable {

    private String methodName;
    private HashMap<String, Symbol> argumentsTable;
    private HashMap<String, Symbol> variablesTable;
    private String returnType;

    public MethodTable(String methodName, String returnType) {
        this.methodName = methodName;
        this.returnType = returnType;
        this.argumentsTable = new HashMap<String, Symbol>();
        this.variablesTable = new HashMap<String, Symbol>();
    }

    public boolean addVariable(String name, String type) {
        if (this.variablesTable.containsKey(name)) 
            return false;

        Symbol s = new Symbol(name, type, false);
        this.variablesTable.put(name, s);
        return true;
    }

    public Symbol getVariable(String name) {
        return variablesTable.get(name);
    }

    public Symbol getArgument(String name) {
        return argumentsTable.get(name);
    }

    public boolean addArgument(String name, String type) {
        if (this.argumentsTable.containsKey(name)) 
            return false;

        Symbol s = new Symbol(name, type, false);
        this.argumentsTable.put(name, s);
        return true;
    }

	public String getName() {
		return methodName;
    }
    
    public void print() {
        System.out.println("------ Method ------");
        System.out.println(" Name: " + methodName);
        System.out.println(" Return Type: " + returnType);
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