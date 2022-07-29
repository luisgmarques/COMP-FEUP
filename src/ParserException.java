public class ParserException extends Throwable {
    public ParserException(String message, Token t, boolean displayColumn) {
        super(message + "\nError found on line " + t.beginLine + (displayColumn ? " column " + t.beginColumn : ""));
    }

    public ParserException(String message) {
        super(message);
    }
}
