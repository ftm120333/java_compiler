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
       var boundOperator = BoundUnaryOperator.bind(syntax.operatorToken.kind, boundOperand.type());

       if (boundOperator == null) {
           diagnostics.add("unary operator: " + syntax.operatorToken.text + " is not valid for type: " + boundOperand.type().getName());
           return boundOperand;
       }
                                            //boundOperatorKind.value (in the video)
       return new BoundUnaryExpression(boundOperator, boundOperand);
    }

    private BoundExpression BindBinaryExpression(BinaryExpressionSyntax syntax){

        var boundLeft = bindExpression(syntax.getLeft());
        var boundRight = bindExpression(syntax.getRight());
        var boundOperator = BoundBinaryOperator.bind(syntax.getOperatorToken().kind, boundLeft.type(), boundRight.type());

        if (boundOperator == null) {
            diagnostics.add("binary operator: " + syntax.getOperatorToken().text + " is not valid for type: " +boundLeft.type() + " and " + boundRight.type());
            return boundLeft;
        }


        return new BoundBinaryExpression(boundLeft, boundOperator, boundRight);

    }


}