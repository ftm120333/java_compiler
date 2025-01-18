package codeAnalysis.syntax;


import codeAnalysis.syntax.*;
import codeAnalysis.text.SourceText;

public class TokenSpanTests {

    public static void runTokenSpanTests() {
        testTokenSpanSingleCharacters();
        testTokenSpanMultipleCharacters();
        testTokenSpanWithWhitespace();
        testTokenSpanForInvalidCharacters();
        System.out.println("\u001B[32mAll Token Span Tests Passed!\u001B[0m");
    }

    // Test spans for single-character tokens
    private static void testTokenSpanSingleCharacters() {
        assertTokenSpan("+", SyntaxKind.PlusToken, 0, 1);
        assertTokenSpan("-", SyntaxKind.MinusToken, 0, 1);
        assertTokenSpan("*", SyntaxKind.StarToken, 0, 1);
        assertTokenSpan("/", SyntaxKind.SlashToken, 0, 1);
    }

    // Test spans for multi-character tokens
    private static void testTokenSpanMultipleCharacters() {
        assertTokenSpan("==", SyntaxKind.EqualsEqualsToken, 0, 2);
        assertTokenSpan("!=", SyntaxKind.BangEqualsToken, 0, 2);
        assertTokenSpan("&&", SyntaxKind.AmpersandAmpersandToken, 0, 2);
        assertTokenSpan("||", SyntaxKind.PipePipeToken, 0, 2);
    }

    // Test spans with whitespace and mixed tokens
    private static void testTokenSpanWithWhitespace() {
        assertTokenSpan("  +  ", SyntaxKind.PlusToken, 2, 1); // Token starts after whitespace
        assertTokenSpan("\t!=\n", SyntaxKind.BangEqualsToken, 1, 2); // Handles tab and newline
        assertTokenSpan(" 123 ", SyntaxKind.NumberToken, 1, 3); // Handles surrounding spaces
    }

    // Test spans for invalid characters
    private static void testTokenSpanForInvalidCharacters() {
        assertTokenSpan("@", SyntaxKind.BadToken, 0, 1);
        assertTokenSpan("$var", SyntaxKind.BadToken, 0, 1); // First character is invalid
        assertTokenSpan("#123", SyntaxKind.BadToken, 0, 1); // Invalid start character
    }

    // Helper function for asserting token spans
    private static void assertTokenSpan(String input, SyntaxKind expectedKind, int expectedStart, int expectedLength) {
        var sourceText = SourceText.from(input);
        var lexer = new Lexer(sourceText);

        SyntaxToken token = lexer.lex(); // Get the first token
        var span = token.span(); // Extract its span

        if (token.kind != expectedKind || span.getStart() != expectedStart || span.getLength() != expectedLength) {
            System.err.printf("\u001B[31mTest Failed for input '%s'. Expected (%s, %d, %d) but got (%s, %d, %d).\u001B[0m\n",
                    input, expectedKind, expectedStart, expectedLength,
                    token.kind, span.getStart(), span.getLength());
            System.exit(1);
        }
    }
}
