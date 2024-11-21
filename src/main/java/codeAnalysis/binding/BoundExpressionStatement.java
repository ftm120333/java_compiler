package codeAnalysis.binding;

import java.util.List;

public class BoundExpressionStatement extends BoundStatement {

    private final BoundExpression expression;

    public BoundExpressionStatement(BoundExpression expression){

        this.expression = expression;

    }

    public BoundNodeKind getKind() {
        return BoundNodeKind.ExpressionStatement;
    }

    public BoundExpression getExpression() {
        return (BoundExpression) expression;
    }

}
