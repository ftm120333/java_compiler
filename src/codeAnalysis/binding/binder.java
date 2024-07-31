package codeAnalysis.binding;


import codeAnalysis.syntax.*;
import java.util.ArrayList;
import java.util.List;

enum BoundNodeKind {
    LiteralExpression, BinaryExpression, UnaryExpression

}

abstract class BoundNode {
    public abstract BoundNodeKind getKind();
}

enum BoundUnaryOperatorKind {
    Identity,
    Negation
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
    public Class<?> type() {
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


class Binder {
   private final List<String> diagnostics = new ArrayList<>();

   public List<String> getDiagnostics() {
       return diagnostics;
   }

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

       if (boundOperatorKind == null) {
           diagnostics.add("unary operator: " + syntax.operatorToken.text + " is not valid for type: " + boundOperand.type().getName());
           return boundOperand;
       }

       return new BoundUnaryExpression(boundOperatorKind, boundOperand);
    }

    private BoundExpression BindBinaryExpression(BinaryExpressionSyntax syntax){

        var boundLeft = bindExpression(syntax.getLeft());
        var boundRight = bindExpression(syntax.getRight());
        var boundOperatorKind = BindBinaryOperatorKind(syntax.getOperatorToken().kind, boundLeft.type(), boundRight.type());

        if (boundOperatorKind == null) {
            diagnostics.add("binary operator: " + syntax.getOperatorToken().text + " is not valid for type: " +boundLeft.type() + " and " + boundRight.type());
            return boundLeft;
        }


        return new BoundBinaryExpression(boundLeft, boundOperatorKind, boundRight);

    }

    private BoundUnaryOperatorKind BindUnaryOperatorKind(SyntaxKind kind, Class<?> operandType) {

        //TODO: SOLVE THIS ISSUE
      if (!(operandType != Integer.class))

            return null;

        //start from 58:13
        return switch (kind) {
            case SyntaxKind.PlusToken -> BoundUnaryOperatorKind.Identity;
            case SyntaxKind.MinusToken -> BoundUnaryOperatorKind.Negation;
            default -> throw new IllegalStateException("Unexpected unary operator: " + kind);
        };
    }
    private BoundBinaryOperatorKind BindBinaryOperatorKind(SyntaxKind kind, Class<?> leftType, Class<?> rightType) {
        if (!(leftType != Integer.class || rightType != Integer.class )){
             return null;
         }


        return switch (kind) {
            case SyntaxKind.StarToken -> BoundBinaryOperatorKind.Multiplication;
            case SyntaxKind.SlashToken -> BoundBinaryOperatorKind.Division;
            case SyntaxKind.MinusToken -> BoundBinaryOperatorKind.Subtraction;
            case SyntaxKind.PlusToken -> BoundBinaryOperatorKind.Addition;
            default -> throw new IllegalStateException("Unexpected binary operator: " + kind);
        };
    }
}