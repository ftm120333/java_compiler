package codeAnalysis.binding;

import codeAnalysis.syntax.SyntaxKind;

public final class BoundUnaryOperator {

    private final SyntaxKind syntaxKind;
    private final BoundUnaryOperatorKind kind;
    private final Class<?> operandType;
    private final Class<?> resultType;

    private BoundUnaryOperator(SyntaxKind syntaxKind, BoundUnaryOperatorKind kind, Class<?> operandType) {
        this(syntaxKind, kind, operandType, operandType);
    }

    private BoundUnaryOperator(SyntaxKind syntaxKind, BoundUnaryOperatorKind kind, Class<?> operandType, Class<?> resultType) {
        this.syntaxKind = syntaxKind;
        this.kind = kind;
        this.operandType = operandType;
        this.resultType = resultType;
    }

    public SyntaxKind getSyntaxKind() {
        return syntaxKind;
    }

    public BoundUnaryOperatorKind getKind() {
        return kind;
    }

    public Class<?> getOperandType() {
        return operandType;
    }

    public Class<?> getResultType() {
        return resultType;
    }

    private static final BoundUnaryOperator[] OPERATORS = {
            new BoundUnaryOperator(SyntaxKind.BangToken, BoundUnaryOperatorKind.LogicalNegation, Boolean.class),
            new BoundUnaryOperator(SyntaxKind.PlusToken, BoundUnaryOperatorKind.Identity, Integer.class),
            new BoundUnaryOperator(SyntaxKind.MinusToken, BoundUnaryOperatorKind.Negation, Integer.class),
    };

    public static BoundUnaryOperator bind(SyntaxKind syntaxKind, Class<?> operandType) {
        for (BoundUnaryOperator op : OPERATORS) {
            if (op.syntaxKind == syntaxKind && op.operandType.equals(operandType)) {
                return op;
            }
        }
        return null;
    }
}














