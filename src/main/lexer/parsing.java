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
        System.out.println(_diagnostics);

    }

    public  Iterable<String> Diognostics(){
        return  _diagnostics;
    }
    private SyntaxToken Peek(int offset){
        var index = _position + offset;
        if(index >= _tokens.length - 1)
            return  _tokens[_tokens.length - 1];
        return _tokens[index];
    }
    private SyntaxToken current() {
        return Peek(0);
    }

    private SyntaxToken nextToken(){
        var current = current();
        _position++;
        return current;
    }
    private SyntaxToken Match(SyntaxKind kind){
        if(current().kind == kind)
            return nextToken();
        _diagnostics.add("Error: Unexpected token "+ current().kind + ", expected {" + kind+ "} ");
        return new SyntaxToken(kind, current().position, null, null);

    }

    public ExpressionSyntax Parse(){
        var left = ParsePrimaryExpresion();
        while (current().kind == SyntaxKind.PlusToken ||
               current().kind == SyntaxKind.MinusToken){
            var operatorToken = nextToken();
            var right = ParsePrimaryExpresion();
            left = new BinaryExpressionSyntax(left,operatorToken,right);
        }
        return left;
    }

    private ExpressionSyntax ParsePrimaryExpresion() {
      var numberToken = Match(SyntaxKind.NumberToken);
      return  new NumberExpresionSyntax(numberToken);
    }
}
