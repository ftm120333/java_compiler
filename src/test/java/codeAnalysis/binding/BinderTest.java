package codeAnalysis.binding;

import codeAnalysis.VariableSymbol;
import codeAnalysis.syntax.*;
import codeAnalysis.text.SourceText;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BinderTest {

    @Test
    void testVariableDeclaration() {
        var syntaxTree = SyntaxTree.parse("var x = 10");
        var binder = new Binder(null);
        var root = syntaxTree.getRoot();

        assertNotNull(root);

        var boundGlobalScope = Binder.bindGlobalScope(null, root);
        var diagnostics = boundGlobalScope.getDiagnostics();

        assertTrue(diagnostics.isEmpty(), "Diagnostics should be empty");
        var variables = boundGlobalScope.getVariables();

        assertEquals(1, variables.size(), "Should declare one variable");
        assertEquals("x", variables.get(0).getName(), "Variable name should be 'x'");
        assertFalse(variables.get(0).isReadOnly(), "Variable should not be read-only");
    }

    @Test
    void testReadOnlyVariableDeclaration() {
        var syntaxTree = SyntaxTree.parse("let x = 10");
        var binder = new Binder(null);
        var root = syntaxTree.getRoot();

        assertNotNull(root);

        var boundGlobalScope = Binder.bindGlobalScope(null, root);
        var diagnostics = boundGlobalScope.getDiagnostics();

        assertTrue(diagnostics.isEmpty(), "Diagnostics should be empty");
        var variables = boundGlobalScope.getVariables();

        assertEquals(1, variables.size(), "Should declare one variable");
        assertEquals("x", variables.get(0).getName(), "Variable name should be 'x'");
        assertTrue(variables.get(0).isReadOnly(), "Variable should be read-only");
    }

    @Test
    void testUndefinedVariable() {
        var syntaxTree = SyntaxTree.parse("x + 5;");
        var binder = new Binder(null);
        var root = syntaxTree.getRoot();
    
        var boundGlobalScope = Binder.bindGlobalScope(null, root);
        var diagnostics = boundGlobalScope.getDiagnostics();
    
        assertFalse(diagnostics.isEmpty(), "Diagnostics should not be empty for undefined variables");
        assertTrue(diagnostics.get(0).getMessage().contains("Variable x does not exist"),
                "The diagnostic should indicate that 'x' does not exist.");
    }
    
   
    @Test
    void testBinaryExpression() {
        var syntaxTree = SyntaxTree.parse("10 + 20");
        var binder = new Binder(null);
        var root = syntaxTree.getRoot();

        assertNotNull(root);

        var boundGlobalScope = Binder.bindGlobalScope(null, root);
        var diagnostics = boundGlobalScope.getDiagnostics();

        assertTrue(diagnostics.isEmpty(), "Diagnostics should be empty for a valid binary expression");

        var statement = boundGlobalScope.getStatement();
        assertNotNull(statement, "Statement should not be null");

        assertTrue(statement instanceof BoundExpressionStatement, "Should bind to an expression statement");

        var expressionStatement = (BoundExpressionStatement) statement;
        assertTrue(expressionStatement.getExpression() instanceof BoundBinaryExpression, "Should bind to a binary expression");

        var binaryExpression = (BoundBinaryExpression) expressionStatement.getExpression();
        assertTrue(binaryExpression.getLeft() instanceof BoundLiteralExpression, "Left operand should be a literal expression");
        assertTrue(binaryExpression.getRight() instanceof BoundLiteralExpression, "Right operand should be a literal expression");
    }

    @Test
    void testBlockStatement() {
        var syntaxTree = SyntaxTree.parse("""
            {
                var x = 10
                x + 20
            }
        """);
        var binder = new Binder(null);
        var root = syntaxTree.getRoot();

        assertNotNull(root);

        var boundGlobalScope = Binder.bindGlobalScope(null, root);
        var diagnostics = boundGlobalScope.getDiagnostics();

        assertTrue(diagnostics.isEmpty(), "Diagnostics should be empty for a valid block statement");

        var statement = boundGlobalScope.getStatement();
        assertNotNull(statement, "Statement should not be null");

        assertTrue(statement instanceof BoundBlockStatement, "Should bind to a block statement");

        var blockStatement = (BoundBlockStatement) statement;
        var statements = blockStatement.getStatements();

        assertEquals(2, statements.size(), "Block should contain two statements");
        assertTrue(statements.get(0) instanceof BoundVariableDeclaration, "First statement should be a variable declaration");
        assertTrue(statements.get(1) instanceof BoundExpressionStatement, "Second statement should be an expression statement");
    }
}
