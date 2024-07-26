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
        children.add(operatorToken);
        children.add(right);
        return children;
    }
}

class ParanthrsizedExpressionSyntax extends ExpressionSyntax {
    SyntaxToken openParenthesisToken;
    ExpressionSyntax expression;
    SyntaxToken closedParenthesisToken;

    public ParanthrsizedExpressionSyntax(SyntaxToken openParenthesisToken, ExpressionSyntax expression, SyntaxToken closedParenthesisToken) {
         this.openParenthesisToken = openParenthesisToken;
         this.expression = expression;
         this.closedParenthesisToken = closedParenthesisToken;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.ParanthrsizedExpression;
    }

    @Override
    public List<SyntaxNode> GetChildren() {
        List<SyntaxNode> children = new ArrayList<>();
        children.add(openParenthesisToken);
        children.add(expression);
        children.add(closedParenthesisToken);
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

    public static SyntaxTree Parse(String text) {
        var parser = new Parser(text);
        return parser.Parse();
    }
}


class Evaluator{
    private final ExpressionSyntax root;

    public Evaluator(ExpressionSyntax root) {
        this.root = root;
    }

    public int Evaluate() throws Exception {
        return EvaluateExpression(root);
    }

    private int EvaluateExpression(ExpressionSyntax node) throws Exception {
        if (node instanceof NumberExpressionSyntax n) {
            return (int) n.getNumberToken().value;
        }

        if (node instanceof BinaryExpressionSyntax b) {
            var left = EvaluateExpression(b.getLeft());
            var right = EvaluateExpression(b.getRight());
            if (b.getOperatorToken().kind == SyntaxKind.PlusToken) {
                return left + right;
            } else if (b.getOperatorToken().kind == SyntaxKind.MinusToken) {
                return left - right;
            } else if (b.getOperatorToken().kind == SyntaxKind.StarToken) {
                return left * right;
            } else if (b.getOperatorToken().kind == SyntaxKind.SlashToken) {
                return left / right;
            }else
                throw new Exception("UnExpected binary operator " + b.getOperatorToken().kind);
        }

        if (node instanceof ParanthrsizedExpressionSyntax p) {
            return EvaluateExpression(p.expression);
        }

        throw new Exception("UnExpected node " + node.getKind());

    }


}
