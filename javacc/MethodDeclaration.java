import java.util.ArrayList;
import java.util.List;

public class MethodDeclaration extends Image {
    public String name;
    public String returnType;
    public List<Arg> args;

    public MethodDeclaration(String name, String returnType){
        this.name = name;
        this.returnType = returnType;
        args = new ArrayList<Arg>();
    }

    public String toString(){
        String buildArgs = "";

        for(Arg a : args){
            buildArgs += a.type + " " + a.name + ",  ";
        }

        if(buildArgs.length() > 0)
            buildArgs = buildArgs.substring(0, buildArgs.length() - 3);

        return "Return Type: " + returnType + "   Name: " + this.name + "   Args: " + buildArgs;
    }

    public void addArg(Arg argument) {
        SimpleNode.varDecList.add(new VarDeclaration(argument.type, argument.name));
        args.add(argument);
    }

}

class Arg {
    public String type;
    public String name;

    public Arg(String type, String name) {
        this.type = type;
        this.name = name;
    }
}
