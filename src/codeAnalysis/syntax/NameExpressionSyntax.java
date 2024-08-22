package codeAnalysis.syntax;

import java.util.Collections;
import java.util.List;

public final class  NameExpressionSyntax extends ExpressionSyntax{
 SyntaxToken identifierToken;

    public NameExpressionSyntax(SyntaxToken identifierToken) {
        this.identifierToken = identifierToken;
    }

    public SyntaxToken getIdentifierToken() {
        return identifierToken;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.NameExpression;
    }

    @Override
    public List<SyntaxNode> GetChildren() {
        return Collections.singletonList(identifierToken);
    }
}


