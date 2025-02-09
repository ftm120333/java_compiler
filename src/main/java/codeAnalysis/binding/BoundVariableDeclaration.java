package codeAnalysis.binding;

import codeAnalysis.VariableSymbol;

public class BoundVariableDeclaration extends BoundStatement {

    private final VariableSymbol variable;
    private final BoundExpression initializer;

    public BoundVariableDeclaration(VariableSymbol variable, BoundExpression initializer) {
        this.variable = variable;
        this.initializer = initializer;
    }

    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.VariableDeclaration;
    }

    public VariableSymbol getVariable() {
        return variable;
    }

    public BoundExpression getInitializer() {
        return initializer;
    }
}
