package codeAnalysis;

import codeAnalysis.binding.Binder;
import codeAnalysis.compiling.Compilation;
import codeAnalysis.syntax.SyntaxNode;
import codeAnalysis.syntax.SyntaxToken;
import codeAnalysis.syntax.SyntaxTree;


import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        boolean showTree = false;
        Map<String, Object> variable = new HashMap<>();
        while (true){
            System.out.printf(">> ");

            var line = scanner.nextLine();
            if(line.isEmpty())
                return;

            if (line.equals("#showTree"))
            {
                showTree = !showTree;
                System.out.println(showTree? "showing parse tree.": "Do not show parse tree.");
                continue;
            }

            var syntaxTree = SyntaxTree.Parse(line);
            var compilation = new Compilation(syntaxTree);
            var result = compilation.Evaluate(variable);
            var diagnostics = result.getDiagnostics();


            /* var binder = new Binder();
            var boundExpression = binder.bindExpression(syntaxTree.getRoot());
            final Iterable<String> diagnostics= (Iterable<String>) syntaxTree.getDiagnostics();*/


            if(showTree){
                PrettyPrint(syntaxTree.getRoot(),"", false);
            }

            if (syntaxTree.getDiagnostics() != null) {
                System.out.println(result.getValue());
            }
            else {

                for (var diagnostic: syntaxTree.getDiagnostics()) {
                    System.out.println(diagnostic);

                    var prefix = line.substring(0, diagnostic.getSpan().getStart());
                    var error = line.substring(diagnostic.getSpan().getLength());
                    var suffix = line.substring(diagnostic.getSpan().end());
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