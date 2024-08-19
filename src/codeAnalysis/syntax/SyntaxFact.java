package codeAnalysis.syntax;

public final class SyntaxFact {

    private SyntaxFact() {
        // Private constructor to prevent instantiation
    }

// -1 * 3
//    -
//    |
//    *
//  /   \
// 1     3

    //  or

//  1 * 3

//
//    *
//  /   \
// -     3
// |
// 1
    public static int getUnaryOperatorPrecedence( SyntaxKind kind) {
        return switch (kind) {
            case SyntaxKind.PlusToken, SyntaxKind.MinusToken, SyntaxKind.BangToken-> 5;
            default -> 0;
        };
    }

    public static int getBinaryOperatorPrecedence( SyntaxKind kind) {
        return switch (kind) {
            case SyntaxKind.PlusToken, SyntaxKind.MinusToken -> 4;
            case SyntaxKind.StarToken, SyntaxKind.SlashToken -> 3;
            case SyntaxKind.AmpersandAmpersandToken -> 2;
                case SyntaxKind.PipePipeToken -> 1;
            default -> 0;
        };
}

    public static SyntaxKind getKeywordKind(String text) {

        switch (text) {
            case "true" -> {
                return SyntaxKind.TrueKeyword;
            }
            case "false" -> {
                return SyntaxKind.FalseKeyword;
            }
//            case "if" -> {
//                return SyntaxKind.IfKeyword;
//            }
//            case "else" -> {
//                return SyntaxKind.ElseKeyword;
//            }
//            case "while" -> {
//                return SyntaxKind.WhileKeyword;
//            }
//            case "do" -> {
//                return SyntaxKind.DoKeyword;
//            }
//            case "for" -> {
//
//    }
            default -> {
                return SyntaxKind.IdentifierToken;
            }
        }
    }
}
