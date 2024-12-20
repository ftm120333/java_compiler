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

    ClosedParenthesisToken,
    BangToken, EqualsToken,
    AmpersandAmpersandToken,
    PipePipeToken,
    EqualsEqualsToken,
    BangEqualsToken,
    IdentifierToken,


    //Nodes
    CompilationUnit,

    //Expressions
    LiteralExpression,
    UnaryExpression,
    BinaryExpression,
    ParanthesizedExpression,
    NameExpression,
    AssignmentExpression,
    PlusEqualsToken,
    MinusEqualsToken,
    StarEqualsToken,
    SlashEqualsToken,
    TildeToken,
    LessToken,
    LessOrEqualsToken,
    GreaterToken,
    GreaterOrEqualsToken,
    AmpersandToken,
    AmpersandEqualsToken,
    PipeToken,
    CommaToken,
    ColonToken,
    CloseBraceToken,
    CloseParenthesisToken,
    OpenBraceToken,
    OpenParenthesisToken,
    HatEqualsToken,
    HatToken,
    PipeEqualsToken,


    //statements
    BlockStatement,
    ExpressionStatement,
    VariableDeclaration,


    //Keywords
    TrueKeyword,
    FalseKeyword,
    WhileKeyword,
    DoKeyword,
    VarKeyword,
    ToKeyword,
    ReturnKeyword,
    LetKeyword,
    IfKeyword,
    FunctionKeyword,
    ForKeyword,
    ElseKeyword,
    ContinueKeyword,
    BreakKeyword,

}
