package codeAnalysis.binding;

import java.beans.Expression;

public final class BoundAssignmentExpression extends BoundExpression {
    final String name;
    final BoundExpression boundExpression;

    public BoundAssignmentExpression(String name, BoundExpression boundExpression) {
        this.name = name;
        this.boundExpression = boundExpression;
    }

    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.AssignmentExpression;
    }

    @Override
    public Class<?> type() {
        return Expression.class;
    }

    public String getName() {
        return name;
    }

    public BoundExpression getBoundExpression() {
        return boundExpression;
    }
}
