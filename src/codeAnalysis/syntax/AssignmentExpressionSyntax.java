package codeAnalysis.syntax;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

// a = 10
// a = b = 5
//      =
//    /  \
//   a    =
//       /  \
//     b      5
public final class AssignmentExpressionSyntax extends ExpressionSyntax {
    SyntaxToken identifierToken;
    SyntaxToken equalsToken;
    ExpressionSyntax expression;

    public AssignmentExpressionSyntax(SyntaxToken identifierToken, SyntaxToken equalsToken, ExpressionSyntax expression) {
        this.identifierToken = identifierToken;
        this.equalsToken = equalsToken;
        this.expression = expression;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.AssignmentExpression;
    }

    public SyntaxToken getIdentifierToken() {
        return identifierToken;
    }

    public SyntaxToken getEqualsToken() {
        return equalsToken;
    }

    public ExpressionSyntax getExpression() {
        return expression;
    }

    @Override
    public List<SyntaxNode> GetChildren() {
        return Arrays.asList(identifierToken, equalsToken, expression);

    }
}
