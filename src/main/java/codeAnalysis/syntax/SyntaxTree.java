package codeAnalysis.syntax;

import codeAnalysis.compiling.Diagnostic;
import codeAnalysis.text.SourceText;

import java.util.Collection;
import java.util.List;


public class SyntaxTree {
    private final List<Diagnostic> diagnostics;
    private final CompilationUnitSyntax Root;

    private final SourceText text;

    private SyntaxTree(SourceText text)
    {
        var parser = new Parser(text);
        this.Root = parser.parseCompilationUnit();
        this.diagnostics = parser.diagnosticBag().get_diagnostics();
        this.text = text;
    }

    public Collection<Diagnostic> getDiagnostics() {
        return diagnostics;
    }

    public CompilationUnitSyntax getRoot() {
        return Root;
    }

    public SourceText getText() {
        return text;
    }


    public static SyntaxTree parse(String text) {
        var sourceText = SourceText.from(text);
        return parse(sourceText);
    }

    public static SyntaxTree parse(SourceText text) {
        return new SyntaxTree(text);
    }

    public static Iterable<SyntaxToken> parseTokens(String text){
        var sourceText = SourceText.from(text);
        return  parseTokens(sourceText);

    }
    public static Iterable<SyntaxToken> parseTokens(SourceText text){
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
