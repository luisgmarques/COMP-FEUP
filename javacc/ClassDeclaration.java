public class ClassDeclaration extends Image {
    public String name;
    public String extendsClass;

    public ClassDeclaration(String name) {
        this.name = name;
        this.extendsClass = "";
        SimpleNode.classDec = this;
    }

    public ClassDeclaration(String name, String extendsClass) {
        super();
        this.name = name;
        this.extendsClass = extendsClass;
    }

    public String toString() {
        if (extendsClass.equals("")) return "Name: " + name;
        else return "Name: " + name + "   Extends: " + extendsClass;
        
    }
}