

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImportSymbolTable {
    /* HashMap containing the lists of methods that were imported from the respective class
    *  Methods in the same list are overloads */
    private HashMap<String, List<Method>> methodsTable;
    /* List of the constructors that were imported (if any) */
    private List<Constructor> constructors;
    /* name of the imported class */
    private String className;

    public ImportSymbolTable(String className) {
        this.className = className;
        this.methodsTable = new HashMap<String, List<Method>>();
        this.constructors = new ArrayList<Constructor>();
    }

    /* Adds a method to the methods table */
    public void addMethod(Method method) {
        String methodName = method.getName();
        if(methodsTable.containsKey(methodName)) { // Adds an overload of the method
            methodsTable.get(methodName).add(method);
        } else { // Adds a new method
            methodsTable.put(method.getName(), new ArrayList<Method>() {{ add(method);}});
        }
    }

    /* Auxiliary function that prints the import symbol table */
    public void print() {
        System.out.println("   *** Library ***");
        System.out.println(" Name: " + className);
        System.out.println(" Instantiatable: " + isInstantiatable());
        System.out.println(" Constructors:");
        for(Constructor constructor : constructors) {
            constructor.print();
        }

        System.out.println(" Methods:");
        for (Map.Entry<String, List<Method>> entry : methodsTable.entrySet()) {
            for(Method method : entry.getValue()) {
                method.print();
            }
        }
    }

    /* Returns the method that matches the arguments, null if there aren't any */
    public Method getMethod(String name, MethodForm form) {
        if(methodsTable.get(name) == null)
            return null;
        for(Method method : methodsTable.get(name)) {
            if(method.getForm().equals(form))
                return method;
        }
        return null;
    }

    /* Returns whether the imported class can be instantiated */
    public boolean isInstantiatable() {
        return constructors.size() > 0;
    }

    /* Adds a constructor to the class */
    public void addConstructor(Constructor constructor) {
        constructors.add(constructor);
    }
}

/* Class that holds the list of arguments of a constructor for the imported classes */
class Constructor {
    public List<String> arguments;

    public Constructor(List<String> arguments) {
        this.arguments = arguments;
    }

    public void print() {
        for(String arg : arguments) {
            System.out.println("Arg type: " + arg);
        }
    }
}