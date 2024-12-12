package codeAnalysis.binding;

public class BoundLiteralExpression extends BoundExpression {
    Object value;

    public BoundLiteralExpression(Object value) {
        this.value = value;
    }

    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.LiteralExpression;
    }



    @Override
    public Class<?> type() {
        return value.getClass();
    }

    public Object getValue() {
        return value;
    }
}
