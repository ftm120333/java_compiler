package codeAnalysis;

import codeAnalysis.compiling.Compilation;
import codeAnalysis.syntax.SyntaxTree;

import java.util.HashMap;
import java.util.Map;




import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

class TestOutput extends PrintStream {
    private final ByteArrayOutputStream output;

    public TestOutput() {
        super(new ByteArrayOutputStream());
        this.output = (ByteArrayOutputStream) super.out;
    }

    public String getCapturedOutput() {
        return output.toString();
    }
}


public class TestMain {

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
   

    public static void compile(String expression) throws Exception {
        Map<String, Object> variable = new HashMap<>();
        Compilation previous = null;

        // Treat the input as a single expression block
        String input = expression;

        // Parse and compile the expression
        var syntaxTree = SyntaxTree.parse(input);

        // Report syntax tree diagnostics, if any
        if (syntaxTree.getDiagnostics() != null && !syntaxTree.getDiagnostics().isEmpty()) {
            for (var diagnostic : syntaxTree.getDiagnostics()) {
                System.out.println(ANSI_RED + diagnostic.getMessage() + ANSI_RESET);
            }
            return;
        }

        // Compile and evaluate the expression
        var compilation = previous == null
                ? new Compilation(syntaxTree)
                : previous.continueWith(syntaxTree);
        previous = compilation;

        var result = compilation.evaluate(variable);

        // Print the result if evaluation succeeded
        if (result.getValue() != null) {
            System.out.println(ANSI_GREEN + "Result: " + result.getValue() + ANSI_RESET);
            System.out.println("Current Variables: " + variable);
        }

        // Report evaluation diagnostics, if any
        if (result.getDiagnostics() != null) {
            for (var diagnostic : result.getDiagnostics()) {
                System.out.println(ANSI_RED + diagnostic + ANSI_RESET);
            }
        }
    }
}
