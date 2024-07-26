package main.lexer;

import java.util.ArrayList;

import java.util.Collections;
import java.util.List;

abstract class SyntaxNode {
    public abstract SyntaxKind getKind();
    public abstract List<SyntaxNode> GetChildren();
}

abstract  class ExpressionSyntax extends SyntaxNode{

}

//sealed class main.lexer.NumberExpresionSyntax permits main.lexer.ExpressionSyntax
class NumberExpressionSyntax extends ExpressionSyntax  {
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
    public List<SyntaxNode> GetChildren() {
        List<SyntaxNode> children = new ArrayList<>();
        children.add(left);
        children.add(right);
        children.add(operatorToken);
        return children;
    }
}

class SyntaxTree {
    private final List<String> diagnostics;
    private final ExpressionSyntax Root;
    private final SyntaxToken EndOfFileToken;

    public SyntaxTree(List<String> diagnostics, ExpressionSyntax root, SyntaxToken endOfFileToken)
    {
        this.diagnostics = diagnostics;
        this.Root = root;
        this.EndOfFileToken = endOfFileToken;
    }

    public Iterable<String> getDiagnostics() {
        return diagnostics;
    }

    public ExpressionSyntax getRoot() {
        return Root;
    }

    public SyntaxToken getEndOfFileToken() {
        return EndOfFileToken;
    }
}
