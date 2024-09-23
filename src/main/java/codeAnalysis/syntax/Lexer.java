package codeAnalysis.syntax;

import codeAnalysis.compiling.DiagnosticBag;
import codeAnalysis.text.SourceText;
import codeAnalysis.text.TextSpan;

public class Lexer {
    private final SourceText _text;
    private int _position;
    private int _start;
    private SyntaxKind _kind;
    private Object _value;

    private final DiagnosticBag _diagnostics = new DiagnosticBag();



    public Lexer(SourceText text){
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
            return '\0';
        return _text.charAt(index);
    }

    private void next(){
        _position++;
    }
    public SyntaxToken lex() {


        _start = _position;

            switch (getCurrent()) {

                case '\0' ->
                    _kind = SyntaxKind.EndOfFileToken;
                case '+' -> {
                    _kind = SyntaxKind.PlusToken;
                    _position++;
                }
                case '-' -> {
                    _kind = SyntaxKind.MinusToken;
                    _position++;
                }
                case '*' -> {
                    _kind = SyntaxKind.StarToken;
                    _position++;
                }
                case '/' -> {
                    _kind = SyntaxKind.SlashToken;
                    _position++;
                }
                case '(' -> {
                    _kind = SyntaxKind.OpenParanthesisToken;
                    _position++;
                }

                case ')' -> {
                    _kind = SyntaxKind.ClosedParanthesisToken;
                    _position++;
                }

                case '&' -> {
                    if (lookAhead() == '&') {
                        _position += 2;
                        _kind = SyntaxKind.AmpersandAmpersandToken;
                    }

                }
                case '|' -> {
                    if (lookAhead() == '|') {
                        _position += 2;
                        _kind = SyntaxKind.PipePipeToken;
                    }

                }
                case '=' -> {
                    if (lookAhead() != '=') {
                        _kind = SyntaxKind.EqualsToken;
                        _position++;
                    } else {
                        _position += 2;
                        _kind = SyntaxKind.EqualsEqualsToken;
                    }
                }
                case '!' -> {
                    if (lookAhead() != '=') {
                        _kind = SyntaxKind.BangToken;
                        _position++;
                    } else {
                        _kind = SyntaxKind.BangEqualsToken;
                        _position += 2;
                    }
                }
                case '0', '1', '2', '3', '4', '5' , '6', '7','8','9'-> readNumberToken();
                case ' ', '\r', '\t' ->  readWhiteSpace();
                default -> {
                if (Character.isLetter(getCurrent())) {
                        readIdentifierOrKeyword();
                }else if(Character.isWhitespace(getCurrent())){
                    readWhiteSpace();
                }else{
                        _diagnostics.reportBadCharacter(_position, getCurrent());
                        _position++;
                    }

                }}

    var length = _position - _start;
    var text = SyntaxFact.getText(_kind);
    if(text == null)
        text = _text.toString(_start,_position);

    return new SyntaxToken(_kind, _start, (String) text, _value);


    }


    private void readWhiteSpace() {
        while (Character.isWhitespace(getCurrent()))
            next();

      _kind = SyntaxKind.WhitespaceToken;
    }

    public void readNumberToken() {
        while (Character.isDigit(getCurrent()))
            next();

        String text = this._text.toString(_start, _position);
        try {
            int value = Integer.parseInt(text);
            _value = value;
        } catch (NumberFormatException e) {
            _diagnostics.reportInvalidNumber(new TextSpan(_start, text.length()), text, Integer.class);
        }
        _kind = SyntaxKind.NumberToken;
    }

    private void readIdentifierOrKeyword() {
        while (Character.isLetter(getCurrent()))
            next();


        String text = this._text.toString(_start, _position);
        _kind = SyntaxFact.getKeywordKind(text);

    }

    public SourceText get_text() {
        return _text;
    }


    public DiagnosticBag get_diagnostics() {
        return _diagnostics;
    }
}


