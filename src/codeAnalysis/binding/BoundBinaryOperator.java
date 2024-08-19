package codeAnalysis.binding;

import codeAnalysis.syntax.SyntaxKind;

public final class BoundBinaryOperator {

    private final SyntaxKind syntaxKind;

    private final BoundBinaryOperatorKind kind;
    private final Class<?> leftType;
    private final Class<?> rightType;
    private final Class<?> resultType;


    private BoundBinaryOperator(SyntaxKind syntaxKind, BoundBinaryOperatorKind kind, Class<?> type) {
        this(syntaxKind, kind,type,type,type);
    }

    private BoundBinaryOperator(SyntaxKind syntaxKind, BoundBinaryOperatorKind kind,
                                Class<?> leftType,Class<?> rightType, Class<?> resultType) {
        this.syntaxKind = syntaxKind;
        this.kind = kind;
        this.leftType = leftType;
        this.rightType = rightType;
        this.resultType = resultType;
    }


    public SyntaxKind getSyntaxKind() {
        return syntaxKind;
    }

    public BoundBinaryOperatorKind getKind() {
        return kind;
    }

    public Class<?> getLeftType() {
        return leftType;
    }

    public Class<?> getRightType() {
        return rightType;
    }

    public Class<?> getResultType() {
        return resultType;
    }

    private static final BoundBinaryOperator[] operators = {
            new BoundBinaryOperator(SyntaxKind.PlusToken, BoundBinaryOperatorKind.Addition, int.class),
            new BoundBinaryOperator(SyntaxKind.MinusToken, BoundBinaryOperatorKind.Subtraction, int.class),
            new BoundBinaryOperator(SyntaxKind.StarToken, BoundBinaryOperatorKind.Multiplication, int.class),
            new BoundBinaryOperator(SyntaxKind.SlashToken, BoundBinaryOperatorKind.Division, int.class),

            new BoundBinaryOperator(SyntaxKind.AmpersandAmpersandToken, BoundBinaryOperatorKind.LogicalAnd, boolean.class),
            new BoundBinaryOperator(SyntaxKind.PipePipeToken, BoundBinaryOperatorKind.LogicalOr, boolean.class),

    };

    public static BoundBinaryOperator bind(SyntaxKind syntaxKind, Class<?> leftType, Class<?> rightType) {
        for (BoundBinaryOperator op : operators) {
            if (op.syntaxKind == syntaxKind && op.leftType == leftType && op.rightType == rightType) {
                return op;
            }
        }
        return null;
    }
}