package codeAnalysis;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class TestOutput extends PrintStream {
    private final ByteArrayOutputStream output;

    public TestOutput() {
        super(new ByteArrayOutputStream());
        this.output = (ByteArrayOutputStream) super.out;
    }

    public String getCapturedOutput() {
        return output.toString();
    }
}