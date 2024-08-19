package codeAnalysis.syntax;

public enum SyntaxKind{
    //Tokens
    BadToken,
    EndOfFileToken,
    WhitespaceToken,
    NumberToken,

    //Operators
    PlusToken,
    MinusToken,
    StarToken,
    SlashToken,
    OpenParanthesisToken,
    ClosedParanthesisToken,

    //Keywords
    TrueKeyword,
    FalseKeyword,
    IdentifierToken,

    //Expressions
    LiteralExpression,
    UnaryExpression,
    BinaryExpression,
    ParanthrsizedExpression, BangToken, EqualsToken, AmpersandAmpersandToken, PipePipeToken, EqualsEqualsToken, BangEqualsToken,

}
