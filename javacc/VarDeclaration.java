public class VarDeclaration extends Image {
    public String type;
    public String name;

    public VarDeclaration(String type, String name) {
        super();
        this.type = type;
        this.name = name;
        SimpleNode.varDecList.add(this);
    }

    public String toString() {
        return "Type: " + type + "   Name: " + name;
    }
}