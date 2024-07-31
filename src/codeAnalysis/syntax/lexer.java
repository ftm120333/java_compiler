package codeAnalysis.syntax;

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
    public SyntaxToken lex(){
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

        if (Character.isLetter(getCurrent())) {
            var start = _position;
            while (Character.isLetter(getCurrent())) {
                next();
            }
            int length = _position - start;
            String text = this._text.substring(start, length);
            var kind = SyntaxFact.getKeywordKind(text);
            return new SyntaxToken(kind, start, text, null);
        }
        switch (getCurrent()) {
            case '+' -> {
                return new SyntaxToken(SyntaxKind.PlusToken, _position++, "+", null);
            }
            case '-' -> {
                return new SyntaxToken(SyntaxKind.MinusToken, _position++, "-", null);
            }
            case '*' -> {
                return new SyntaxToken(SyntaxKind.StarToken, _position++, "*", null);
            }
            case '/' -> {
                return new SyntaxToken(SyntaxKind.SlashToken, _position++, "/", null);
            }
            case '(' -> {
                return new SyntaxToken(SyntaxKind.OpenParanthesisToken, _position++, "(", null);
            }
            case ')' -> {
                return new SyntaxToken(SyntaxKind.ClosedParanthesisToken, _position++, ")", null);
            }
            default -> {

                _diagnostics.add("Error: bad character input: " + getCurrent());
                return new SyntaxToken(SyntaxKind.BadToken, _position++, _text.substring(_position - 1, _position), null);
            }
        }
    }

    public List<String> get_diagnostics() {
        return _diagnostics;
    }
}


