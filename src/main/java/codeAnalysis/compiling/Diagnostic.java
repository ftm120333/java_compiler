package codeAnalysis.compiling;

import codeAnalysis.text.TextSpan;

public class Diagnostic {

    TextSpan span;
    String message;

    public Diagnostic(TextSpan span, String message) {
        this.span = span;
        this.message = message;
    }

    public TextSpan getSpan() {
        return span;
    }

    public String getMessage() {
        return message;
    }

    public Object get_diagnostics() {
        return this;
    }

    @Override
    public String toString() {
        return message; // Return the diagnostic message
    }

}
