package main.lexer;

import java.util.ArrayList;

import java.util.Collections;
import java.util.List;

abstract class SyntaxNode {
    public abstract SyntaxKind getKind();
    public abstract Iterable<SyntaxNode> GetChildren();
}

abstract  class ExpressionSyntax extends SyntaxNode{

}
//sealed class main.lexer.NumberExpresionSyntax permits main.lexer.ExpressionSyntax
class NumberExpresionSyntax extends ExpressionSyntax  {
    public SyntaxToken numberToken;
    public  NumberExpresionSyntax(SyntaxToken numberToken){
         this.numberToken = numberToken;
    }

    public SyntaxToken NumberToken() {
        return numberToken;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.NumberExpression;
    }

    @Override
    public Iterable<SyntaxNode> GetChildren() {
        return Collections.singletonList(numberToken);
    }
}

class BinaryExpressionSyntax extends  ExpressionSyntax{
    ExpressionSyntax left;
    SyntaxToken operatorToken;
    ExpressionSyntax right;

    public BinaryExpressionSyntax(ExpressionSyntax left, SyntaxToken operatorToken, ExpressionSyntax right) {
        this.left = left;
        this.operatorToken = operatorToken;
        this.right = right;
    }

    public ExpressionSyntax getLeft() {
        return left;
    }

    public SyntaxToken getOperatorToken() {
        return operatorToken;
    }

    public ExpressionSyntax getRight() {
        return right;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.BinaryExpression;
    }

    @Override
    public Iterable<SyntaxNode> GetChildren() {
        List<SyntaxNode> children = new ArrayList<>();
        children.add(left);
        children.add(right);
        children.add(operatorToken);
        return children;
    }
}
