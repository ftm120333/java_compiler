package codeAnalysis.binding;


import codeAnalysis.VariableSymbol;
import codeAnalysis.compiling.Diagnostic;
import codeAnalysis.compiling.DiagnosticBag;
import codeAnalysis.syntax.*;


import java.util.*;

abstract class BoundNode {
    public abstract BoundNodeKind getKind();
}

public class Binder {
   private final DiagnosticBag _diagnostics = new DiagnosticBag();
    private  Map<VariableSymbol, Object> _variables;
   private BoundScope _scope;

    public Binder(BoundScope parent) {
        _scope = new BoundScope(parent);
    }

    public static BoundGlobalScope bindGlobalScope(BoundGlobalScope previous, CompilationUnitSyntax syntax) {

        var parentScope = createParentScope(previous);
        var binder = new Binder(parentScope);
        var expression = binder.bindStatement(syntax.getStatement());
        List<VariableSymbol>  variables = binder._scope.getDeclareVariables();
        List<Diagnostic> diagnostics = binder._diagnostics.get_diagnostics();


        if (previous != null) {
            diagnostics.addAll(0, previous.getDiagnostics());
        }


        return new BoundGlobalScope(previous,diagnostics, variables, expression);
    }

    //Reconstructs a hierarchical scope chain for variable inheritance.
    private static BoundScope createParentScope(BoundGlobalScope previous) {
        var stack = new Stack<BoundGlobalScope>();
        while (previous != null) {
            stack.push(previous);
            previous = previous.getPrevious();
        }

        BoundScope parent = null;
        while (!stack.isEmpty()) {
            previous = stack.pop();
            var scope = new BoundScope(parent);
            for (VariableSymbol variable : previous.getVariables()) {

                scope.tryDeclare(variable);
            }
            parent = scope;
        }


        return parent;
    }

    public List<Diagnostic> getDiagnostics() {
        return _diagnostics.get_diagnostics();
    }

    private BoundStatement bindStatement(StatementSyntax syntax) {
        switch (syntax.getKind()){
            case SyntaxKind.BlockStatement:
                return  BindBlockStatement((BlockStatementSyntax) syntax);
            case SyntaxKind.VariableDeclaration:
                return BindVariableDeclaration((VariableDeclarationSyntax) syntax);
            case SyntaxKind.ExpressionStatement:
                return  BindExpressionStatement((ExpressionStatementSyntax) syntax);
            default:
                throw new RuntimeException("Unexpected syntax " + syntax.getKind());
        }
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
                return  bindNameExpression((NameExpressionSyntax) syntax);
            case SyntaxKind.AssignmentExpression:
                return  BindAssignmentExpression((AssignmentExpressionSyntax) syntax);
            default:
                throw new RuntimeException("Unexpected syntax " + syntax.getKind());
        }
    }




    private BoundStatement BindBlockStatement(BlockStatementSyntax syntax) {
         List<BoundStatement> statements = new ArrayList<>();
         this._scope = new BoundScope(this._scope);
         for (StatementSyntax statementSyntax : syntax.getStatements()) {
           // var statement = bindStatement((StatementSyntax) statementSyntax);
             BoundStatement statement = bindStatement(statementSyntax);
            statements.add(statement);
        }

        _scope = _scope.getParent();
        return new BoundBlockStatement(statements);
    }

    private BoundStatement BindVariableDeclaration(VariableDeclarationSyntax syntax) {
        var name = syntax.getIdentifier().text;
        boolean isReadonly = syntax.getKeyword().kind == SyntaxKind.LetKeyword;
        BoundExpression initializer = bindExpression(syntax.getInitializer());
        VariableSymbol variable = new VariableSymbol(name,isReadonly,initializer.getClass());
        if(!_scope.tryDeclare(variable)){
            _diagnostics.reportVariableAlreadyDeclared(syntax.getIdentifier().span(), name);
        }
        return new BoundVariableDeclaration(variable, initializer);
    }


    private BoundStatement BindExpressionStatement(ExpressionStatementSyntax syntax) {
        BoundExpression expression = bindExpression(syntax.getExpression());
        return new BoundExpressionStatement( expression);
    }

