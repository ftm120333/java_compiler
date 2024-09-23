package codeAnalysis;

import codeAnalysis.syntax.SyntaxNode;
import codeAnalysis.syntax.SyntaxToken;
import codeAnalysis.syntax.SyntaxTree;
import codeAnalysis.compiling.Compilation;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import codeAnalysis.text.TextSpan;
public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        boolean showTree = false;
        Map<VariableSymbol, Object> variable = new HashMap<>();
        var textBuilder = new StringBuilder();
        while (true){
            if(textBuilder.isEmpty())
                System.out.print("> ");
            else
                System.out.print("| ");

            var input = scanner.nextLine();
            boolean isBlank = input == null || input.isBlank();
            if(textBuilder.isEmpty()){
                if(isBlank)
                {
                    break;
                }
                else if (input.equals("#showTree"))
                {
                    showTree = !showTree;
                    System.out.println(showTree? "showing parse tree.": "Do not show parse tree.");
                    continue;
                }
                else if(input.equals("#cls"))
                {  ///TODO:CHECK THIS
                    /*This method clears the terminal screen and moves the cursor to the top.
                     It works on most Unix-based terminals (Linux, macOS),
                     and some Windows terminals that support ANSI codes.
                    */
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    continue;
                }
        }
        textBuilder.append(input);  //appendLine in C#
        var text = textBuilder.toString();
        var syntaxTree = SyntaxTree.parse(text);
        if(!isBlank && syntaxTree.getDiagnostics() != null)
            continue;

        var compilation = new Compilation(syntaxTree);
        var result = compilation.Evaluate(variable);

            if(showTree){
                //PrettyPrint(syntaxTree.getRoot(),"", false);
                System.out.println(syntaxTree.getRoot());
            }

            if (syntaxTree.getDiagnostics() != null) {
                System.out.println(result.getValue());
            }
            else {
                for (var diagnostic: result.getDiagnostics()) {
                    var lineIndex = syntaxTree.getText().getLineIndex(diagnostic.getSpan().getStart());
                    var line = syntaxTree.getText().getLines().get(lineIndex);
                    var lineNumber = lineIndex + 1;
                    var character = diagnostic.getSpan().getStart() - syntaxTree.getText().getLines().get(lineIndex).getStart() + 1;

                    System.out.println();
                    System.out.println("(" +lineNumber + ", " + character + "): ");
                    System.out.println(diagnostic);
                    System.out.println();

                    var prefixSpan = TextSpan.fromBounds(line.getStart(), diagnostic.getSpan().getStart());
                    var suffixSpan = TextSpan.fromBounds(diagnostic.getSpan().end(), line.end());

                    var prefix = input.substring(0, diagnostic.getSpan().getStart());
                    var error = input.substring(diagnostic.getSpan().getLength());
                    var suffix = input.substring(diagnostic.getSpan().end());

                    System.out.println();
                    System.out.print("  ");
                    System.out.print(prefix);
                    System.out.print("\u001B[31m" + error + "\u001B[0m");
                    System.out.print(suffix);
                    System.out.println();
                }
                System.out.println();
            }
        }
        //textBuilder.delete();
    }
    static void PrettyPrint(SyntaxNode node, String indent, boolean isLast){
        var marker = isLast ? "|__": "|--";
        System.out.print(indent);
        System.out.print(marker);
        System.out.print(node.getKind());

        if (node instanceof SyntaxToken t && t.value != null){
            System.out.print(" ");
            System.out.print(t.value);
        }
        System.out.println(" ");
        // Update indentation for child nodes
        indent +=  isLast? "   ": "|   ";


        var children = node.GetChildren();
        if (!children.isEmpty()) {
            SyntaxNode lastChild = children.get(children.size() - 1);

            for (var child : children) {
                PrettyPrint(child, indent, child == lastChild);
            }
        }
    }
}