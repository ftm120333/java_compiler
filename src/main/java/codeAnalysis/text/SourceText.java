package codeAnalysis.text;

import codeAnalysis.text.TextLine;
import codeAnalysis.text.TextSpan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class SourceText {
    private final String text;
    private final List<TextLine> lines;
    private final String fileName;

    private SourceText(String text, String fileName) {
        this.text = text;
        this.fileName = fileName;
        this.lines = parseLines(this, text);
    }



    public static SourceText from(String text ) {
        return new SourceText(text, "");
    }

    private static List<TextLine> parseLines(SourceText sourceText, String text) {
        List<TextLine> result = new ArrayList<>();
        int position = 0;
        int lineStart = 0;

        while (position < text.length()) {
            int lineBreakWidth = getLineBreakWidth(text, position);

            if (lineBreakWidth == 0) {
                position++;
            } else {
                addLine(result, sourceText, position, lineStart, lineBreakWidth);
                position += lineBreakWidth;
                lineStart = position;
            }
        }

        if (position >= lineStart) {
            addLine(result, sourceText, position, lineStart, 0);
        }

        return Collections.unmodifiableList(result);  // Make the list immutable
    }

    private static void addLine(List<TextLine> result, SourceText sourceText, int position, int lineStart, int lineBreakWidth) {
        int lineLength = position - lineStart;
        int lineLengthIncludingLineBreak = lineLength + lineBreakWidth;
        TextLine line = new TextLine(sourceText, lineStart, lineLength, lineLengthIncludingLineBreak);
        result.add(line);
    }

    private static int getLineBreakWidth(String text, int position) {
        char c = text.charAt(position);
        char l = position + 1 >= text.length() ? '\0' : text.charAt(position + 1);

        if (c == '\r' && l == '\n') {
            return 2;
        }

        if (c == '\r' || c == '\n') {
            return 1;
        }

        return 0;
    }

    public List<TextLine> getLines() {
        return lines;
    }

    public char charAt(int index) {
        return text.charAt(index);
    }

    public int length() {
        return text.length();
    }

    public String getFileName() {
        return fileName;
    }

    public int getLineIndex(int position) {
        int lower = 0;
        int upper = lines.size() - 1;

        while (lower <= upper) {
            int index = lower + (upper - lower) / 2;
            int start = lines.get(index).getStart();

            if (position == start) {
                return index;
            }

            if (start > position) {
                upper = index - 1;
            } else {
                lower = index + 1;
            }
        }

        return lower - 1;
    }

    @Override
    public String toString() {
        return text;
    }

    public String toString(int start, int length) {
        return text.substring(start, length);
    }

    public String toString(TextSpan span) {
        return toString(span.getStart(), span.getLength());
    }
}
