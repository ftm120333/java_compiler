package main.lexer;

import java.util.ArrayList;
import java.util.List;

class Lexer {
    private String _text;
    private int _position;
    private List<String> _diagnostics = new ArrayList<>();


    public Lexer(String text){
        _text = text;
    }

    private char getCurrent(){
        if (_position >= _text.length()) {
            return '\n';
        }
        return _text.charAt(_position);
    }
    private void next(){
        _position++;
    }
    public SyntaxToken nextToken(){
        if (_position >= _text.length())
            return new SyntaxToken(SyntaxKind.EndOfFileToken, _position,"\n", null);

        if (Character.isDigit(getCurrent())){
            var start = _position;
            while (Character.isDigit(getCurrent())) {
                next();
            }
            int length = _position - start;
            String text = this._text.substring(start , start + length);
            int value = Integer.parseInt(text);
            if(!Integer.valueOf(text).equals(value))
                _diagnostics.add("The number " + text + " is not a valid integer.");
            return new SyntaxToken(SyntaxKind.NumberToken, start, text, value);
        }

        if (Character.isWhitespace(getCurrent())){
            var start = _position;
            while (Character.isWhitespace(getCurrent())) {
                next();
            }
            int length = _position - start;
            String text = this._text.substring(start,start + length);
            return new SyntaxToken(SyntaxKind.WhitespaceToken, start, text, null);
        }

        if (getCurrent()== '+')
            return new SyntaxToken(SyntaxKind.PlusToken, _position++, "+",null);
        else if (getCurrent()== '-')
            return new SyntaxToken(SyntaxKind.MinusToken, _position++, "-",null);
        else if (getCurrent()== '*')
            return new SyntaxToken(SyntaxKind.StarToken, _position++, "*",null);
        else if (getCurrent()== '/')
            return new SyntaxToken(SyntaxKind.SlashToken, _position++, "/",null);
        else if (getCurrent()== '(')
            return new SyntaxToken(SyntaxKind.OpenParanthesisToken, _position++, "(",null);
        else if (getCurrent()== ')')
            return new SyntaxToken(SyntaxKind.ClosedParanthesisToken, _position++, ")",null);
        else{

            _diagnostics.add("Error: bad character input: " + getCurrent());
            return new SyntaxToken(SyntaxKind.BadToken, _position++, _text.substring(_position -1, _position),null);
        }
    }

    public List<String> get_diagnostics() {
        return _diagnostics;
    }
}
enum SyntaxKind{
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
class SyntaxToken extends SyntaxNode{
    public final SyntaxKind kind;
    public final int position;
    public final String text;
    public final Object value;

    public SyntaxToken(SyntaxKind kind, int position, String text, Object value){

        this.kind = kind;
        this.position = position;
        this.text = text;
        this.value = value;
    }

    @Override
    public SyntaxKind getKind() {
        return kind;
    }

    @Override
    public List<SyntaxNode> GetChildren() {
        return new ArrayList<SyntaxNode>();
    }
}

