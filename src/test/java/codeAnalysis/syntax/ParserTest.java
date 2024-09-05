package codeAnalysis.syntax;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {


    @Test
    public void test_parser_initialization_with_text() {
        String input = "123 + 456";
        Parser parser = new Parser(input);

        SyntaxToken[] tokens = parser.getTokens();
        for (SyntaxToken token : tokens) {
            System.out.println("Token Type: " + token.kind);
        }
        assertNotNull(tokens);
        assertEquals(3, tokens.length);
        assertEquals(SyntaxKind.NumberToken, tokens[0].getKind());
        assertEquals(SyntaxKind.PlusToken, tokens[1].getKind());
        assertEquals(SyntaxKind.NumberToken, tokens[2].getKind());
    }
    @Test
    public void test_parser_with_empty_input() {
        String input = "";
        Parser parser = new Parser(input);
        SyntaxToken[] tokens = parser.getTokens();

        assertNotNull(tokens);
        assertEquals(0, tokens.length);
}

    @Test
    public void returns_next_token_when_current_token_matches_expected_kind() {
        String text = "a = 5";
        Parser parser = new Parser(text);
        SyntaxToken token = parser.matchToken(SyntaxKind.IdentifierToken);
        assertEquals(SyntaxKind.IdentifierToken, token.kind);
        assertEquals("a", token.text);
    }

    @Test
    public void test_parse_assignment_expression() {
        // Setup
        String input = "x = 10";
        SyntaxTree syntaxTree = SyntaxTree.parse(input);

        // Execution
        ExpressionSyntax result = syntaxTree.getRoot();

        // Assertion
        assertTrue(result instanceof AssignmentExpressionSyntax);
        AssignmentExpressionSyntax assignmentExpression = (AssignmentExpressionSyntax) result;
        assertEquals(SyntaxKind.IdentifierToken, assignmentExpression.getIdentifierToken().kind);
        assertEquals(SyntaxKind.EqualsToken, assignmentExpression.equalsToken.kind);
        assertTrue(assignmentExpression.expression instanceof LiteralExpressionSyntax);
    }

    @Test
    public void test_parse_binary_expression() {
        // Setup
        String input = "x + 10 * y";
        SyntaxTree syntaxTree = SyntaxTree.parse(input);

        // Execution
        ExpressionSyntax result = syntaxTree.getRoot();

        // Assertion
        assertTrue(result instanceof BinaryExpressionSyntax);
        BinaryExpressionSyntax binaryExpression = (BinaryExpressionSyntax) result;
        assertEquals(SyntaxKind.BinaryExpression, binaryExpression.getKind());
        assertEquals(SyntaxKind.PlusToken, binaryExpression.getOperatorToken().getKind());
        assertTrue(binaryExpression.getRight() instanceof BinaryExpressionSyntax);
    }
}