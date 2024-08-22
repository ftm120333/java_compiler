package codeAnalysis.binding;

public final class BoundVariableExpression extends BoundExpression {
    final String name;
    final  Class<?> type;
    public BoundVariableExpression(String name, Class<?> type) {
        this.name = name;

        this.type = type;
    }

    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.VariableExpression;
    }

    @Override
    public Class<?> type() {
        return type;
    }

    public String getName() {
        return name;
    }
}
