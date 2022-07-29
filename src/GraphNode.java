import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class GraphNode {
    private int id;
    private String name;
    private int register; // color of the node
    private HashSet<Integer> linkedNodes;

    public GraphNode(int id, String name) {
        this.id = id;
        this.name = name;
        this.register = -1;
        this.linkedNodes = new HashSet<>();
    }

    public int getID() {
        return this.id;
    }

    public void addNode(Integer nodeId) {
        linkedNodes.add(nodeId);
    }

    public void removeNode(Integer nodeId) {
        linkedNodes.remove(nodeId);
    }

    public HashSet<Integer> getLinkedNodes() {
        return linkedNodes;
    }

    public boolean intersectsNode(Integer nodeId) {
        return linkedNodes.contains(nodeId);
    }

    @Override
    public String toString() {
        String str = "ID: " + id + "\nName: " + name + "\nRegister: " + register + "\nLinked nodes:\n";
        for(Integer i : linkedNodes) {
            str += i + " / ";
        }
        return str + "\n";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GraphNode)) {
            return false;
        }
        GraphNode node = (GraphNode) obj;
        return node.id == id;
    }

    public void setRegister(int register) {
        this.register = register;
    }

    public int getRegister() {
        return this.register;
    }

    /* Returns a copy of the current GraphNode */
    public GraphNode getCopy() {
        GraphNode copy = new GraphNode(id, name);
        copy.register = register;
        for(Integer linkedNode : linkedNodes) {
            copy.addNode(linkedNode);
        }
        return copy;
    }

    public String getName() {
        return this.name;
    }
}
