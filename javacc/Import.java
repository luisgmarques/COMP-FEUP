import java.util.ArrayList;
import java.util.List;

public class Import extends Image {

    public String className;
    public MethodDeclaration md;
    public boolean isStatic;
    public List<String> constructorArgs;

    public Import() {
        className = "";
        isStatic = false;
        constructorArgs = new ArrayList<String>();
    }

    public void setClassName(String className){
        this.className = className;
    }

    public void setStatic(){
        isStatic = true;
    }

    public void addArgsToConstructor(String arg) {
        this.constructorArgs.add(arg);
    }

    public void addMethod(String methodName) {
        md = new MethodDeclaration(methodName, "void");
    }

    public void addArgsToMethod(String type){
        if (type.equals("void")) return;
        Arg arg = new Arg(type, String.valueOf(md.args.size()));
        md.args.add(arg);
    }

    public void setReturnType(String returnType){
        md.returnType = returnType;
    }

    public String toString() {
        String statement = new String();

        if(isStatic)
            statement += "static ";

        statement += this.className;

        if(md != null) {
            statement += " " + md.name + "( ";
            for(Arg arg: md.args){
                statement += arg.type + " ";
            }
            statement += ") " + md.returnType;
        } else {
            statement += "( ";
            for (String arg: constructorArgs) {
                statement += arg + " ";
            }
            statement += ")";
        }

        return statement;
    }
}