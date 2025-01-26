package codeAnalysis.syntax;

import codeAnalysis.text.SourceText;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {

    @Test
    void testSimpleExpression() {
        var expression = "1 + 2";
        var syntaxTree = SyntaxTree.parse(expression);

        var root = syntaxTree.getRoot();
        assertNotNull(root);

        assertNode(root, SyntaxKind.CompilationUnit, List.of(
                SyntaxKind.ExpressionStatement,
                SyntaxKind.EndOfFileToken
        ));

        var expressionStatement = (ExpressionStatementSyntax) root.getStatement();
        assertNode(expressionStatement, SyntaxKind.ExpressionStatement);

        var binaryExpression = (BinaryExpressionSyntax) expressionStatement.getExpression();
        assertNode(binaryExpression, SyntaxKind.BinaryExpression, List.of(
                SyntaxKind.LiteralExpression,
                SyntaxKind.PlusToken,
                SyntaxKind.LiteralExpression
        ));
    }

    @Test
    void testVariableDeclaration() {
        var expression = "var x = 42";
        var syntaxTree = SyntaxTree.parse(expression);

        var root = syntaxTree.getRoot();
        assertNotNull(root);

        assertNode(root, SyntaxKind.CompilationUnit, List.of(
                SyntaxKind.VariableDeclaration,
                SyntaxKind.EndOfFileToken
        ));

        var variableDeclaration = (VariableDeclarationSyntax) root.getStatement();
        assertNode(variableDeclaration, SyntaxKind.VariableDeclaration);

        assertToken(variableDeclaration.getKeyword(), SyntaxKind.VarKeyword, "var");
        assertToken(variableDeclaration.getIdentifier(), SyntaxKind.IdentifierToken, "x");
        assertToken(variableDeclaration.getEqualsToken(), SyntaxKind.EqualsToken, "=");

        var initializer = (LiteralExpressionSyntax) variableDeclaration.getInitializer();
        assertNode(initializer, SyntaxKind.LiteralExpression);
    }

    @Test
    void testBlockStatement() {
        var expression = "{ var x = 42  x + 10 }";
        var syntaxTree = SyntaxTree.parse(expression);

        var root = syntaxTree.getRoot();
        assertNotNull(root);

        assertNode(root, SyntaxKind.CompilationUnit, List.of(
                SyntaxKind.BlockStatement,
                SyntaxKind.EndOfFileToken
        ));

        var blockStatement = (BlockStatementSyntax) root.getStatement();
        assertNode(blockStatement, SyntaxKind.BlockStatement);

        var statements = blockStatement.getStatements();
        assertEquals(2, statements.size());

        var variableDeclaration = (VariableDeclarationSyntax) statements.get(0);
        assertNode(variableDeclaration, SyntaxKind.VariableDeclaration);

        var expressionStatement = (ExpressionStatementSyntax) statements.get(1);
        assertNode(expressionStatement, SyntaxKind.ExpressionStatement);

        var binaryExpression = (BinaryExpressionSyntax) expressionStatement.getExpression();
        assertNode(binaryExpression, SyntaxKind.BinaryExpression);
    }

    @Test
    void testBooleanLiteral() {
        var expression = "true && false";
        var syntaxTree = SyntaxTree.parse(expression);

        var root = syntaxTree.getRoot();
        assertNotNull(root);

        assertNode(root, SyntaxKind.CompilationUnit, List.of(
                SyntaxKind.ExpressionStatement,
                SyntaxKind.EndOfFileToken
        ));

        var expressionStatement = (ExpressionStatementSyntax) root.getStatement();
        var binaryExpression = (BinaryExpressionSyntax) expressionStatement.getExpression();
        assertNode(binaryExpression, SyntaxKind.BinaryExpression);

        var left = (LiteralExpressionSyntax) binaryExpression.getLeft();
        assertNode(left, SyntaxKind.LiteralExpression);
        assertToken(left.getNumberToken(), SyntaxKind.TrueKeyword, "true");

        var right = (LiteralExpressionSyntax) binaryExpression.getRight();
        assertNode(right, SyntaxKind.LiteralExpression);
        assertToken(right.getNumberToken(), SyntaxKind.FalseKeyword, "false");
    }

    @Test
    void testParenthesizedExpression() {
        var expression = "(1 + 2) * 3";
        var syntaxTree = SyntaxTree.parse(expression);

        var root = syntaxTree.getRoot();
        assertNotNull(root);

        assertNode(root, SyntaxKind.CompilationUnit, List.of(
                SyntaxKind.ExpressionStatement,
                SyntaxKind.EndOfFileToken
        ));

        var expressionStatement = (ExpressionStatementSyntax) root.getStatement();
        var binaryExpression = (BinaryExpressionSyntax) expressionStatement.getExpression();
        assertNode(binaryExpression, SyntaxKind.BinaryExpression);

        var left = (ParanthrsizedExpressionSyntax) binaryExpression.getLeft();
        assertNode(left, SyntaxKind.ParanthesizedExpression);

        var innerBinaryExpression = (BinaryExpressionSyntax) left.expression;
        assertNode(innerBinaryExpression, SyntaxKind.BinaryExpression);
    }

    // Helper for asserting nodes
    private void assertNode(SyntaxNode node, SyntaxKind expectedKind) {
        assertNotNull(node);
        assertEquals(expectedKind, node.getKind());
    }

    private void assertNode(SyntaxNode node, SyntaxKind expectedKind, List<SyntaxKind> expectedChildren) {
        assertNode(node, expectedKind);

        var children = node.GetChildren();
        assertEquals(expectedChildren.size(), children.size());

        for (int i = 0; i < expectedChildren.size(); i++) {
            assertEquals(expectedChildren.get(i), children.get(i).getKind());
        }
    }

    // Helper for asserting tokens
    private void assertToken(SyntaxToken token, SyntaxKind expectedKind, String expectedText) {
        assertNotNull(token);
        assertEquals(expectedKind, token.getKind());
        assertEquals(expectedText, token.text);
    }
}
