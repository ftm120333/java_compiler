package codeAnalysis.binding;


import codeAnalysis.VariableSymbol;
import codeAnalysis.compiling.DiagnosticBag;
import codeAnalysis.syntax.*;


import java.util.*;

abstract class BoundNode {
    public abstract BoundNodeKind getKind();
}

public class Binder {
   private final DiagnosticBag _diagnostics = new DiagnosticBag();
   private BoundScope _scope;
    public Binder(BoundScope parent) {
        _scope = new BoundScope(parent);
    }
    public static BoundGlobalScope bindGlobalScope(BoundGlobalScope previous, CompilationUnitSyntax syntax) {
        var binder = new Binder(null);
        var expression = binder.bindExpression(syntax.getExpression());
        var variables = binder._scope;
        var dagnostics = binder._diagnostics;
        return new BoundGlobalScope(null,dagnostics, variables, expression);
    }
    private static BoundScope createParentScope(BoundGlobalScope previous) {
        var stack = new Stack<BoundGlobalScope>();
        while (previous != null) {
            stack.push(previous);
            previous = previous.previous;
        }
        BoundScope parent = null;
        while (!stack.isEmpty()) {
            previous = stack.pop();
            var scope = new BoundScope(parent);
            for (var variable : previous.variables) {
                parent.tryDeclare(variable);
            }
            parent = scope;
        }
        return parent;
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
        var variable =_scope.tryLookup(name, new VariableSymbol(name, null));
       if(!variable){
           _diagnostics.ReportUndefinedName(syntax.getIdentifierToken().span(), name);
           return new BoundLiteralExpression(0);
       }
       return new BoundVariableExpression( variable);
    }

    private BoundExpression BindAssignmentExpression(AssignmentExpressionSyntax syntax) {
        var name = syntax.getIdentifierToken().text;
        var boundExpression = bindExpression(syntax.getExpression());
        var variable = new VariableSymbol(name, boundExpression.getClass());
        if(!_scope.tryLookup(name, variable)){
            _diagnostics.ReportVariableAlreadyDeclared(syntax.getIdentifierToken().span(), name);
        }
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


