package codeAnalysis.syntax;

import java.util.List;

public class BlockStatementSyntax extends StatementSyntax {

    private final SyntaxToken openBraceToken;
    private final List<SyntaxNode> statements;
    private final SyntaxToken closeBraceToken;

    public BlockStatementSyntax(SyntaxToken openBraceToken, List<SyntaxNode> statements,
                                SyntaxToken closeBraceToken) {
        this.openBraceToken = openBraceToken;
        this.statements = statements;
        this.closeBraceToken = closeBraceToken;
    }


    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.BlockStatement;
    }

    @Override
    public List<SyntaxNode> GetChildren() {
        return List.of();
    }


    public SyntaxToken getOpenBraceToken() {
        return openBraceToken;
    }

    public List<SyntaxNode> getStatements() {
        return statements;
    }

    public SyntaxToken getCloseBraceToken() {
        return closeBraceToken;
    }
}


