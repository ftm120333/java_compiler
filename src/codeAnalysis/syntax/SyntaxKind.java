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


    //Expressions
    LiteralExpression,
    UnaryExpression,
    BinaryExpression,
    ParanthrsizedExpression,
}