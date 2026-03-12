package madlang;

// Custom exception class to store line and column information for runtime errors
public class MadLangRuntimeError extends RuntimeException {
    public final int line;
    public final int col;

    public MadLangRuntimeError(String message, int line, int col) {
        super(message);
        this.line = line;
        this.col = col;
    }
}