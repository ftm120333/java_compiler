package codeAnalysis.compiling;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
public class TextSpan {

    private static int start;
    private static int length;

    public TextSpan(int start, int length) {
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
}


