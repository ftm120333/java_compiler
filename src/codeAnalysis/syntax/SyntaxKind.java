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
    BangToken, EqualsToken,
    AmpersandAmpersandToken,
    PipePipeToken,
    EqualsEqualsToken,
    BangEqualsToken,
    IdentifierToken,
    //Keywords
    TrueKeyword,
    FalseKeyword,


    //Expressions
    LiteralExpression,
    UnaryExpression,
    BinaryExpression,
    ParanthesizedExpression,
    NameExpression, AssignmentExpression,

}
