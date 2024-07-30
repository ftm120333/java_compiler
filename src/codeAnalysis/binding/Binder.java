package codeAnalysis.binding;


import codeAnalysis.syntax.*;

import java.lang.reflect.Type;

enum BoundNodeKind {
    LiteralExpression, BinaryExpression, UnaryExpression

}

abstract class BoundNode {
    public abstract BoundNodeKind getKind();
}

abstract class BoundExpression extends BoundNode {

    public abstract Type type();
}

enum BoundUnaryOperatorKind {
    Identity,
    Negation
}

class BoundLiteralExpression extends BoundExpression {
    Object value;
    public BoundLiteralExpression(Object value) {
        this.value = value;
    }
    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.LiteralExpression;
    }

    // TODO: check this

    @Override
    public Type type() {
        return value.getClass();
    }
    public Object getValue() {
        return value;
    }
}

enum BoundBinaryOperatorKind{
    Addition,
    Subtraction,
    Multiplication,
    Division
}


class BoundBinaryExpression extends BoundExpression {

    BoundExpression left;
    BoundExpression right;
    BoundBinaryOperatorKind operatorKind;
    public BoundBinaryExpression(BoundExpression left, BoundBinaryOperatorKind operatorKind, BoundExpression right) {
        this.left = left;
        this.right = right;
        this.operatorKind = operatorKind;
    }
    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.BinaryExpression;
    }
    @Override
    public Type type() {
        return left.type();
    }
    public BoundExpression getLeft() {
        return left;
    }
    public BoundExpression getRight() {
        return right;
    }
    public BoundBinaryOperatorKind getOperatorKind() {
        return operatorKind;
    }
}
class BoundUnaryExpression extends BoundExpression {

    BoundExpression operand;
    BoundUnaryOperatorKind operatorKind;

    public BoundUnaryExpression(BoundUnaryOperatorKind operatorKind,BoundExpression operand) {
        this.operand = operand;
        this.operatorKind = operatorKind;
    }

    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.UnaryExpression;
    }

    @Override
    public Type type() {
        return operand.type();
    }

    public BoundExpression getOperand() {
        return operand;
    }

    public BoundUnaryOperatorKind getOperatorKind() {
        return operatorKind;
    }


}


class Binder {

    public BoundExpression bindExpression(ExpressionSyntax syntax) {
        switch (syntax.getKind()){
            case SyntaxKind.LiteralExpression:
                return  BindLiteralExpression(((LiteralExpressionSyntax) syntax));

            case SyntaxKind.UnaryExpression:
                return  BindUnaryExpression((UnaryExpressionSyntax) syntax);

            case SyntaxKind.BinaryExpression:
                return  BindBinaryExpression((BinaryExpressionSyntax) syntax);

            default:
                throw new IllegalStateException("Unexpected value: " + syntax.getKind());
        }
    }

    private BoundExpression BindLiteralExpression(LiteralExpressionSyntax syntax) {
        //cast the value to a nullable integer (If the cast fails, it results in null)
        //If the result of the cast is null, it defaults to 0.
       var valueObj = syntax.getNumberToken().value;
       var value = (valueObj instanceof Integer)? (Integer) valueObj : 0;

        return new BoundLiteralExpression(value);
    }

    private BoundExpression BindUnaryExpression(UnaryExpressionSyntax syntax) {
        var boundOperand = bindExpression(syntax.operand);
       var boundOperatorKind = BindUnaryOperatorKind(syntax.operatorToken.kind, boundOperand.type());


       return new BoundUnaryExpression(boundOperatorKind, boundOperand);
    }

    private BoundExpression BindBinaryExpression(BinaryExpressionSyntax syntax){

        var boundOperatorKind = BindBinaryOperatorKind(syntax.getOperatorToken().kind);
        var boundLeft = bindExpression(syntax.getLeft());
        var boundRight = bindExpression(syntax.getRight());
        return new BoundBinaryExpression(boundLeft, boundOperatorKind, boundRight);
    }

    private BoundUnaryOperatorKind BindUnaryOperatorKind(SyntaxKind kind, Type operandType) {
/*        if (!(operandType instanceof Integer))
            return null;
        */
        //start from 58:13
        switch (kind) {
            case SyntaxKind.PlusToken:
                return BoundUnaryOperatorKind.Identity;
            case SyntaxKind.MinusToken:
                return BoundUnaryOperatorKind.Negation;
            default:
                throw new IllegalStateException("Unexpected unary operator: " + kind);
        }
    }
    private BoundBinaryOperatorKind BindBinaryOperatorKind(SyntaxKind kind) {
        switch (kind) {
            case SyntaxKind.StarToken:
                return BoundBinaryOperatorKind.Multiplication;
            case SyntaxKind.SlashToken:
                return BoundBinaryOperatorKind.Division;
            case SyntaxKind.MinusToken:
                return BoundBinaryOperatorKind.Subtraction;
            case SyntaxKind.PlusToken:
                return BoundBinaryOperatorKind.Addition;
            default:
                throw new IllegalStateException("Unexpected binary operator: " + kind);
        }
    }
}