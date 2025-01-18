package codeAnalysis.syntax;


import codeAnalysis.syntax.*;
import codeAnalysis.text.SourceText;

import java.util.ArrayList;
import java.util.List;

public class LexerTest {


    public static void runLexerTests() {
        testSingleTokens();
        testNumberParsing();
        testOperators();
        testIdentifiers();
        testWhitespaceHandling();
        testInvalidCharacters();
        System.out.println("\u001B[32mAll Lexer Tests Passed!\u001B[0m");
    }

    // Test basic single-character tokens
    private static void testSingleTokens() {
        assertTokens("+", SyntaxKind.PlusToken);
        assertTokens("-", SyntaxKind.MinusToken);
        assertTokens("*", SyntaxKind.StarToken);
        assertTokens("/", SyntaxKind.SlashToken);
        assertTokens("(", SyntaxKind.OpenParenthesisToken);
        assertTokens(")", SyntaxKind.ClosedParenthesisToken);
        assertTokens("{", SyntaxKind.OpenBraceToken);
        assertTokens("}", SyntaxKind.CloseBraceToken);
        assertTokens("=", SyntaxKind.EqualsToken);
        assertTokens("!", SyntaxKind.BangToken);
    }

    // Test number parsing (literals)
    private static void testNumberParsing() {
        assertTokens("123", SyntaxKind.NumberToken);
        assertTokens("0", SyntaxKind.NumberToken);
        assertTokens("987654", SyntaxKind.NumberToken);
    }

    // Test compound and multi-character operators
    private static void testOperators() {
        assertTokens("==", SyntaxKind.EqualsEqualsToken);
        assertTokens("!=", SyntaxKind.BangEqualsToken);
        assertTokens("&&", SyntaxKind.AmpersandAmpersandToken);
        assertTokens("||", SyntaxKind.PipePipeToken);
    }

    // Test identifiers and keywords
    private static void testIdentifiers() {
        assertTokens("var", SyntaxKind.VarKeyword);
        assertTokens("let", SyntaxKind.LetKeyword);
        assertTokens("true", SyntaxKind.TrueKeyword);
        assertTokens("false", SyntaxKind.FalseKeyword);
        assertTokens("foo", SyntaxKind.IdentifierToken);
        assertTokens("bar123", SyntaxKind.IdentifierToken);
    }

    // Test whitespace and end-of-file (EOF)
    private static void testWhitespaceHandling() {
        assertTokens("    ", SyntaxKind.WhitespaceToken);
        assertTokens("\t\n", SyntaxKind.WhitespaceToken);
        assertTokens("", SyntaxKind.EndOfFileToken);
    }

    // Test invalid and bad characters
    private static void testInvalidCharacters() {
        assertTokens("@", SyntaxKind.BadToken);
        assertTokens("$", SyntaxKind.BadToken);
        assertTokens("#", SyntaxKind.BadToken);
    }

    // Assertion helper for token testing
    private static void assertTokens(String input, SyntaxKind expectedKind) {
        var sourceText = SourceText.from(input);
        var lexer = new Lexer(sourceText);
        List<SyntaxToken> tokens = new ArrayList<>();

        // Lex tokens until EOF
        SyntaxToken token;
        do {
            token = lexer.lex();
            tokens.add(token);
        } while (token.kind != SyntaxKind.EndOfFileToken);

        // Extract the first token
        SyntaxToken actualToken = tokens.get(0);
        if (actualToken.kind != expectedKind) {
            System.err.printf("\u001B[31mTest Failed for input '%s'. Expected %s, but got %s.\u001B[0m\n",
                    input, expectedKind, actualToken.kind);
            System.exit(1);
        }
    }
}
