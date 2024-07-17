package main.lexer;

import main.ConsoleColors;
import main.lexer.Parser;
import main.lexer.SyntaxNode;
import main.lexer.SyntaxToken;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true){
            System.out.printf("> ");
            var line = scanner.nextLine();
            var parser = new Parser(line);
            var expression = parser.Parse();
    /*         ConsoleColors.RESET + " NORMAL"*/
    /*                 ConsoleColors.RED*/


            PrettyPrint(expression, "",false);
            if(parser.Diognostics() != null){
                System.out.println("it is not null");
                for (var diagnostic:parser.Diognostics() ) {
                    System.out.println(ConsoleColors.CYAN_BRIGHT+ diagnostic);

                }
            }
        }
    }
    static void PrettyPrint(SyntaxNode node, String indent,boolean isLast){
        var marker = isLast ? "|__": "|--";
        System.out.print( indent);
        System.out.print(marker);
        System.out.print(node.getKind());

        if (node instanceof SyntaxToken t && t.value != null ){
            System.out.print(" ");
            System.out.print(t.value);
        }
        System.out.println(" ");
        indent +=  isLast? "   ": "|   ";

        SyntaxNode lastChild = null;
        for (SyntaxNode child : node.GetChildren()) {
            lastChild = child;
        }

        for (var child:node.GetChildren()) {
            PrettyPrint(child,indent, node == lastChild);
        }
    }
}