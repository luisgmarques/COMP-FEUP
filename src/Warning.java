public class Warning {
    public Warning(String message, Token t, boolean displayColumn) {
        System.out.println("WARNING: " + message + "\nOn line " + t.beginLine + (displayColumn ? " column " + t.beginColumn : ""));
    }

    public Warning(String message) {
        System.out.println(message);
    }
}
