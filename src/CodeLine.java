import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class CodeLine {

    private int index;
    private List<CodeLine> predecessors;
    private List<CodeLine> successors;
    private BitSet uses;
    private BitSet defs;
    private BitSet in;
    private BitSet out;

    public CodeLine(int index) {
        this.index = index;
        this.predecessors = new ArrayList<CodeLine>();
        this.successors = new ArrayList<CodeLine>();
        this.uses = new BitSet();
        this.defs = new BitSet();
        this.in = new BitSet();
        this.out = new BitSet();
    }

    /**
     * Getters and setters
     */

    public int getIndex() {
        return index;
    }

    public void setDef(int index) {
        defs.set(index);
    }

    public void setUse(int index) {
        uses.set(index);
    }

    public BitSet getUses() {
        return uses;
    }

    public BitSet getDefs() {
        return defs;
    }

    public void addPredecessor(CodeLine codeLine) {
        predecessors.add(codeLine);
    }

    public void addSuccessor(CodeLine codeLine) {
        successors.add(codeLine);
    }

    public List<CodeLine> getPredecessors() {
        return this.predecessors;
    }

    public List<CodeLine> getSuccessors() {
        return this.successors;
    }

    public BitSet getIn() {
        return this.in;
    }

    public BitSet getOut() {
        return this.out;
    }

    public void printPredsAndSucc() {
        System.out.println("Code line: " + getIndex());
        System.out.println("Predecessors: " + getPredecessors());
        System.out.println("Successors: " + getSuccessors());
        System.out.println();
    }

    public void printInAndOut() {
        System.out.println("Live-in: " + getIn());
        System.out.println("Live-out: " + getOut());
        System.out.println();
    }

    @Override
    public String toString() {
        return getIndex()+"";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CodeLine)) {
            return false;
        }
        CodeLine codeLine = (CodeLine) obj;

        return codeLine.getIndex() == index;
    }
}
