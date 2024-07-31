package codeAnalysis.binding;

public class BoundUnaryExpression extends BoundExpression {

    BoundExpression operand;
    BoundUnaryOperatorKind operatorKind;

    public BoundUnaryExpression(BoundUnaryOperatorKind operatorKind, BoundExpression operand) {
        this.operand = operand;
        this.operatorKind = operatorKind;
    }

    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.UnaryExpression;
    }

    @Override
    public Class<?> type() {
        return operand.type();
    }

    public BoundExpression getOperand() {
        return operand;
    }

    public BoundUnaryOperatorKind getOperatorKind() {
        return operatorKind;
    }


}
