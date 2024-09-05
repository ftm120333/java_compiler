package codeAnalysis.binding;

public class BoundUnaryExpression extends BoundExpression {

    BoundExpression operand;
    BoundUnaryOperator op;

    public BoundUnaryExpression(BoundUnaryOperator op, BoundExpression operand) {
        this.operand = operand;
        this.op = op;
    }

    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.UnaryExpression;
    }

    @Override
    public Class<?> type() {
        return op.getResultType();
    }

    public BoundExpression getOperand() {
        return operand;
    }

    public BoundUnaryOperator getOperatorKind() {
        return op;
    }


}
