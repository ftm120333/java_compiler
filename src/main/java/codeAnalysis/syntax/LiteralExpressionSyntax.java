package codeAnalysis.syntax;

import java.util.Collections;
import java.util.List;

//sealed class main.lexer.NumberExpresionSyntax permits main.lexer.ExpressionSyntax
public class LiteralExpressionSyntax extends ExpressionSyntax  {
    private  SyntaxToken literalToken;
    private Object value;

    public LiteralExpressionSyntax(SyntaxToken literalToken){
        this(literalToken, literalToken.value);
    }

    public LiteralExpressionSyntax(SyntaxToken literalToken, Object value) {
      this.literalToken = literalToken;
      this.value = value;

    }
    public Object getValue() {
        return value;
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
