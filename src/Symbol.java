import java.util.HashSet;
import java.util.*;
public class Symbol {
    public static final String ESCAPE_CHAR = "_ESC";

    /* Name of the variable */
    private final String name;
    /* Type of the variable */
    private String type;
    /* Value of the variable */
    private String value;
    /* Declares whether the variable is globally initialized or if it is a parameter, which is initialized by default */
    private boolean isInited;
    /* HashSet containing all the scope nodes where the variable is initialized */
    private HashSet<SimpleNode> initializedScopes;
    private int index;
    private int length; /* array length */


    public Symbol(String name, String type, boolean isClassVariable) {
        this.type = type;
        this.isInited = false;
        this.index = -1;
        this.length = -1;
        this.initializedScopes = new HashSet<SimpleNode>();

        /* variable name update */
        if(isClassVariable && hasToBeRenamed(name)){
            name = name.concat(ESCAPE_CHAR);
        }
        this.name = name;
    }

    @Override
    public String toString() {
        String name = "";
        try {
            Integer.parseInt(this.name);
        } catch (Exception e) {
            name = "Name: " + this.name + " / ";
        }
        return name + "Type: " + type;
    }

    /* Returns the whether the variable has been initialized */
    public boolean isInited() {
        return isInited;
    }

    /* Sets the variable as initialized */
    public void setInited() {
        this.isInited = true;
    }

    /* Returns the name of the variable */
    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    /* Returns the type of the variable */
    public String getType() {
        return type;
    }

    /* Returns the length of the variable if it is an array */
    public int getLength() { return length; }

    /* Sets the value of the variable, also setting it as initialized */
    public void setValue(String value) {
        this.value = value;
    }

    /* Sets the type of the variable */
    public void setType(String type) {
        this.type = type;
    }

    /* Sets the index of the variable */
    public void setIndex(int index) { this.index = index; }

    /* Returns current index value of variable*/
    public int getIndex() { return this.index; }

    /* Adds a scope node to the HashSet */
    public void addScopeNode(SimpleNode node) {
        initializedScopes.add(node);
    }

    /* Returns true if scope node begins to the HashSet and false otherwise */
    public boolean containsScopeNode(SimpleNode node) {
        return initializedScopes.contains(node);
    }

    /* Sets the length of an array variable */
    public void setLength(int length){ this.length = length; }

    /* Checks if name of the variable is used in "forbidden" */
    public static boolean hasToBeRenamed(String name){
        Set<String> toAvoid = Set.of("field", "getfield", "putfield", "invokevirtual", "invokestatic");
        return (toAvoid).contains(name);
    }

}