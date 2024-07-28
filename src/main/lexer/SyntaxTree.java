package main.lexer;

import java.util.List;

public class SyntaxTree {
    private final List<String> diagnostics;
    private final ExpressionSyntax Root;
    private final SyntaxToken EndOfFileToken;

    public SyntaxTree(List<String> diagnostics, ExpressionSyntax root, SyntaxToken endOfFileToken)
    {
        this.diagnostics = diagnostics;
        this.Root = root;
        this.EndOfFileToken = endOfFileToken;
    }

    public Iterable<String> getDiagnostics() {
        return diagnostics;
    }

    public ExpressionSyntax getRoot() {
        return Root;
    }

    public SyntaxToken getEndOfFileToken() {
        return EndOfFileToken;
    }

    public static SyntaxTree Parse(String text) {
        var parser = new Parser(text);
        return parser.Parse();
    }
}
