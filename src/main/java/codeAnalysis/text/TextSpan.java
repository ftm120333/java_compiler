package codeAnalysis.text;

import java.util.Objects;

public class TextSpan {
    private final int start;
    private final int length;
    public TextSpan(int start, int length){

        this.start = start;
        this.length = length;
    }

    public int getStart() {
        return start;
    }

    public int getLength() {
        return length;
    }
    public int end() {
        return start + length;
    }


    public static TextSpan fromBounds(int start, int end)
    {
        var length = end - start;
        return new TextSpan(start, length);
    }
  

    @Override
    public String toString() {

        return "TextSpan [start=" + start + ", length=" + length + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        TextSpan textSpan = (TextSpan) obj;
        return start == textSpan.start && length == textSpan.length;
    }

    @Override
    public int hashCode() {
    
        return Objects.hash(start, length);
    }

}

