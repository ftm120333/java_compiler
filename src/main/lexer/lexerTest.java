package main.lexer;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LexerTest {

    @Test
    void testEndOfFileToken() {
        Lexer lexer = new Lexer("");
        SyntaxToken token = lexer.nextToken();
        Assertions.assertEquals(SyntaxKind.EndOfFileToken, token.getKind());
        assertEquals(0, token.position);
        assertEquals("\n", token.text);
        assertNull(token.value);
    }

    @Test
    void testNumberToken() {
        Lexer lexer = new Lexer("123");
        SyntaxToken token = lexer.nextToken();
        assertEquals(SyntaxKind.NumberToken, token.getKind());
        assertEquals(0, token.position);
        assertEquals("123", token.text);
        assertEquals(123, token.value);
    }

    @Test
    void testWhitespaceToken() {
        Lexer lexer = new Lexer("   ");
        SyntaxToken token = lexer.nextToken();
        assertEquals(SyntaxKind.WhitespaceToken, token.getKind());
        assertEquals(0, token.position);
        assertEquals("   ", token.text);
        assertNull(token.value);
    }

    @Test
    void testPlusToken() {
        Lexer lexer = new Lexer("+");
        SyntaxToken token = lexer.nextToken();
        assertEquals(SyntaxKind.PlusToken, token.getKind());
        assertEquals(0, token.position);
        assertEquals("+", token.text);
        assertNull(token.value);
    }

    @Test
    void testMinusToken() {
        Lexer lexer = new Lexer("-");
        SyntaxToken token = lexer.nextToken();
        assertEquals(SyntaxKind.MinusToken, token.getKind());
        assertEquals(0, token.position);
        assertEquals("-", token.text);
        assertNull(token.value);
    }

    @Test
    void testStarToken() {
        Lexer lexer = new Lexer("*");
        SyntaxToken token = lexer.nextToken();
        assertEquals(SyntaxKind.StarToken, token.getKind());
        assertEquals(0, token.position);
        assertEquals("*", token.text);
        assertNull(token.value);
    }

    @Test
    void testSlashToken() {
        Lexer lexer = new Lexer("/");
        SyntaxToken token = lexer.nextToken();
        assertEquals(SyntaxKind.SlashToken, token.getKind());
        assertEquals(0, token.position);
        assertEquals("/", token.text);
        assertNull(token.value);
    }

    @Test
    void testOpenParenthesisToken() {
        Lexer lexer = new Lexer("(");
        SyntaxToken token = lexer.nextToken();
        assertEquals(SyntaxKind.OpenParanthesisToken, token.getKind());
        assertEquals(0, token.position);
        assertEquals("(", token.text);
        assertNull(token.value);
    }

    @Test
    void testClosedParenthesisToken() {
        Lexer lexer = new Lexer(")");
        SyntaxToken token = lexer.nextToken();
        assertEquals(SyntaxKind.ClosedParanthesisToken, token.getKind());
        assertEquals(0, token.position);
        assertEquals(")", token.text);
        assertNull(token.value);
    }

    @Test
    void testBadToken() {
        Lexer lexer = new Lexer("@");
        SyntaxToken token = lexer.nextToken();
        assertEquals(SyntaxKind.BadToken, token.getKind());
        assertEquals(0, token.position);
        assertEquals("@", token.text);
        assertNull(token.value);
    }

    @Test
    void testDiagnostics() {
        Lexer lexer = new Lexer("@");
        lexer.nextToken();
        List<String> diagnostics = lexer.get_diagnostics();
        assertFalse(diagnostics.isEmpty());
        assertEquals("Error: bad character input: @", diagnostics.get(0));
    }
}
