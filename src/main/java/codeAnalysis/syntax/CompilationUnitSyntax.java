package codeAnalysis.syntax;

import java.util.Collections;
import java.util.List;

public class CompilationUnitSyntax extends SyntaxNode {
    StatementSyntax statement;
    SyntaxToken endOfFileToken;

    public CompilationUnitSyntax(StatementSyntax statement, SyntaxToken endOfFileToken) {
        this.statement = statement;
        this.endOfFileToken = endOfFileToken;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.CompilationUnit;
    }

    @Override

    public List<SyntaxNode> GetChildren() {
        return List.of(statement, endOfFileToken);
    }


    public StatementSyntax getStatement() {
        return statement;
    }

    public SyntaxToken getEndOfFileToken() {
        return endOfFileToken;
    }
}
