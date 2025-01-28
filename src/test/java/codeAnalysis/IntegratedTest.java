package codeAnalysis;


import codeAnalysis.compiling.Compilation;
import codeAnalysis.syntax.SyntaxTree;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;



public class IntegratedTest {

    @Test
    void testSimpleAssignment() throws Exception {
        String expression = "var a = 5";
        assertOutput(expression, "Result: 5", "Current Variables: {a=5}");
    }

    @Test
    void testArithmeticExpression() throws Exception {
        String expression = "5 + 3";
        assertOutput(expression, "Result: 8");
    }

    @Test
    void testUndefinedVariable() throws Exception {
        String expression = "a + 3";
        assertOutput(expression, "Variable a does not exist.");
    }

    @Test
    void testVariableUsage() throws Exception {
        String expression = """
            {var a = 10
            a + 20}
            """;
        assertOutput(expression, "Result: 30", "Current Variables: {a=10}");
    }
    @Test
    void testVariableUsageWithScope() throws Exception {
        String expression = """
            {var a = 10
            {a + 20}}
            """;
        assertOutput(expression, "Result: 30", "Current Variables: {a=10}");
    }

    @Test
    void testInvalidSyntax() throws Exception {
        String expression = "var = 5";
        assertOutput(expression, "Unexpected token <EqualsToken>. Expected <IdentifierToken>.");
    }

    @Test
    void testMultipleAssignments() throws Exception {
        String expression = """
            {var x = 5
            var y = 10
            x + y}
            """;
        assertOutput(expression, "Result: 15", "Current Variables: {x=5, y=10}");
    }

    @Test
    void testChainedAssignment() throws Exception {
        String expression = """
           { var x = 5
            var y = x
            {y + 10}
                }""";
        assertOutput(expression, "Result: 15", "Current Variables: {x=5, y=5}");
    }

    @Test
    void testBooleanExpression() throws Exception {
        String expression = "true && false";
        assertOutput(expression, "Result: false");
    }

    @Test
    void testParenthesesPrecedence() throws Exception {
        String expression = "1 + 2 * (3 + 4)";
        assertOutput(expression, "Result: 15");
    }

    @Test
    void testBlockScope() throws Exception {
        String expression = """
            {var c = 4
             var m = 4
              m + c 
            }
            """;
        assertOutput(expression, "Result: 8", "Current Variables: {c=4, m=4}");
    }

    @Test
    void testReadOnlyVariable() throws Exception {
        String expression = """
            {let x = 10
            {x = 20}}
            """;
        assertOutput(expression, "Variable x is read-only and can not be assigned.");
    }

    @Test
    void testInvalidNumber() throws Exception {
        String expression = "var x = 999999999999999999999999999999";
        assertOutput(expression, "The number 999999999999999999999999999999 is not a valid Integer.");
    }

    @Test
    void testWhitespaceHandling() throws Exception {
        String expression = """
           { 
            var x = 10   
            
            x + 5  
    }
            """;
        assertOutput(expression, "Result: 15", "Current Variables: {x=10}");
    }

    @Test
    void testLogicalOperators() throws Exception {
        String expression = "true || false && false";
        assertOutput(expression, "Result: true");
    }

    

    // Helper to run `compile` and assert output
    private void assertOutput(String expression, String... expectedOutputs) throws Exception {
        var out = new TestOutput(); // Custom helper to capture System.out
        System.setOut(out);

        // Call the compile method
        TestMain.compile(expression);

        // Reset System.out after test
        System.setOut(System.out);

        // Assert all expected outputs are in the captured output
        String actualOutput = out.getCapturedOutput();
        for (String expected : expectedOutputs) {
            assertTrue(actualOutput.contains(expected),
                    "Expected output not found: " + expected + "\nActual output:\n" + actualOutput);
        }
    }
}
