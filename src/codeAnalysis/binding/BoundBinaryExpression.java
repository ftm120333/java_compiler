package codeAnalysis.binding;


import codeAnalysis.syntax.SyntaxKind;

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
        return left.type();
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
