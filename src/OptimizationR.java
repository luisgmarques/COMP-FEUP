import java.util.HashMap;
import java.util.Map;

public class OptimizationR {
    private SimpleNode root;
    private SymbolTable symbolTable;
    private HashMap<MethodForm, MethodLines> methodLinesMap;
    private int currCodeLine;
    private static int i = -1;
    public static int k;
    public static int necessaryRegs;
    public OptimizationR(SimpleNode root, SymbolTable symbolTable, int n) {
        this.root = root;
        this.symbolTable = symbolTable;
        this.methodLinesMap = new HashMap<MethodForm, MethodLines>();
        this.currCodeLine = 0;
        this.necessaryRegs = 0;
        k = n;
        divideIntoCodeLines(root);
        handleCodeLines(root);
        livenessAnalysis();
        buildGraph();
        colorGraph();
        assignRegisters();
        checkNecessaryRegs();
        //print();
    }

    /* Iterates through the nodes of the tree, assigning each one to a CodeLine */
    private void divideIntoCodeLines(SimpleNode node) {
        addCodeLine(node);

        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            SimpleNode n = node.getChild(i);
            if (n != null) {
                divideIntoCodeLines(n);
            }
        }
    }

    /* Gets the successors, predecessors, uses and defs for each CodeLine */
    private void handleCodeLines(SimpleNode node) {
        if (node.codeLine != null) {
            if (isCodeLineInitiator(node))
                checkUsesAndDefs(node, node.codeLine);
            checkPredAndSucc(node);
        }

        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            SimpleNode n = (SimpleNode) node.getChild(i);
            if (n != null) {
                handleCodeLines(n);
            }
        }
    }

    /**
     * Auxiliary functions for the handle of CodeLines
     */

    private void checkUsesAndDefs(SimpleNode node, CodeLine codeLine) {
        if (node.codeLine.getIndex() != codeLine.getIndex()) return;
        int i = 0;
        if (node.jjtGetNumChildren() > 0 && node.toString().equals("Assign")) {
            SimpleNode child = node.getChild(0);
            int index = getVariableIndex(child);
            if (index != -1)
                codeLine.setDef(index);
            if (child.jjtGetNumChildren() > 0)
                checkUsesAndDefs(child.getChild(0), codeLine);
            i = 1;
        } else if (node.toString().equals("Identifier")) {
            int index = getVariableIndex(node);
            if (index != -1)
                codeLine.setUse(index);
        }

        for (; i < node.jjtGetNumChildren(); i++) {
            checkUsesAndDefs(node.getChild(i), codeLine);
        }
    }

    private void checkPredAndSucc(SimpleNode node) {
        CodeLine codeLine = node.codeLine;
        if ((!codeLine.getSuccessors().isEmpty() || !isCodeLineInitiator(node)) && !node.toString().equals("If")) return;
        switch(node.toString()) {
            case "While":
                checkPredAndSuccWhile(node);
                break;
            case "If":
                checkPredAndSuccIf(node);
                break;
            case "Break":
                handleBreak(node);
                break;
            case "Continue":
                handleContinue(node);
                break;
            default:
                SimpleNode nextSibling = node.getNextSibling();
                if (nextSibling != null) {
                    codeLine.addSuccessor(nextSibling.codeLine);
                    nextSibling.codeLine.addPredecessor(codeLine);
                }
        }
    }

    private void handleContinue(SimpleNode node) {
        SimpleNode whileNode = node.getPredecessor("While");
        whileNode.codeLine.addPredecessor(node.codeLine);
        node.codeLine.addSuccessor(whileNode.codeLine);
    }

    private void handleBreak(SimpleNode node) {
        SimpleNode whileNode = node.getPredecessor("While");
        SimpleNode nextSibling = whileNode.getNextSibling();
        if (nextSibling != null) {
            node.codeLine.addSuccessor(nextSibling.codeLine);
            nextSibling.codeLine.addPredecessor(node.codeLine);
        }
    }

    private void checkPredAndSuccWhile(SimpleNode node) {
        CodeLine codeLine = node.codeLine;
        SimpleNode firstWhileLine = node.getChild(1);
        SimpleNode lastWhileLine = node.getChild(node.jjtGetNumChildren() - 1);
        SimpleNode nextSibling = node.getNextSibling();
        if (node.jjtGetNumChildren() > 1) {
            codeLine.addSuccessor(firstWhileLine.codeLine);
            firstWhileLine.codeLine.addPredecessor(codeLine);
        }
        if (nextSibling != null) {
            codeLine.addSuccessor(nextSibling.codeLine);
            nextSibling.codeLine.addPredecessor(codeLine);
        }
        if (!lastWhileLine.toString().equals("Break") && !lastWhileLine.toString().equals("Continue")) {
            codeLine.addPredecessor(lastWhileLine.codeLine);
            lastWhileLine.codeLine.addSuccessor(codeLine);
        }
    }

    private void checkPredAndSuccIf(SimpleNode node) {
        CodeLine codeLine = node.codeLine;
        SimpleNode firstIfChild = node.getChild(1);
        if (firstIfChild != null) {
            codeLine.addSuccessor(firstIfChild.codeLine);
            firstIfChild.codeLine.addPredecessor(codeLine);
        }
        if (node.getParent().toString().equals("Else")) {
            handleElseIf(node);
        } else {
            handleNormalIf(node);
        }
    }

    private void handleNormalIf(SimpleNode node) {
        CodeLine codeLine = node.codeLine;
        SimpleNode lastIfChild = node.getChild(node.jjtGetNumChildren()-1);
        SimpleNode nextSibling = node.getNextSibling();
        if (nextSibling != null) {
            if (nextSibling.toString().equals("Else")) {
                // if-else block

                SimpleNode elseSibling = nextSibling.getNextSiblingNotElse(false);
                if (elseSibling != null) {
                    lastIfChild.codeLine.addSuccessor(elseSibling.codeLine);
                    elseSibling.codeLine.addPredecessor(lastIfChild.codeLine);
                }

                if (nextSibling.jjtGetNumChildren() > 0) {
                    SimpleNode firstElseChild = nextSibling.getChild(0);
                    SimpleNode lastElseChild = nextSibling.getChild(nextSibling.jjtGetNumChildren()-1);
                    codeLine.addSuccessor(firstElseChild.codeLine);
                    firstElseChild.codeLine.addPredecessor(codeLine);
                    SimpleNode siblingNotElse = node.getNextSiblingNotElse(false);
                    if (firstElseChild.equals(lastElseChild) && firstElseChild.toString().equals("If"))
                        siblingNotElse = nextSibling.getNextSibling().getChild(0);
                    if (siblingNotElse != null) {
                        lastElseChild.codeLine.addSuccessor(siblingNotElse.codeLine);
                        siblingNotElse.codeLine.addPredecessor(lastElseChild.codeLine);
                    }
                } else {
                    nextSibling = nextSibling.getNextSibling();
                    if (nextSibling != null) {
                        codeLine.addSuccessor(nextSibling.codeLine);
                        nextSibling.codeLine.addPredecessor(codeLine);
                    }
                }

            } else { // if block
                codeLine.addSuccessor(nextSibling.codeLine);
                nextSibling.codeLine.addPredecessor(codeLine);
                lastIfChild.codeLine.addSuccessor(nextSibling.codeLine);
                nextSibling.codeLine.addPredecessor(lastIfChild.codeLine);
            }
        }
    }

    private void handleElseIf(SimpleNode node) {
        CodeLine codeLine = node.codeLine;
        if (node.jjtGetNumChildren() > 0) {
            SimpleNode lastIfChild = node.getChild(node.jjtGetNumChildren() - 1);
            SimpleNode nextSiblingNotElse = node.getParent().getNextSiblingNotElse(false);
            if (nextSiblingNotElse != null) {
                lastIfChild.codeLine.addSuccessor(nextSiblingNotElse.codeLine);
                nextSiblingNotElse.codeLine.addPredecessor(lastIfChild.codeLine);
                if (!codeLine.getSuccessors().contains(nextSiblingNotElse.codeLine) &&
                    !node.getParent().getNextSibling().toString().equals("Else")) {
                    codeLine.addSuccessor(nextSiblingNotElse.codeLine);
                    nextSiblingNotElse.codeLine.addPredecessor(codeLine);
                }
            }
            SimpleNode nextSibling = node.getParent().getNextSibling();
            if (nextSibling != null && nextSibling.toString().equals("Else")) {
                CodeLine nextElseCodeLine = nextSibling.getChild(0).codeLine;
                if (!codeLine.getSuccessors().contains(nextElseCodeLine)) {
                    codeLine.addSuccessor(nextElseCodeLine);
                    nextElseCodeLine.addPredecessor(codeLine);
                }
                if (!nextSibling.getChild(0).toString().equals("If")) {
                    SimpleNode lastElseChild = nextSibling.getChild(nextSibling.jjtGetNumChildren()-1);
                    if (nextSibling.jjtGetNumChildren() > 0) {
                        lastElseChild.codeLine.addSuccessor(nextSiblingNotElse.codeLine);
                        nextSiblingNotElse.codeLine.addPredecessor(lastElseChild.codeLine);
                    }
                }
            }
        }

    }

    /**
     * End of auxiliary functions for the handle of CodeLines
     */

    /* Checks whether a node is a part of a code line */
    private boolean representsCodeLine(SimpleNode node) {
        if (node.getParent() == null || (!node.toString().equals("Method") && !node.toString().equals("Main")
                && node.getParent().codeLine == null)) {
            return false;
        } else {
            return true;
        }
    }

    /* Auxiliary function to assign a CodeLine to a node */
    private void addCodeLine(SimpleNode node) {
        if (!representsCodeLine(node)) {
            node.codeLine = null;
        } else {
            if (node.toString().equals("Method") || node.toString().equals("Main")) {
                this.currCodeLine = 0;
                node.codeLine = new CodeLine(-1);
                createNewMethodLine(node);
            } else {
                node.methodLines = node.getParent().methodLines;
                if (isCodeLineInitiator(node)) {
                    node.codeLine = new CodeLine(this.currCodeLine);
                    this.currCodeLine++;
                    assignCodeLineToMethod(node);
                } else {
                    node.codeLine = node.getParent().codeLine;
                }
            }
        }
    }

    /* Adds the CodeLine to the IR */
    private void assignCodeLineToMethod(SimpleNode node) {
        MethodForm methodForm = SemanticAnalyzer.getCurrentMethod(node);
        MethodLines ml = methodLinesMap.get(methodForm);
        ml.addCodeLine(node.codeLine);
    }

    /* If the node is a method, adds it to the HashMap (IR) */
    private void createNewMethodLine(SimpleNode node) {
        MethodLines m = new MethodLines();
        if(node.jjtGetNumChildren() > 0) {
            MethodForm methodForm = SemanticAnalyzer.getCurrentMethod(node.getChild(0));
            methodLinesMap.put(methodForm, m);
        }
    }

    /* Returns the index associated with the local variable of the method */
    private int getVariableIndex(SimpleNode node) {
        MethodForm methodForm = SemanticAnalyzer.getCurrentMethod(node);
        Identifier id = (Identifier) (node.getImage());
        Symbol symbol;
        if ((symbol = symbolTable.getLocalVarSymbol(id.toString(), methodForm)) == null) return -1;
        return symbol.getIndex();
    }

    /* Checks if a node matches the start of a code line */
    private boolean isCodeLineInitiator(SimpleNode node) {
        SimpleNode parent = node.getParent();
        String parentString = parent.toString();
        if (node.toString().equals("Else")) return false;
        if (parentString.equals("Method") || parentString.equals("Main")) {
            return true;
        }
        if ((parentString.equals("If") || parentString.equals("While")) && !parent.getChild(0).equals(node)) {
            return true;
        }
        if (node.toString().equals("If") || node.toString().equals("While") || parentString.equals("Else")) {
            return true;
        }
        return false;
    }

    /* Performs the liveness analysis for each method */
    private void livenessAnalysis() {
        for (MethodLines ml : methodLinesMap.values()) {
            ml.livenessAnalysis();
        }
    }

    /* Builds the Register-Interference Graph for each method */
    private void buildGraph() {
        for (Map.Entry<MethodForm, MethodLines> entry : methodLinesMap.entrySet()) {
            MethodLines ml = entry.getValue();
            Method method = symbolTable.getMethod(entry.getKey());
            for (Symbol s : method.getArgumentsTable().values()) {
                ml.addNodeToGraph(s, true);
            }
            for (Symbol s : method.getVariablesList()) {
                ml.addNodeToGraph(s, false);
            }
            checkDifferentTypes(entry.getKey(), ml);
            ml.buildGraph();
        }
    }

    /* Checks whether there are array type variables and links them to variables with a different type in the graph */
    private void checkDifferentTypes(MethodForm mf, MethodLines ml) {
        for (GraphNode node : ml.getGraph().values()) {
            Symbol variable = symbolTable.getLocalVarSymbol(node.getName(), mf);
            if (variable.getType().equals("int[]")) {
                for (GraphNode node2 : ml.getGraph().values()) {
                    Symbol variable2 = symbolTable.getLocalVarSymbol(node2.getName(), mf);
                    if (node.getID() == node2.getID() || variable.getType().equals(variable2.getType())) continue;
                    node.addNode(node2.getID());
                    node2.addNode(node.getID());
                }
            }
        }
    }

    /* Assigns a color to each node of the graph for each method */
    private void colorGraph() {
        for (MethodLines ml : methodLinesMap.values()) {
            ml.colorGraph();
        }
    }

    /* Assigns a register to each variable of the symbol table according to the way the graph was colored */
    private void assignRegisters() {
        for (Map.Entry<MethodForm, MethodLines> entry : methodLinesMap.entrySet()) {
            MethodForm methodForm = entry.getKey();
            MethodLines methodLines = entry.getValue();
            for (GraphNode node : methodLines.getGraph().values()) {
                Symbol variable = symbolTable.getLocalVarSymbol(node.getName(), methodForm);
                variable.setIndex(node.getRegister());
            }
        }
    }

    /* Checks if the n value for the optimization R matches the necessary number of registers.
        If not, exits and displays a message informing about the minimum number necessary.
     */
    private void checkNecessaryRegs() {
        if (necessaryRegs > k) {
            System.err.println("Not enough registers to perform optimization! You need a minimum of " + necessaryRegs + "!");
            System.exit(0);
        }
    }

    public void print() {
        for (Map.Entry<MethodForm, MethodLines> entry : methodLinesMap.entrySet()) {
            System.out.println("Method: " + entry.getKey().name);
            entry.getValue().print();
            System.out.println();
        }
    }

}
