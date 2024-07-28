package main.lexer;

public enum SyntaxKind{
    WhitespaceToken,
    PlusToken,
    MinusToken,
    StarToken,
    SlashToken,
    OpenParanthesisToken,
    ClosedParanthesisToken,
    BadToken,
    EndOfFileToken,
    NumberExpression,
    BinaryExpression,
    ParanthrsizedExpression, NumberToken
}
