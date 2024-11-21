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

    public static BoundGlobalScope bindGlobalScope(BoundGlobalScope previous,CompilationUnitSyntax syntax) {
        var parentScope = createParentScope(previous);
        var binder = new Binder(parentScope);
        var expression = binder.bindStatement(syntax.getStatement());
        var variables = binder._scope.getDeclareVariables();
        var diagnostics = binder.diagnostics().get_diagnostics();
        return new BoundGlobalScope(previous,diagnostics, variables, expression);
    }

    private static BoundScope createParentScope(BoundGlobalScope previous) {
        var stack = new Stack<BoundGlobalScope>();
        while (previous != null) {
            stack.push(previous);
            previous = previous.previous;
        }
        BoundScope parent = new BoundScope(null);;
        while (!stack.isEmpty()) {
            previous = stack.pop();
            var scope = new BoundScope(parent);
            for (var variable : previous.variables) {
                scope.tryDeclare(variable);
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

    private BoundStatement bindStatement(StatementSyntax syntax) {
        switch (syntax.getKind()){
            case SyntaxKind.BlockStatement:
                return  BindBlockStatement((BlockStatementSyntax) syntax);

            case SyntaxKind.ExpressionStatement:
                return  BindExpressionStatement((ExpressionStatementSyntax) syntax);

            default:
                throw new IllegalStateException("Unexpected Syntax: " + syntax.getKind());
        }
    }

    private BoundStatement BindBlockStatement(BlockStatementSyntax syntax) {

        var statements = new ArrayList<BoundStatement>();
        for (var statementSyntax : syntax.getStatements()) {
            var statement = bindStatement((StatementSyntax) statementSyntax); // TODO: remove casting
            statements.add(statement);
        }
        return new BoundBlockStatement(statements);
    }

    private BoundStatement BindExpressionStatement(ExpressionStatementSyntax syntax) {
        var expression = bindExpression(syntax.getExpression());
        return new BoundExpressionStatement( expression);

    }




    private BoundExpression BindNameExpression(NameExpressionSyntax syntax) {
       var name = syntax.getIdentifierToken().text;
        var variable =_scope.tryLookup(name);
       if(!variable){
           System.out.println("Undefined variable name: " + name);
           _diagnostics.reportUndefinedName(syntax.getIdentifierToken().span(), name);
           return new BoundLiteralExpression(0);
       }
       return new BoundVariableExpression(new VariableSymbol(name, null));
    }

    private BoundExpression BindAssignmentExpression(AssignmentExpressionSyntax syntax) {
        var name = syntax.getIdentifierToken().text;
        var boundExpression = bindExpression(syntax.getExpression());
        var variable = new VariableSymbol(name, null);
        if(!_scope.tryLookup(name)){
            variable = new VariableSymbol(name, boundExpression.getClass());
            _scope.tryDeclare(variable);
        }

        if(boundExpression.getClass() != variable.getType()){
            _diagnostics.reportCannotConvert(syntax.getIdentifierToken().span(), boundExpression.getClass(), variable.getType());
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
        System.out.println("left: " +  syntax.getLeft());
        var boundRight = bindExpression(syntax.getRight());
        System.out.println("right: " +  syntax.getRight());
        var boundOperator = BoundBinaryOperator.bind(syntax.getOperatorToken().kind, boundLeft.type(), boundRight.type());
        System.out.println("operator: " +  syntax.getOperatorToken().kind);
        if (boundOperator == null) {
            _diagnostics.addUndefinedBinaryOperator(syntax.getOperatorToken().span(), syntax.getOperatorToken().text, boundLeft.type(), boundRight.type() );
            return boundLeft;
        }


        return new BoundBinaryExpression(boundLeft, boundOperator, boundRight);

    }


}


