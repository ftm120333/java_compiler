package codeAnalysis.syntax;

import codeAnalysis.compiling.DiagnosticBag;
import codeAnalysis.text.SourceText;


import java.util.ArrayList;
import java.util.List;

class Parser {
    private final ArrayList<SyntaxToken> _tokens;
    private int _position;
    private final DiagnosticBag _diagnostics = new  DiagnosticBag();
    private final  SourceText _text;

    public Parser(SourceText text) {
        var tokens = new ArrayList<SyntaxToken>();
        var lexer = new Lexer(text);
        SyntaxToken token;
        do {

            token = lexer.lex();

            if (token.kind != SyntaxKind.WhitespaceToken
                && token.kind != SyntaxKind.BadToken) {
                tokens.add(token);

            }

        } while (token.kind != SyntaxKind.EndOfFileToken);

        _text = text;
        _tokens =  tokens;
        _diagnostics.addRange(lexer.get_diagnostics());



    }

   public  DiagnosticBag diagnosticBag() {
        return _diagnostics;
   }
 /* peek(int offset): Retrieves a token at  a specific offset from
  the current position without advancing the position.*/

    private SyntaxToken peek(int offset) {
        var index = _position + offset;
        if (index >= _tokens.size()) //If the index is out of bounds,
                                         // the method returns the last token in the array
            return _tokens.get(_tokens.size() - 1);
        return _tokens.get(index);
    }

    private SyntaxToken current() {
        return peek(0);
    } // return the token at the current position in the _tokens array.

    private SyntaxToken nextToken() { //advances the parser's current position
        var current = current();
        _position++;
        return current;
    }

    public SyntaxToken matchToken(SyntaxKind kind) {//checks if the current token's kind matches the expected kind
        if (current().kind == kind)              // If it matches, it consumes the token and returns it.
            return nextToken();                 //If it doesn't match, it logs an error and returns a new token of the expected kind to handle the error
        _diagnostics.reportUnexpectedToken( current().span(), current().kind , kind);
        return new SyntaxToken(kind, current().position, null, null);

    }

    public CompilationUnitSyntax parseCompilationUnit() {
        var statement = parseStatement();
        var endOfFileToken = matchToken(SyntaxKind.EndOfFileToken);
        return new CompilationUnitSyntax( statement, endOfFileToken);
    }

    private ExpressionSyntax parseExpression(){
         return parseAssignmentExpression();
    }

    private StatementSyntax parseStatement() {
        if (current().kind == SyntaxKind.OpenBraceToken) {
            return parseBlockStatement();

        } else if (current().kind == SyntaxKind.LetKeyword || current().kind == SyntaxKind.VarKeyword) {
            return parseVariableDeclaration();
            
        } else {
            return parseExpressionStatement();
        }
    }

    private StatementSyntax parseVariableDeclaration() {
        var expected = current().kind == SyntaxKind.LetKeyword ? SyntaxKind.LetKeyword : SyntaxKind.VarKeyword;
        var keyword = matchToken(expected);
        var identifier = matchToken(SyntaxKind.IdentifierToken);
        var equalsToken = matchToken(SyntaxKind.EqualsToken);
        var initializer = parseExpression();
        return new VariableDeclarationSyntax(keyword, identifier, equalsToken, initializer);
    }

    private ExpressionStatementSyntax parseExpressionStatement() {
        var expression = parseExpression();
        return new ExpressionStatementSyntax(expression);
    }


    private BlockStatementSyntax parseBlockStatement() {
        var statements = new ArrayList<StatementSyntax>();
        var openBraceToken = matchToken(SyntaxKind.OpenBraceToken);

        while (current().kind != SyntaxKind.EndOfFileToken &&
                current().kind != SyntaxKind.CloseBraceToken) {
            statements.add(parseStatement());
        }

        var closeBraceToken = matchToken(SyntaxKind.CloseBraceToken);
        return new BlockStatementSyntax(openBraceToken, List.copyOf(statements), closeBraceToken);
    }


    private ExpressionSyntax parseAssignmentExpression(){

        if (peek(0).kind == SyntaxKind.IdentifierToken &&
            peek(1).kind == SyntaxKind.EqualsToken){
                var identifierToken = nextToken();
                var operatorToken = nextToken();
                var right = parseAssignmentExpression();
                return  new AssignmentExpressionSyntax(identifierToken, operatorToken, right );
        }
        return parseBinaryExpression(0);
    }
    private ExpressionSyntax parseBinaryExpression(int parentPrecedence) {

        ExpressionSyntax left;
        var unaryOperatorPrecedence =  SyntaxFact.getUnaryOperatorPrecedence(current().kind);
        if (unaryOperatorPrecedence != 0 && unaryOperatorPrecedence >= parentPrecedence) {
            var operatorToken = nextToken();
            var operand = parseBinaryExpression(unaryOperatorPrecedence);
            left = new UnaryExpressionSyntax(operatorToken, operand);
        } else {
            left = ParsePrimaryExpression();
        }

        while (true) {
            var precedence = SyntaxFact.getBinaryOperatorPrecedence(current().kind);
            if (precedence == 0 || precedence <= parentPrecedence) {
                break;
            }
            var operatorToken = nextToken();
            var right = parseBinaryExpression(precedence);
            left = new BinaryExpressionSyntax(left, operatorToken, right);

        }
        return left;
    }

    private ExpressionSyntax ParsePrimaryExpression() {
        switch (current().kind) {
            case OpenParenthesisToken -> {
                return parseParenthesizedExpression();
            }
            case FalseKeyword, TrueKeyword -> {
                return parseBooleanLiteral();
            }
            case NumberToken -> {
                return parseNumberLiteral();
            }
            case IdentifierToken -> {
              return parseNamExpression();
            }
            default -> {
                return parseNamExpression();
            }
        }

    }

    private ExpressionSyntax parseNamExpression() {
        var identifierToken = matchToken(SyntaxKind.IdentifierToken);
        return new NameExpressionSyntax(identifierToken);
    }

    private ExpressionSyntax parseNumberLiteral() {
        var numberToken = matchToken(SyntaxKind.NumberToken);
        return  new LiteralExpressionSyntax(numberToken);
    }

    private ExpressionSyntax parseBooleanLiteral() {
        var isTrue = current().kind == SyntaxKind.TrueKeyword;
        var keyword = isTrue? matchToken(SyntaxKind.TrueKeyword): matchToken(SyntaxKind.FalseKeyword);
        return new LiteralExpressionSyntax(keyword, isTrue);
    }

    private ExpressionSyntax parseParenthesizedExpression() {
        var left = matchToken(SyntaxKind.OpenParenthesisToken);
        var expression = parseExpression();
        var right = matchToken(SyntaxKind.ClosedParenthesisToken);
        return new ParanthrsizedExpressionSyntax(left, expression, right);
    }


}


