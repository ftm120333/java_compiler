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
            case SyntaxKind.PlusToken, SyntaxKind.MinusToken -> 3;
            default -> 0;
        };
    }

    public static int getBinaryOperatorPrecedence( SyntaxKind kind) {
        return switch (kind) {
            case SyntaxKind.PlusToken, SyntaxKind.MinusToken -> 1;
            case SyntaxKind.StarToken, SyntaxKind.SlashToken -> 2;
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
