


package codeAnalysis;

import codeAnalysis.syntax.SyntaxNode;
import codeAnalysis.syntax.SyntaxToken;
import codeAnalysis.syntax.SyntaxTree;
import codeAnalysis.compiling.Compilation;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    // ANSI escape codes for colored output
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_BLUE = "\u001B[34m";

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        boolean showTree = false;
        Map<String, Object> variable = new HashMap<>();
        var textBuilder = new StringBuilder();
        Compilation previous = null;

        while (true) {
            if (textBuilder.isEmpty())
                System.out.print(ANSI_BLUE + "» " + ANSI_RESET);
            else
                System.out.print("· ");


                var input = scanner.nextLine();


            boolean isBlank = input == null || input.isBlank();

            if (textBuilder.isEmpty()) {

                if (input.equals("#showTree")) {
                    showTree = !showTree;
                    System.out.println(showTree ? "Showing parse tree." : "Not showing parse tree.");
                    continue;
                } else if (input.equals("#cls")) {
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    continue;
                } else if (input.equals("#reset")) {
                    previous = null;
                    continue;
                } else if (input.isBlank()) {

                    break; // Exit the loop on a blank line if no input was provided before
                }
            }


            // Run the code if the user enters a blank line after adding input
            if (input.isBlank()) {

                var text = textBuilder.toString().trim();;

                textBuilder.setLength(0); // Clear the text builder for the next input block
                var syntaxTree = SyntaxTree.parse(text);

                if (syntaxTree.getDiagnostics() != null && !syntaxTree.getDiagnostics().isEmpty()) {
                    for (var diagnostic : syntaxTree.getDiagnostics()) {
                        System.out.println(ANSI_RED + diagnostic.getMessage() + ANSI_RESET);
                    }
                    continue;
                }

                var compilation = previous == null
                        ? new Compilation(syntaxTree)
                        : previous.continueWith(syntaxTree);
                previous = compilation;

                var result = compilation.evaluate(variable);
                System.out.println("result: " + result.getValue());
                if(result.getValue() != null)
                   System.out.println("result type : " + result.getValue().getClass());

                if (showTree) {
                    PrettyPrint(syntaxTree.getRoot(), "", false);
                }

                // Handle diagnostics for the syntax tree
                if (syntaxTree.getDiagnostics() != null && !syntaxTree.getDiagnostics().isEmpty()) {

                    for (var diagnostic : syntaxTree.getDiagnostics()) {
                        System.out.println(ANSI_RED + diagnostic.getMessage() + ANSI_RESET);
                    }
                    textBuilder.setLength(0); // Clear the input builder after an error
                    continue;
                }

                // Handle diagnostics from evaluation result
                if (result.getDiagnostics() != null) {

                    for (var diagnostic : result.getDiagnostics()) {
                        System.out.println(ANSI_RED + diagnostic + ANSI_RESET);
                    }
                    textBuilder.setLength(0); // Clear the input builder after an error
                    continue;
                }

                // Display the result
                if (result.getValue() != null) {

                    System.out.println(ANSI_GREEN + "Result: " + result.getValue() + ANSI_RESET);
                    System.out.println("Current Variables: " + variable);
                }

            } else {

                // Append the input to the text builder for multi-line input
                textBuilder.append(input);
            }
        }
    }
    static void PrettyPrint(SyntaxNode node, String indent, boolean isLast) {
        var marker = isLast ? "|__" : "|--";
        System.out.print(indent);
        System.out.print(marker);
        System.out.print(node.getKind());

        if (node instanceof SyntaxToken t && t.value != null) {
            System.out.print(" ");
            System.out.print(t.value);
        }
        System.out.println(" ");

        indent += isLast ? "   " : "|   ";
        var children = node.GetChildren();
        if (children == null || children.isEmpty()) {
            return;
        }

        SyntaxNode lastChild = children.get(children.size() - 1);
        for (var child : children) {
            PrettyPrint(child, indent, child == lastChild);
        }
    }
}
