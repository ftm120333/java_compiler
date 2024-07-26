package main.lexer;

import main.ConsoleColors;
import main.lexer.Parser;
import main.lexer.SyntaxNode;
import main.lexer.SyntaxToken;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        boolean showTree = false;
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

            if(showTree){
                PrettyPrint(syntaxTree.getRoot(),"", false);
            }

            if (syntaxTree.getDiagnostics() != null) {
                var evaluator = new Evaluator(syntaxTree.getRoot());
                var result = evaluator.Evaluate();
                System.out.println(result);
            }
            else {

                for (var diagnostic: syntaxTree.getDiagnostics()) {
                    System.out.println(ConsoleColors.CYAN_BRIGHT+ diagnostic);

                }
            }
        }
    }
    static void PrettyPrint(SyntaxNode node, String indent,boolean isLast){
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