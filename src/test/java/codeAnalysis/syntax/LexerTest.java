package codeAnalysis.syntax;

import codeAnalysis.syntax.Lexer;
import codeAnalysis.syntax.SyntaxKind;
import codeAnalysis.syntax.SyntaxToken;
import codeAnalysis.text.SourceText;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LexerTest {

    @Test
    void testSingleTokens() {
        assertTokens("+", SyntaxKind.PlusToken, 0, 1);
        assertTokens("-", SyntaxKind.MinusToken, 0, 1);
        assertTokens("*", SyntaxKind.StarToken, 0, 1);
        assertTokens("/", SyntaxKind.SlashToken, 0, 1);
        assertTokens("(", SyntaxKind.OpenParenthesisToken, 0, 1);
        assertTokens(")", SyntaxKind.ClosedParenthesisToken, 0, 1);
        assertTokens("{", SyntaxKind.OpenBraceToken, 0, 1);
        assertTokens("}", SyntaxKind.CloseBraceToken, 0, 1);
        assertTokens("=", SyntaxKind.EqualsToken, 0, 1);
        assertTokens("!", SyntaxKind.BangToken, 0, 1);
    }

    @Test
    void testNumberParsing() {
        assertTokens("123", SyntaxKind.NumberToken, 0, 3);
        assertTokens("0", SyntaxKind.NumberToken, 0, 1);
        assertTokens("987654", SyntaxKind.NumberToken, 0, 6);
    }

    @Test
    void testOperators() {
        assertTokens("==", SyntaxKind.EqualsEqualsToken, 0, 2);
        assertTokens("!=", SyntaxKind.BangEqualsToken, 0, 2);
        assertTokens("&&", SyntaxKind.AmpersandAmpersandToken, 0, 2);
        assertTokens("||", SyntaxKind.PipePipeToken, 0, 2);
    }

    @Test
    void testIdentifiers() {
        assertTokens("var", SyntaxKind.VarKeyword, 0, 3);
        assertTokens("let", SyntaxKind.LetKeyword, 0, 3);
        assertTokens("true", SyntaxKind.TrueKeyword, 0, 4);
        assertTokens("false", SyntaxKind.FalseKeyword, 0, 5);
       assertTokens("f", SyntaxKind.IdentifierToken, 0, 1);
    }

    @Test
    void testWhitespaceHandling() {
        assertTokens("    ", SyntaxKind.WhitespaceToken, 0, 4);
        assertTokens("\t\n", SyntaxKind.WhitespaceToken, 0, 2);
    }


    // Helper method to assert token details
    private static void assertTokens(String input, SyntaxKind expectedKind, int expectedStart, int expectedLength) {
        var sourceText = SourceText.from(input);
        var lexer = new Lexer(sourceText);
        List<SyntaxToken> tokens = new ArrayList<>();

        // Lex tokens until EOF
        SyntaxToken token;
        do {
            token = lexer.lex();
            tokens.add(token);
        } while (token.kind != SyntaxKind.EndOfFileToken);

        // Validate the first token
        SyntaxToken actualToken = tokens.get(0);
        assertEquals(expectedKind, actualToken.kind, "Token kind mismatch for input: " + input);
        assertEquals(expectedStart, actualToken.span().getStart(), "Token start mismatch for input: " + input);
        assertEquals(expectedLength, actualToken.span().getLength(), "Token length mismatch for input: " + input);
    }
}
