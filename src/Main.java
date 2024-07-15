


class Lexer {
    private String _text;
    private int _position;

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
        if (Character.isDigit(getCurrent())){
            var start = _position;
            while (Character.isDigit(getCurrent())) {
                next();
            }
            int length = _position - start;
            String text = this._text.substring(start, start + length);
            int value = Integer.parseInt(text);
            return new SyntaxToken(SyntaxKind.NumberToken, start, text);
        }
        return null;  // Return null or handle other token types as needed
    }
}
enum SyntaxKind{
    NumberToken
}
class SyntaxToken {
    private final SyntaxKind kind;
    private final int position;
    private final String text;

    public SyntaxToken(SyntaxKind kind, int position, String text){

        this.kind = kind;
        this.position = position;
        this.text = text;
    }

    public SyntaxKind getKind() {
        return kind;
    }

    public int getPosition() {
        return position;
    }

    public String getText() {
        return text;
    }
}

public class Main {
    public static void main(String[] args) {

        System.out.printf("Hello and welcome!");

        for (int i = 1; i <= 5; i++) {

            System.out.println("i = " + i);
        }
    }
}