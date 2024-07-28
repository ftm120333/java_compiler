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

            token = lexer.nextToken();
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
    private SyntaxToken Peek(int offset){
        var index = _position + offset;
        if(index >= _tokens.length - 1) //If the index is out of bounds, the method returns the last token in the array
            return  _tokens[_tokens.length - 1];
        return _tokens[index];
    }

    private SyntaxToken current() {
        return Peek(0);
    } // return the token at the current position in the _tokens array.

    private SyntaxToken nextToken(){ //advances the parser's current position
        var current = current();
        _position++;
        return current;
    }
    private SyntaxToken MatchToken(SyntaxKind kind){//checks if the current token's kind matches the expected kind
        if(current().kind == kind)              // If it matches, it consumes the token and returns it.
            return nextToken();                 //If it doesn't match, it logs an error and returns a new token of the expected kind to handle the error
        _diagnostics.add("Error: Unexpected token "+ current().kind + ", expected {" + kind+ "} ");
        return new SyntaxToken(kind, current().position, null, null);

    }
    public SyntaxTree Parse(){
        var expression = ParseExpression();
        var endOfFileToken = MatchToken(SyntaxKind.EndOfFileToken);
        return new SyntaxTree(_diagnostics, expression, endOfFileToken);
    }

    public ExpressionSyntax ParseExpression(){
        return ParseTerm();
    }

    public ExpressionSyntax ParseTerm(){
        var left = ParseFactor();
        while (current().kind == SyntaxKind.PlusToken ||
                current().kind == SyntaxKind.MinusToken){
            var operatorToken = nextToken();
            var right = ParseFactor();
            left = new BinaryExpressionSyntax(left,operatorToken,right);
        }
        return left;
    }


    public ExpressionSyntax ParseFactor(){
        var left = ParsePrimaryExpresion();
        while (current().kind == SyntaxKind.StarToken ||
                current().kind == SyntaxKind.SlashToken){
            var operatorToken = nextToken();
            var right = ParsePrimaryExpresion();
            left = new BinaryExpressionSyntax(left,operatorToken,right);
        }
        return left;
    }

    private ExpressionSyntax ParsePrimaryExpresion() {
        if (current().kind == SyntaxKind.OpenParanthesisToken) {
            var left = nextToken();
            var expression = ParseExpression();
            var right = MatchToken(SyntaxKind.ClosedParanthesisToken);
            return new ParanthrsizedExpressionSyntax(left, expression, right);
        }

        var numberToken = MatchToken(SyntaxKind.NumberToken);
        return  new NumberExpressionSyntax(numberToken);
    }
}


