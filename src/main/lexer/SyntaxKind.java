package main.lexer;

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
    NumberExpression,
    UnaryExpression,
    BinaryExpression,
    ParanthrsizedExpression,
}
