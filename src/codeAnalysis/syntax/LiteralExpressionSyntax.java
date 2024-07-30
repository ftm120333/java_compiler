package codeAnalysis.syntax;

import java.util.Collections;
import java.util.List;

//sealed class main.lexer.NumberExpresionSyntax permits main.lexer.ExpressionSyntax
public class LiteralExpressionSyntax extends ExpressionSyntax  {
    private  SyntaxToken literalToken;
    public LiteralExpressionSyntax(SyntaxToken numberToken){
        this.literalToken = numberToken;
    }

    public SyntaxToken getNumberToken() {
        return literalToken;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.LiteralExpression;
    }

    @Override
    public List<SyntaxNode> GetChildren() {
        return Collections.singletonList(literalToken);
    }
}
