public class SemanticError {
    private String message;
    public int line;

    public SemanticError(String message, Token t, boolean displayColumn) {
        this.message = message + "\nError found on line " + t.beginLine + (displayColumn ? " column " + t.beginColumn : "");
        this.line = t.beginLine;
    }

    public SemanticError(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
