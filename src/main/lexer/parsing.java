package main.lexer;

import java.util.ArrayList;
import java.util.List;

class Parser {
    private  SyntaxToken[] _tokens;
    private int _position;
    private List<String> _diagnostics = new ArrayList<>();

    public Parser(String text){
        var tokens = new ArrayList<SyntaxToken>();
        var lexer = new Lexer(text);
        SyntaxToken token;
        do{

            token = lexer.lex();
            if(token.kind != SyntaxKind.WhitespaceToken && token.kind != SyntaxKind.BadToken ){
                tokens.add(token);

            }

        } while (token.kind != SyntaxKind.EndOfFileToken);


        _tokens = tokens.toArray(new SyntaxToken[0]);

        _diagnostics.addAll(lexer.get_diagnostics());


    }

    public  List<String> Diognostics(){
        return  _diagnostics;
    }
    private SyntaxToken peek(int offset){
        var index = _position + offset;
        if(index >= _tokens.length - 1) //If the index is out of bounds, the method returns the last token in the array
            return  _tokens[_tokens.length - 1];
        return _tokens[index];
    }

    private SyntaxToken current() {
        return peek(0);
    } // return the token at the current position in the _tokens array.

    private SyntaxToken nextToken(){ //advances the parser's current position
        var current = current();
        _position++;
        return current;
    }
    private SyntaxToken matchToken(SyntaxKind kind){//checks if the current token's kind matches the expected kind
        if(current().kind == kind)              // If it matches, it consumes the token and returns it.
            return nextToken();                 //If it doesn't match, it logs an error and returns a new token of the expected kind to handle the error
        _diagnostics.add("Error: Unexpected token "+ current().kind + ", expected {" + kind+ "} ");
        return new SyntaxToken(kind, current().position, null, null);

    }
    public SyntaxTree parse(){
        var expression = parseExpression(0);
        var endOfFileToken = matchToken(SyntaxKind.EndOfFileToken);
        return new SyntaxTree(_diagnostics, expression, endOfFileToken);
    }


    private ExpressionSyntax parseExpression(int parentPrecedence ) {
        var left = ParsePrimaryExpresion();

        while (true) {
            var precedence = getBinaryOperatorPrecedence(current().kind);
            if (precedence == 0 || precedence <= parentPrecedence) {
                break;
            }
            var operatorToken = nextToken();
            var right = parseExpression(precedence);
            left = new BinaryExpressionSyntax(left, operatorToken, right);
        }
        return left;
        }

    private static int getBinaryOperatorPrecedence(SyntaxKind kind) {
        return switch (kind) {
            case SyntaxKind.PlusToken, SyntaxKind.MinusToken -> 1;
            case SyntaxKind.StarToken, SyntaxKind.SlashToken -> 2;
            default -> 0;
        };
    }




    private ExpressionSyntax ParsePrimaryExpresion() {
        if (current().kind == SyntaxKind.OpenParanthesisToken) {
            var left = nextToken();
            var expression = parseExpression(0);
            var right = matchToken(SyntaxKind.ClosedParanthesisToken);
            return new ParanthrsizedExpressionSyntax(left, expression, right);
        }

        var numberToken = matchToken(SyntaxKind.NumberToken);
        return  new NumberExpressionSyntax(numberToken);
    }
}


