package codeAnalysis.binding;

import codeAnalysis.VariableSymbol;

public final class BoundVariableExpression extends BoundExpression {
    final VariableSymbol variable;

    public BoundVariableExpression(VariableSymbol Variable ) {
        this.variable = Variable;

    }



    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.VariableExpression;
    }

    @Override
    public Class<?> type() {
        return variable.getType();
    }

    public VariableSymbol getName() {
        return variable;
    }

}
