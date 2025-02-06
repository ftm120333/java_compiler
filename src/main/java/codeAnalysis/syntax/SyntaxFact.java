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
            case SyntaxKind.PlusToken, SyntaxKind.MinusToken, SyntaxKind.BangToken-> 6;
            default -> 0;
        };
    }

    public static int getBinaryOperatorPrecedence( SyntaxKind kind) {
        return switch (kind) {
            case SyntaxKind.StarToken, SyntaxKind.SlashToken -> 5;

            case SyntaxKind.PlusToken, SyntaxKind.MinusToken -> 4; 

            case SyntaxKind.EqualsEqualsToken, SyntaxKind.BangEqualsToken,
             SyntaxKind.LessToken, SyntaxKind.LessOrEqualsToken, 
             SyntaxKind.GreaterToken, SyntaxKind.GreaterOrEqualsToken -> 3;
             
            case SyntaxKind.AmpersandAmpersandToken -> 2;
                case SyntaxKind.PipePipeToken -> 1;
            default -> 0;
        };
}

    public static SyntaxKind getKeywordKind(String text) {

        switch (text) {
            case "false" -> {
                return SyntaxKind.FalseKeyword;
            }
            case "let" -> {
                return SyntaxKind.LetKeyword;
            }
            case "true" -> {
                return SyntaxKind.TrueKeyword;
            }
            case "var" -> {
                return SyntaxKind.VarKeyword;
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

    public static Object getText(SyntaxKind kind) {
        switch (kind)
        {
            case SyntaxKind.PlusToken:
                return "+";
            case SyntaxKind.PlusEqualsToken:
                return "+=";
            case SyntaxKind.MinusToken:
                return "-";
            case SyntaxKind.MinusEqualsToken:
                return "-=";
            case SyntaxKind.StarToken:
                return "*";
            case SyntaxKind.StarEqualsToken:
                return "*=";
            case SyntaxKind.SlashToken:
                return "/";
            case SyntaxKind.SlashEqualsToken:
                return "/=";
            case SyntaxKind.BangToken:
                return "!";
            case SyntaxKind.EqualsToken:
                return "=";
            case SyntaxKind.TildeToken:
                return "~";
            case SyntaxKind.LessToken:
                return "<";
            case SyntaxKind.LessOrEqualsToken:
                return "<=";
            case SyntaxKind.GreaterToken:
                return ">";
            case SyntaxKind.GreaterOrEqualsToken:
                return ">=";
            case SyntaxKind.AmpersandToken:
                return "&";
            case SyntaxKind.AmpersandAmpersandToken:
                return "&&";
            case SyntaxKind.AmpersandEqualsToken:
                return "&=";
            case SyntaxKind.PipeToken:
                return "|";
            case SyntaxKind.PipeEqualsToken:
                return "|=";
            case SyntaxKind.PipePipeToken:
                return "||";
            case SyntaxKind.HatToken:
                return "^";
            case SyntaxKind.HatEqualsToken:
                return "^=";
            case SyntaxKind.EqualsEqualsToken:
                return "==";
            case SyntaxKind.BangEqualsToken:
                return "!=";
            case SyntaxKind.OpenParenthesisToken:
                return "(";
            case SyntaxKind.CloseParenthesisToken:
                return ")";
            case SyntaxKind.OpenBraceToken:
                return "{";
            case SyntaxKind.CloseBraceToken:
                return "}";
            case SyntaxKind.ColonToken:
                return ":";
            case SyntaxKind.CommaToken:
                return ",";
            case SyntaxKind.BreakKeyword:
                return "break";
            case SyntaxKind.ContinueKeyword:
                return "continue";
            case SyntaxKind.ElseKeyword:
                return "else";
            case SyntaxKind.FalseKeyword:
                return "false";
            case SyntaxKind.ForKeyword:
                return "for";
            case SyntaxKind.FunctionKeyword:
                return "function";
            case SyntaxKind.IfKeyword:
                return "if";
            case SyntaxKind.LetKeyword:
                return "let";
            case SyntaxKind.ReturnKeyword:
                return "return";
            case SyntaxKind.ToKeyword:
                return "to";
            case SyntaxKind.TrueKeyword:
                return "true";
            case SyntaxKind.VarKeyword:
                return "var";
            case SyntaxKind.WhileKeyword:
                return "while";
            case SyntaxKind.DoKeyword:
                return "do";
            default:
                return null;
        }
    }
}
