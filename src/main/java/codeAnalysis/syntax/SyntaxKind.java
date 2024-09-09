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
    NameExpression, AssignmentExpression, PlusEqualsToken, MinusEqualsToken,
    StarEqualsToken,
    SlashEqualsToken,
    TildeToken, LessToken,
    LessOrEqualsToken,
    GreaterToken,
    GreaterOrEqualsToken,
    AmpersandToken,
    AmpersandEqualsToken,
    PipeToken, WhileKeyword,
    DoKeyword, VarKeyword,
    ToKeyword, ReturnKeyword, LetKeyword,
    IfKeyword, FunctionKeyword, ForKeyword,
    ElseKeyword, ContinueKeyword, BreakKeyword,
    CommaToken, ColonToken, CloseBraceToken,
    OpenBraceToken, CloseParenthesisToken,
    OpenParenthesisToken, HatEqualsToken,
    HatToken, PipeEqualsToken,


}
