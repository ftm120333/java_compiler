package codeAnalysis.syntax;

import codeAnalysis.compiling.DiagnosticBag;
import codeAnalysis.compiling.TextSpan;

public class Lexer {
    private final String _text;
    private int _position;
    private final DiagnosticBag _diagnostics = new DiagnosticBag();



    public Lexer(String text){
        _text = text;
    }

    private char getCurrent(){
        if (_position >= _text.length()) {
            return '\0';
        }
        return _text.charAt(_position);
    }
    private char current() {
        return peek(0);
    }
    private char lookAhead() { return peek(1);
    }

    private char peek(int offset){
        var index = _position + offset;
        if (index >= _text.length())
            return '\n';
        return _text.charAt(index);
    }

    private void next(){
        _position++;
    }
    public SyntaxToken lex(){
        if (_position >= _text.length())
            return new SyntaxToken(SyntaxKind.EndOfFileToken, _position,"\n", null);
        var start = _position;
        if (Character.isDigit(getCurrent())){

            while (Character.isDigit(getCurrent())) {
                next();
            }
            int length = _position - start;
            String text = this._text.substring(start , start + length);
            int value = Integer.parseInt(text);
            if(!Integer.valueOf(text).equals(value))
                _diagnostics.reportInvalidNumber(new TextSpan(start, length), text, Integer.class);
            return new SyntaxToken(SyntaxKind.NumberToken, start, text, value);
        }

        if (Character.isWhitespace(getCurrent())){

            while (Character.isWhitespace(getCurrent())) {
                next();
            }
            int length = _position - start;
            String text = this._text.substring(start,start + length);
            return new SyntaxToken(SyntaxKind.WhitespaceToken, start, text, null);
        }

        if (Character.isLetter(getCurrent())) {

            while (Character.isLetter(getCurrent())) {
                next();
            }
            int length = _position - start;
            String text = this._text.substring(start,start +  length);
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

            case '&' -> {
                if (lookAhead() == '&') {
                    _position += 2;
                    return new SyntaxToken(SyntaxKind.AmpersandAmpersandToken, start, "&&", null);
                }

            }
            case '|' -> {
                if (lookAhead() == '|') {
                    _position += 2;
                    return new SyntaxToken(SyntaxKind.PipePipeToken,start, "||", null);
                }

            }
            case '=' -> {
                if (lookAhead() == '=') {
                    _position += 2;
                    return new SyntaxToken(SyntaxKind.EqualsEqualsToken, start, "==", null);
                }
                else{
                    _position += 1;
                    return new SyntaxToken(SyntaxKind.EqualsToken, start, "=", null);
                }
            }
            case '!' -> {
                if (lookAhead() == '=') {
                    _position += 2;
                    return new SyntaxToken(SyntaxKind.BangEqualsToken, start, "!=", null);
                }
                else
                {
                    _position +=1;
                    return new SyntaxToken(SyntaxKind.BangToken, start, "!", null);
                }
            }

            default -> {

                _diagnostics.reportBadCharacter(_position, getCurrent());
                return new SyntaxToken(SyntaxKind.BadToken, _position++, _text.substring(_position - 1, _position), null);
            }

        }
        return new SyntaxToken(SyntaxKind.BadToken, _position++, _text.substring(_position - 1, _position), null);
    }

    public String get_text() {
        return _text;
    }

    public int get_position() {
        return _position;
    }

    public DiagnosticBag get_diagnostics() {
        return _diagnostics;
    }
}


