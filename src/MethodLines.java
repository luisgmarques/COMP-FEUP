import java.util.*;

public class MethodLines {
    private List<CodeLine> codeLines;
    private HashMap<Integer, GraphNode> graph;


    public MethodLines() {
        this.codeLines = new ArrayList<>();
        this.graph = new HashMap<>();
    }

    public void addCodeLine(CodeLine line) {
        codeLines.add(line);
    }

    /* Performs the liveness analysis for this specific method */
    public void livenessAnalysis() {
        boolean hasNextIteration = false;
        do {
            hasNextIteration = false;
            for (int i = codeLines.size() - 1; i >= 0; i--) {
                CodeLine currCodeLine = codeLines.get(i);

                BitSet outPrime = new BitSet();
                if (!hasNextIteration) {
                    outPrime = currCodeLine.getOut();
                }
                assignOut(currCodeLine);
                
                if (!hasNextIteration && !outPrime.equals(currCodeLine.getOut())) {
                    hasNextIteration = true;
                }

                BitSet inPrime = new BitSet();
                if (!hasNextIteration) {
                    inPrime = currCodeLine.getIn();
                }
                
                assignIn(currCodeLine);

                if (!hasNextIteration && !inPrime.equals(currCodeLine.getIn())) {
                    hasNextIteration = true;
                }
            }
        } while(hasNextIteration);
    }

    /* Auxiliary function that assigns the variables that are live when entering a CodeLine */
    private void assignIn(CodeLine codeLine) {
        BitSet in = codeLine.getIn();
        in.clear();
        in.or(codeLine.getOut());
        in.andNot(codeLine.getDefs());
        in.or(codeLine.getUses());
    }

    /* Auxiliary function that assigns the variables that are live when leaving a CodeLine */
    private void assignOut(CodeLine codeLine) {
        BitSet out = codeLine.getOut();
        out.clear();
        for (CodeLine successor : codeLine.getSuccessors()) {
            out.or(successor.getIn());
        }
    }

    public void addNodeToGraph(Symbol variable, boolean isArgument) {
        GraphNode node = new GraphNode(variable.getIndex(), variable.getName());
        if (isArgument)
            node.setRegister(variable.getIndex());
        graph.put(variable.getIndex(), node);
    }

    /* Builds the Register-Interference Graph */
    public void buildGraph() {
        List<Integer> setBits;
        for (CodeLine codeLine : codeLines) {
            setBits = new ArrayList<>();
            BitSet in = codeLine.getIn();
            for (int i = in.nextSetBit(0); i != -1; i = in.nextSetBit(i + 1)) {
                setBits.add(i);
            }
            for (int i = 0; i < setBits.size(); i++) {
                for (int j = 0; j < setBits.size(); j++) {
                    if (i == j) continue;
                    graph.get(setBits.get(i)).addNode(setBits.get(j));
                }
            }
        }
    }

    /* Assigns a color to each node of the graph */
    public void colorGraph() {
        int k = OptimizationR.k;
        HashMap<Integer, GraphNode> clonedGraph = cloneGraph();
        Stack<GraphNode> stack = new Stack<>();
        boolean exitLoop = false;
        while (!exitLoop) {
            exitLoop = true;
            for (GraphNode node : clonedGraph.values()) {
                HashSet<Integer> linkedNodes = node.getLinkedNodes();
                if (linkedNodes.size() < k) {
                    exitLoop = false;
                    for (Integer nodeId : linkedNodes) {
                        clonedGraph.get(nodeId).removeNode(node.getID());
                    }
                    stack.push(graph.get(node.getID()));
                    clonedGraph.remove(node.getID());
                    break;
                }
            }
        }
        if (!clonedGraph.isEmpty()) {
            int numRegs = 0;
            for (int id : clonedGraph.keySet()) {
                GraphNode node = graph.get(id);
                if (node.getRegister() != -1) {
                    if (node.getRegister() > numRegs)
                        numRegs = node.getRegister();
                    continue;
                }
                HashSet<Integer> usedRegisters = getLinkedNodesRegisters(node);
                for (int i = 1; i <= graph.size(); i++) {
                    if (!usedRegisters.contains(i)) {
                        node.setRegister(i);
                        if (i > numRegs)
                            numRegs = i;
                        break;
                    }
                }
            }
            if (numRegs > OptimizationR.necessaryRegs)
                OptimizationR.necessaryRegs = numRegs;
        }

        while (!stack.isEmpty()) {
            GraphNode node = stack.pop();
            if (node.getRegister() != -1)
                continue;
            HashSet<Integer> usedRegisters = getLinkedNodesRegisters(node);
            for (int i = 1; i <= k; i++) {
                if (!usedRegisters.contains(i)) {
                    node.setRegister(i);
                    break;
                }
            }
        }
    }

    /* Returns a HashSet with the registers of the nodes that are linked to given node in the IG */
    public HashSet<Integer> getLinkedNodesRegisters(GraphNode node) {
        HashSet<Integer> ret = new HashSet<>();
        for (Integer nodeID : node.getLinkedNodes()) {
            ret.add(graph.get(nodeID).getRegister());
        }
        return ret;
    }

    public void print() {
        for (CodeLine  cl : codeLines) {
            cl.printPredsAndSucc();
            cl.printInAndOut();
        }
        System.out.println("Graph:");
        for(GraphNode graphNode : graph.values()) {
            System.out.println(graphNode);
        }
    }

    /* Returns a copy of the IG */
    public HashMap<Integer, GraphNode> cloneGraph() {
        HashMap<Integer, GraphNode> clonedGraph = new HashMap<>();
        for(Map.Entry<Integer, GraphNode> entry : graph.entrySet()) {
            clonedGraph.put(entry.getKey(), entry.getValue().getCopy());
        }
        return clonedGraph;
    }

    public HashMap<Integer, GraphNode> getGraph() {
        return this.graph;
    }
}