//    private BoundExpression bindNameExpression(NameExpressionSyntax syntax) {
//        String name = syntax.getIdentifierToken().text;
//        VariableSymbol variable = _scope.lookupVariable(name);
//        if (variable == null) {
//            _diagnostics.reportUndefinedName(syntax.getIdentifierToken().span(), name);
//            return new BoundLiteralExpression(0);
//        }
//        BoundExpression boundExpression = new BoundVariableExpression(variable);
//
//        if (variable.isReadOnly()) {
//            _diagnostics.reportVariableAlreadyDeclared(syntax.getIdentifierToken().span(), name);
//        }
//
//        if (!boundExpression.type().equals(variable.getType())) {
//            _diagnostics.reportCannotConvert(syntax.getIdentifierToken().span(), boundExpression.type(), variable.getType());
//            return boundExpression;
//        }
//
//        return new BoundAssignmentExpression(variable, boundExpression);
//    }
private BoundExpression bindNameExpression(NameExpressionSyntax syntax) {
    String name = syntax.getIdentifierToken().text;

    // Lookup variable in the current scope
    VariableSymbol variable = _scope.lookupVariable(name);
    if (variable == null) {
        _diagnostics.reportUndefinedName(syntax.getIdentifierToken().span(), name);
        return new BoundLiteralExpression(0); // Default value for undefined variables
    }
    // Return the variable expression directly
    return new BoundVariableExpression(variable);
}



    private BoundExpression BindAssignmentExpression(AssignmentExpressionSyntax syntax) {

        String name = syntax.getIdentifierToken().text;
        BoundExpression boundExpression = bindExpression(syntax.getExpression());
        boolean isReadonly = syntax.getIdentifierToken().kind == SyntaxKind.LetKeyword;

        //check variable declaration
        var variable = new VariableSymbol(name, isReadonly, boundExpression.getClass());

        if(!_scope.tryLookup(name)){
            _diagnostics.reportUndefinedName(syntax.getIdentifierToken().span(), name);
            return boundExpression;
        }
        if(variable.isReadOnly()){
            _diagnostics.reportCannotAssign(syntax.getEqualsToken().span(), name);
        }
        if(!boundExpression.type().equals(variable.getType())){
            _diagnostics.reportCannotConvert(syntax.getIdentifierToken().span(), boundExpression.getClass(), variable.getType());
            return boundExpression;
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
        return new BoundLiteralExpression(value);
    }

    private BoundExpression BindUnaryExpression(UnaryExpressionSyntax syntax) {
        BoundExpression boundOperand = bindExpression(syntax.operand);

        BoundUnaryOperator boundOperator = BoundUnaryOperator.bind(syntax.operatorToken.kind, boundOperand.type());

       if (boundOperator == null) {
           _diagnostics.reportUndefinedUnaryOperator(syntax.operatorToken.span(), syntax.operatorToken.text, boundOperand.type());
           return boundOperand;
       }
                                            //boundOperatorKind.value (in the video)
       return new BoundUnaryExpression(boundOperator, boundOperand);
    }
    private BoundExpression BindBinaryExpression(BinaryExpressionSyntax syntax) {
        BoundExpression boundLeft = bindExpression(syntax.getLeft());
        BoundExpression boundRight = bindExpression(syntax.getRight());

        // Resolve actual types of operands for operator binding
        Class<?> leftType = resolveType(boundLeft);
        Class<?> rightType = resolveType(boundRight);

        // Find the matching binary operator
        BoundBinaryOperator boundOperator = BoundBinaryOperator.bind(syntax.getOperatorToken().kind, leftType, rightType);

        if (boundOperator == null) {
            _diagnostics.addUndefinedBinaryOperator(syntax.getOperatorToken().span(), syntax.getOperatorToken().text, leftType, rightType);
            return boundLeft; // Return the left operand as a fallback
        }

        return new BoundBinaryExpression(boundLeft, boundOperator, boundRight);
    }

    // Helper method to resolve the type of a bound expression
    private Class<?> resolveType(BoundExpression expression) {
        if (expression instanceof BoundLiteralExpression) {
            return expression.type();
        } else if (expression instanceof BoundVariableExpression variableExpression) {
            return variableExpression.getVariable().getType();
        }
        return expression.type(); // Default case
    }
//    private BoundExpression BindBinaryExpression(BinaryExpressionSyntax syntax){
//        BoundExpression boundLeft = bindExpression(syntax.getLeft());
//        BoundExpression boundRight = bindExpression(syntax.getRight());
//
//
//        BoundBinaryOperator boundOperator = BoundBinaryOperator.bind(syntax.getOperatorToken().kind, boundLeft.type(), boundRight.type());
//          if (boundOperator == null) {
//            _diagnostics.addUndefinedBinaryOperator(syntax.getOperatorToken().span(), syntax.getOperatorToken().text, boundLeft.type(), boundRight.type() );
//            return boundLeft;
//        }
//
//        return new BoundBinaryExpression(boundLeft, boundOperator, boundRight);
//
//    }
}


