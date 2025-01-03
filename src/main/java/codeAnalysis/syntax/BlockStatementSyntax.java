package codeAnalysis.syntax;

import java.util.List;

public class BlockStatementSyntax extends StatementSyntax {

    private final SyntaxToken openBraceToken;
    private final List<StatementSyntax> statements;
    private final SyntaxToken closeBraceToken;

    public BlockStatementSyntax(SyntaxToken openBraceToken, List<StatementSyntax> statements,
                                SyntaxToken closeBraceToken) {
        this.openBraceToken = openBraceToken;
        this.statements = statements;
        this.closeBraceToken = closeBraceToken;
    }


    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.BlockStatement;
    }


    public SyntaxToken getOpenBraceToken() {
        return openBraceToken;
    }

    public List<StatementSyntax> getStatements() {
        return statements;
    }

    public SyntaxToken getCloseBraceToken() {
        return closeBraceToken;
    }
    @Override
    public List<SyntaxNode> GetChildren() {
        return List.of();
    }

}


