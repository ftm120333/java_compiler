package codeAnalysis.binding;


import codeAnalysis.VariableSymbol;
import codeAnalysis.compiling.DiagnosticBag;
import codeAnalysis.syntax.*;



import java.util.Map;

enum BoundNodeKind {
    LiteralExpression,
    VariableExpression,
    AssignmentExpression,
    BinaryExpression,
    UnaryExpression

}

abstract class BoundNode {
    public abstract BoundNodeKind getKind();
}


public class Binder {
   private final Map<VariableSymbol, Object> _variables;
   private final DiagnosticBag _diagnostics = new DiagnosticBag();

    public Binder(Map<VariableSymbol, Object> variables) {
        _variables = variables;
    }

    public DiagnosticBag diagnostics() {
       return _diagnostics;
   }

    public BoundExpression bindExpression(ExpressionSyntax syntax) {
        switch (syntax.getKind()){
            case SyntaxKind.LiteralExpression:
                return  BindLiteralExpression(((LiteralExpressionSyntax) syntax));

            case SyntaxKind.UnaryExpression:
                return  BindUnaryExpression((UnaryExpressionSyntax) syntax);

            case SyntaxKind.BinaryExpression:
                return  BindBinaryExpression((BinaryExpressionSyntax) syntax);
            case SyntaxKind.ParanthesizedExpression:
                return  BindParenthesizedExpression((ParanthrsizedExpressionSyntax) syntax);
            case SyntaxKind.NameExpression:
                return  BindNameExpression((NameExpressionSyntax) syntax);
            case SyntaxKind.AssignmentExpression:
                return  BindAssignmentExpression((AssignmentExpressionSyntax) syntax);
            default:
                throw new IllegalStateException("Unexpected value: " + syntax.getKind());
        }
    }

    private BoundExpression BindNameExpression(NameExpressionSyntax syntax) {
       var name = syntax.getIdentifierToken().text;
        var variable = _variables.keySet().stream()
                .filter(v -> v.getName().equals(name))
                .findFirst();
       if(variable != null){
           _diagnostics.ReportUndefinedName(syntax.getIdentifierToken().span(), name);
           return new BoundLiteralExpression(0);
       }
       return new BoundVariableExpression(variable.get());
    }

    private BoundExpression BindAssignmentExpression(AssignmentExpressionSyntax syntax) {
        var name = syntax.getIdentifierToken().text;
        var boundExpression = bindExpression(syntax.getExpression());
        var existingVariable = _variables.keySet().stream()
                .filter(v -> v.getName().equals(name))
                .findFirst();
        if(existingVariable != null)
            _variables.remove(existingVariable);
        var variable = new VariableSymbol(name, boundExpression.getClass());
        return new BoundAssignmentExpression(variable, boundExpression);
    }


    private BoundExpression BindParenthesizedExpression(ParanthrsizedExpressionSyntax syntax) {
        return bindExpression(syntax.expression);
    }

    private BoundExpression BindLiteralExpression(LiteralExpressionSyntax syntax) {
        //cast the value to a nullable integer (If the cast fails, it results in null)
        //If the result of the cast is null, it defaults to 0.
        Object value = syntax.getValue() != null ? syntax.getValue() : 0;
        //syntax.getValue() != null ? syntax.getValue() : 0;
        return new BoundLiteralExpression(value);
    }

    private BoundExpression BindUnaryExpression(UnaryExpressionSyntax syntax) {

       var boundOperand = bindExpression(syntax.operand);
       var boundOperator = BoundUnaryOperator.bind(syntax.operatorToken.kind, boundOperand.type());

       if (boundOperator == null) {
           _diagnostics.reportUndefinedUnaryOperator(syntax.operatorToken.span(), syntax.operatorToken.text, boundOperand.type());
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
            _diagnostics.addUndefinedBinaryOperator(syntax.getOperatorToken().span(), syntax.getOperatorToken().text, boundLeft.type(), boundRight.type() );
            return boundLeft;
        }


        return new BoundBinaryExpression(boundLeft, boundOperator, boundRight);

    }


}