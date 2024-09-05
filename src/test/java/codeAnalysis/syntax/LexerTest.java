package codeAnalysis.syntax;

import codeAnalysis.syntax.Lexer;
import codeAnalysis.syntax.SyntaxKind;
import codeAnalysis.syntax.SyntaxToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
//src/test/java/codeAnalysis/syntax/codeAnalysis.syntax.LexerTest.java
class LexerTest {

    @Test
    public void initializes_text_with_provided_input() {
        String input = "sample text";
        Lexer lexer = new Lexer(input);
        assertEquals(input, lexer.get_text());
    }
    @Test
    public void test_lex_digit_sequence_returns_number_token() {
        String input = "123";
        Lexer lexer = new Lexer(input);
        SyntaxToken token = lexer.lex();
        assertEquals(SyntaxKind.NumberToken, token.kind);
        assertEquals(0, token.position);
        assertEquals("123", token.text);
        assertEquals(123, token.value);
    }
    @Test
    public void test_whitespace_input() {
        String input = "   ";
        Lexer lexer = new Lexer(input);
        SyntaxToken token = lexer.lex();

        assertEquals(SyntaxKind.WhitespaceToken, token.kind);
        assertEquals(0, token.position);
        assertEquals("   ", token.text);
        assertNull(token.value);
    }
    @Test
    public void test_lex_empty_string_returns_end_of_file_token() {
        String input = "";
        Lexer lexer = new Lexer(input);
        SyntaxToken token = lexer.lex();
        assertEquals(SyntaxKind.EndOfFileToken, token.kind);
        assertEquals(0, token.position);
        assertEquals("\n", token.text);
        assertNull(token.value);
    }

    @Test
    public void test_letter_token_lexing() {
        String input = "a+-*/() && || == != !";
        Lexer lexer = new Lexer(input);
        SyntaxToken token = lexer.lex();

        assertEquals(SyntaxKind.IdentifierToken, token.kind);
        assertEquals(0, token.position);
        assertEquals("a", token.text);
        assertNull(token.value);

        token = lexer.lex();
        assertEquals(SyntaxKind.PlusToken, token.kind);
        assertEquals(1, token.position);
        assertEquals("+", token.text);
        assertNull(token.value);

        token = lexer.lex();
        assertEquals(SyntaxKind.MinusToken, token.kind);
        assertEquals(2, token.position);
        assertEquals("-", token.text);
        assertNull(token.value);

        token = lexer.lex();
        assertEquals(SyntaxKind.StarToken, token.kind);
        assertEquals(3, token.position);
        assertEquals("*", token.text);
        assertNull(token.value);

        token = lexer.lex();
        assertEquals(SyntaxKind.SlashToken, token.kind);
        assertEquals(4, token.position);
        assertEquals("/", token.text);
        assertNull(token.value);

        token = lexer.lex();
        assertEquals(SyntaxKind.OpenParanthesisToken, token.kind);
        assertEquals(5, token.position);
        assertEquals("(", token.text);
        assertNull(token.value);

        token = lexer.lex();
        assertEquals(SyntaxKind.ClosedParanthesisToken, token.kind);
        assertEquals(6, token.position);
        assertEquals(")", token.text);
        assertNull(token.value);

        token = lexer.lex();
        token = lexer.lex();
        assertEquals(SyntaxKind.AmpersandAmpersandToken, token.kind);
        assertEquals(8, token.position);
        assertEquals("&&", token.text);
        assertNull(token.value);

        token = lexer.lex();
        token = lexer.lex();
        assertEquals(SyntaxKind.PipePipeToken, token.kind);
        assertEquals(11, token.position);
        assertEquals("||", token.text);
        assertNull(token.value);

        token = lexer.lex();
        token = lexer.lex();
        assertEquals(SyntaxKind.EqualsEqualsToken, token.kind);
        assertEquals(14, token.position);
        assertEquals("==", token.text);
        assertNull(token.value);

        token = lexer.lex();
        token = lexer.lex();
        assertEquals(SyntaxKind.BangEqualsToken, token.kind);
        assertEquals(17, token.position);
        assertEquals("!=", token.text);
        assertNull(token.value);

        token = lexer.lex();
        token = lexer.lex();
        assertEquals(SyntaxKind.BangToken, token.kind);
        assertEquals(20, token.position);
        assertEquals("!", token.text);
        assertNull(token.value);

    }



}