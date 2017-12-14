@SuppressWarnings("serial")
public class ParseException extends Exception {
    public ParseException(String message) {
        super(message);
    }

    public ParseException(String expected, String actual) {
        super("expected " + expected + ", got '" + actual + "'");
    }
}
