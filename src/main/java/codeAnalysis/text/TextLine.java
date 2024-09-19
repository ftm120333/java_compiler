package codeAnalysis.text;

public final class TextLine {

    private final SourceText  text;
    private final int start;
    private final int length;
    public final int lengthIncludeLineBreak;

    public TextLine(SourceText  text, int start, int length, int lengthIncludeLineBreak) {
        this.text = text;
        this.start = start;
        this.length = length;
        this.lengthIncludeLineBreak = lengthIncludeLineBreak;
    }

    public SourceText getText() {
        return text;
    }

    public int getStart() {
        return start;
    }

    public int getLength() {
        return length;
    }

    public int getLengthIncludeLineBreak() {
        return lengthIncludeLineBreak;
    }

    public int end() {
        return start + length;
    }

    public TextSpan span() {
        return new TextSpan(start, length);
    }

    public TextSpan spanIncludeLineBreak() {
        return new TextSpan(start, lengthIncludeLineBreak);
    }

    public String toString( ) {
        return text.toString(span());
    }

}
