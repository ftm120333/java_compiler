package codeAnalysis.binding;

import codeAnalysis.VariableSymbol;

public final class BoundAssignmentExpression extends BoundExpression {
    final VariableSymbol variable;
    final BoundExpression expression;

    public BoundAssignmentExpression(VariableSymbol variable, BoundExpression expression) {
        this.variable = variable;
        this.expression = expression;
    }

    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.AssignmentExpression;
    }



    public VariableSymbol getVariable() {
        return variable;
    }

    public BoundExpression getExpression() {
        return expression;
    }

    @Override
    public Class<?> type() {
        return variable.getType();
    }
}
