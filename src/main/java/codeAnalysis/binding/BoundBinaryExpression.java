package codeAnalysis.binding;


public class BoundBinaryExpression extends BoundExpression {

    BoundExpression left;
    BoundExpression right;
    BoundBinaryOperator op;
    public BoundBinaryExpression(BoundExpression left, BoundBinaryOperator op, BoundExpression right) {
        this.left = left;
        this.right = right;
        this.op = op;
    }
    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.BinaryExpression;
    }
    @Override
    public Class<?> type() {
        return op.getResultType();
    }
    public BoundExpression getLeft() {
        return left;
    }
    public BoundExpression getRight() {
        return right;
    }
    public BoundBinaryOperator getOperatorKind() {
        return op;
    }
}
