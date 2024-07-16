import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true){
            System.out.printf("> ");
            var line = scanner.nextLine();
            var parser = new Parser(line);
            var expression = parser.Parse();
            PrettyPrint(expression, "");


        }
    }
    static void PrettyPrint(SyntaxNode node, String indent ){
        System.out.println(node.getKind());
        if (node instanceof SyntaxToken t && t.value != null ){
            System.out.println("");
            System.out.println(t.value);
        }
        indent += "  ";
        for (var child:node.GetChildren()) {
            PrettyPrint(child,indent);
        }
    }
}