package codeAnalysis.syntax;

import codeAnalysis.compiling.Diagnostic;

import java.util.Collection;
import java.util.List;

public class SyntaxTree {
    private final List<Diagnostic> diagnostics;
    private final ExpressionSyntax Root;
    private final SyntaxToken EndOfFileToken;

    public SyntaxTree(List<Diagnostic> diagnostics, ExpressionSyntax root, SyntaxToken endOfFileToken)
    {
        this.diagnostics = diagnostics;
        this.Root = root;
        this.EndOfFileToken = endOfFileToken;
    }

    public Collection<Diagnostic> getDiagnostics() {
        return diagnostics;
    }

    public ExpressionSyntax getRoot() {
        return Root;
    }

    public SyntaxToken getEndOfFileToken() {
        return EndOfFileToken;
    }

    public static SyntaxTree parse(String text) {
        var parser = new Parser(text);
        return parser.parse();
    }

    public static Iterable<SyntaxToken> parseTokens(String text){
        var lexer = new Lexer(text);
        while (true){
            var token= lexer.lex();
            if (token.kind == SyntaxKind.EndOfFileToken)
                break;
            return (Iterable<SyntaxToken>) token;
        }
    return null;
    }

}
