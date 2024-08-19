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


public class Binder {
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
       var valueObj = syntax.getValue();
       var value = (valueObj instanceof Integer)? (Integer) valueObj : 0;
        //syntax.getValue() != null ? syntax.getValue() : 0;
        return new BoundLiteralExpression(value);
    }

    private BoundExpression BindUnaryExpression(UnaryExpressionSyntax syntax) {

       var boundOperand = bindExpression(syntax.operand);
       var boundOperatorKind = BindUnaryOperatorKind(syntax.operatorToken.kind, boundOperand.type());

       if (boundOperatorKind == null) {
           diagnostics.add("unary operator: " + syntax.operatorToken.text + " is not valid for type: " + boundOperand.type().getName());
           return boundOperand;
       }
                                            //boundOperatorKind.value (in the video)
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
        if (operandType == Integer.class)
        {
        return switch (kind) {
            case SyntaxKind.PlusToken -> BoundUnaryOperatorKind.Identity;
            case SyntaxKind.MinusToken -> BoundUnaryOperatorKind.Negation;
            default -> throw new IllegalStateException("Unexpected unary operator: " + kind);
        };
    }
        if (operandType == Boolean.class)
        {
            return switch (kind) {
                case SyntaxKind.BangToken -> BoundUnaryOperatorKind.LogicalNegation;
                default -> throw new IllegalStateException("Unexpected unary operator: " + kind);
            };
        }
        return null;
   }
    private BoundBinaryOperatorKind BindBinaryOperatorKind(SyntaxKind kind, Class<?> leftType, Class<?> rightType) {
        if (leftType == Integer.class || rightType == Integer.class){
            return switch (kind) {
                case SyntaxKind.StarToken -> BoundBinaryOperatorKind.Multiplication;
                case SyntaxKind.SlashToken -> BoundBinaryOperatorKind.Division;
                case SyntaxKind.MinusToken -> BoundBinaryOperatorKind.Subtraction;
                case SyntaxKind.PlusToken -> BoundBinaryOperatorKind.Addition;
                default -> throw new IllegalStateException("Unexpected binary operator: " + kind);
        };}

        if (leftType == Boolean.class || rightType == Boolean.class) {
            return switch (kind) {
                case SyntaxKind.AmpersandAmpersandToken -> BoundBinaryOperatorKind.LogicalAnd;
                case SyntaxKind.PipePipeToken -> BoundBinaryOperatorKind.LogicalOr;

                default -> throw new IllegalStateException("Unexpected binary operator: " + kind);
            };
        }

        return null;
    }
}