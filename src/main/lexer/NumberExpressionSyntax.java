package main.lexer;

import java.util.Collections;
import java.util.List;

//sealed class main.lexer.NumberExpresionSyntax permits main.lexer.ExpressionSyntax
public class NumberExpressionSyntax extends ExpressionSyntax  {
    private  SyntaxToken numberToken;
    public NumberExpressionSyntax(SyntaxToken numberToken){
        this.numberToken = numberToken;
    }

    public SyntaxToken getNumberToken() {
        return numberToken;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.NumberExpression;
    }

    @Override
    public List<SyntaxNode> GetChildren() {
        return Collections.singletonList(numberToken);
    }
}
