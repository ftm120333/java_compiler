package codeAnalysis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


import codeAnalysis.text.TextSpan;

public final class AnnotatedText {

    private final String text;
    private final List<TextSpan> spans;

    public AnnotatedText(String text, List<TextSpan> spans) {
        this.text = text;
        this.spans = spans;
    }

    public String getText() {
        return text;
    }

    public List<TextSpan> getSpans() {
    
        return spans;
    }

    public static AnnotatedText parse(String text) {
        text = unindent(text);
           //Used to construct the plain text (without brackets).
        StringBuilder textBuilder = new StringBuilder();
           //Stores the spans as they are identified.
        List<TextSpan> spanBuilder = new ArrayList<>();
           //A stack to track the start positions of spans (when encountering [).
        Stack<Integer> startStack = new Stack<>();

        int position = 0;

        for (char c : text.toCharArray()) {
            if (c == '[') {
                startStack.push(position);
            } else if (c == ']') {
                if (startStack.isEmpty()) {
                    throw new IllegalArgumentException("Too many ']' in text");
                }

                int start = startStack.pop();
                int end = position;
                TextSpan span = TextSpan.fromBounds(start, end);
                spanBuilder.add(span);
            } else {
                textBuilder.append(c);
                position++;
            }
        }

        if (!startStack.isEmpty()) {
            throw new IllegalArgumentException("Missing ']' in text");
        }
      
        return new AnnotatedText(textBuilder.toString(), spanBuilder);
    }


    //This method removes unnecessary leading spaces from each line in a multi-line string. 
    private static String unindent(String text) {
        String[] lines = unindentLines(text);

        //Join the lines into a single string separated by 
        //the system's default line separator (System.lineSeparator()).
        return String.join(System.lineSeparator(), lines);
    }


    // This method processes each line in the input
    // text to remove leading spaces while preserving relative indentation.
    public static String[] unindentLines(String text) {
        
        List<String> lines = new ArrayList<>();
          //Split text into lines
        try (BufferedReader reader = new BufferedReader(new StringReader(text))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading lines", e);
        }

        int minIndentation = Integer.MAX_VALUE;
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);

            if (line.trim().isEmpty()) {
                lines.set(i, "");
                continue;
            }

            int indentation = line.length() - line.trim().length();
            minIndentation = Math.min(minIndentation, indentation);
        }

        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).isEmpty()) {
                continue;
            }

            lines.set(i, lines.get(i).substring(minIndentation));
        }

        while (!lines.isEmpty() && lines.get(0).isEmpty()) {
            lines.remove(0);
        }

        while (!lines.isEmpty() && lines.get(lines.size() - 1).isEmpty()) {
            lines.remove(lines.size() - 1);
        }
        //it creates an empty array of type String with a size of 0. The toArray()
        // method then copies the elements from the List into the array and returns it.
        return lines.toArray(new String[0]);
    }

    
}
