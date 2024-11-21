package codeAnalysis.binding;

public enum BoundNodeKind {

    //Expressions
    ParenthesizedExpression,
    LiteralExpression,
    VariableExpression,
    AssignmentExpression,
    BinaryExpression,
    UnaryExpression,


    //statements
    BlockStatement,
    ExpressionStatement,
    VariableDeclaration,

}
