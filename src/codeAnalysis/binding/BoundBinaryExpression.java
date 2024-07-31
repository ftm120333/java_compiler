package codeAnalysis.binding;

public class BoundBinaryExpression extends BoundExpression {

    BoundExpression left;
    BoundExpression right;
    BoundBinaryOperatorKind operatorKind;
    public BoundBinaryExpression(BoundExpression left, BoundBinaryOperatorKind operatorKind, BoundExpression right) {
        this.left = left;
        this.right = right;
        this.operatorKind = operatorKind;
    }
    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.BinaryExpression;
    }
    @Override
    public Class<?> type() {
        return left.type();
    }
    public BoundExpression getLeft() {
        return left;
    }
    public BoundExpression getRight() {
        return right;
    }
    public BoundBinaryOperatorKind getOperatorKind() {
        return operatorKind;
    }
}
