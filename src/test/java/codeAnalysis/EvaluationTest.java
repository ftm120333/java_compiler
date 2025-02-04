package codeAnalysis;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import codeAnalysis.compiling.Compilation;
import codeAnalysis.compiling.EvaluationResult;
import codeAnalysis.syntax.SyntaxTree;
import codeAnalysis.text.TextSpan;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class EvaluationTest {

    @ParameterizedTest
    @CsvSource({
        "'1', 1",
        "'+1', 1",
        "'-1', -1",
        "'14 + 12', 26",
        "'12 - 3', 9",
        "'4 * 2', 8",
        "'9 / 3', 3",
        "'(10)', 10",
        "'12 == 3', false",
        "'3 == 3', true",
        "'12 != 3', true",
        "'3 != 3', false",
        "'false == false', true",
        "'true == false', false",
        "'false != false', false",
        "'true != false', true",
        "'true', true",
        "'false', false",
        "'!true', false",
        "'!false', true",
        "'{ var a = 0 (a = 10) * a }', 100",
        
        //Not Implemented cases
        // "'3 < 4', true",
        // "'5 < 4', false",
        // "'4 <= 4', true",
        // "'4 <= 5', true",
        // "'5 <= 4', false",
        // "'4 > 3', true",
        // "'4 > 5', false",
        // "'4 >= 4', true",
        // "'5 >= 4', true",
        // "'4 >= 5', false",
        //"'{ var a = 0 if a == 0 a = 10 a }', 10",
        // "'{ var a = 0 if a == 4 a = 10 a }', 0",
        // "'{ var a = 0 if a == 0 a = 10 else a = 5 a }', 10",
        // "'{ var a = 0 if a == 4 a = 10 else a = 5 a }', 5",
        // "'{ var i = 10 var result = 0 while i > 0 { result = result + i i = i - 1} result }', 55",
        // "'{ var result = 0 for i = 1 to 10 { result = result + i } result }', 55"
    })

    public void evaluator_computes_correctValues(String text, Object expectedValue) throws Exception {
         assertValue(text, expectedValue);
    }

    private void assertValue(String text, Object expectedValue) throws Exception {
        AnnotatedText annotatedText = AnnotatedText.parse(text);
        SyntaxTree syntaxTree = SyntaxTree.parse(annotatedText.getText());
        Compilation compilation = new Compilation(syntaxTree);
        EvaluationResult result = compilation.evaluate(new HashMap<>());
        System.out.println("Result: " + result.getValue());
        assertEquals(expectedValue, result.getValue().toString().trim());
    }
    
    @Test
    public void evaluator_VariableDeclaration_Reports_Redeclaration() throws Exception {
        String text = """
            {
                var x = 10
                var y = 100
                {
                    var x = 10
                }
                var [x] = 5
            }
        """;

        String diagnostics = """
            Variable x already declared.
        """;

        assertDiagnostics(text, diagnostics);
    }

    @Test
    public void evaluator_NameExpression_Reports_Undefined() throws Exception {
        String text = "[x] * 10";

        String diagnostics = """
            Variable x does not exist.
        """;

        assertDiagnostics(text, diagnostics);
    }

    @Test
    public void evaluator_AssignmentExpression_Reports_CannotAssign() throws Exception {
        String text = """
            {
                let x = 10
                x [=] 0
            }
        """;

        String diagnostics = """
            Variable x is read-only and can not be assigned.
        """;

        assertDiagnostics(text, diagnostics);
    }

    @Test
    public void evaluator_unary_Reports_undefined() throws Exception {
        String text = "[+]true";

        String diagnostics = """
            The unary operator + is not defined for type class java.lang.Boolean.
        """;

        assertDiagnostics(text, diagnostics);
    }
    @Test
    public void evaluator_unary2_Reports_undefined() throws Exception {
        String text = "[-]true";

        String diagnostics = """
            The unary operator - is not defined for type class java.lang.Boolean.
        """;

        assertDiagnostics(text, diagnostics);
    }

    @Test
    public void evaluator_binary_Reports_undefined() throws Exception {
        String text = "10 [*] true";

        String diagnostics = "The binary operator * is not defined for types class java.lang.Integer and class java.lang.Boolean.";

        assertDiagnostics(text, diagnostics);
    }
    
    @Test
    public void evaluator_binary_withOR_Reports_undefined() throws Exception {
        String text = "10 [||] true";

        String diagnostics = "The binary operator || is not defined for types class java.lang.Integer and class java.lang.Boolean.";

        assertDiagnostics(text, diagnostics);
    }
    ///TODO:Check the code of this 
    
    // @Test
    // public void evaluator_AssignmentExpression_Reports_CannotConvert() throws Exception {
    //     String text = """
    //         {
    //             var x = 10  
    //             x = [true]
    //         }
    //     """;

    //     String diagnostics = """
    //         Can not convert type class java.lang.Boolean to class java.lang.Integer.
    //     """;

    //     assertDiagnostics(text, diagnostics);
    // }
    
    private void assertDiagnostics(String text, String diagnosticText) throws Exception {
        AnnotatedText annotatedText = AnnotatedText.parse(text);
        SyntaxTree syntaxTree = SyntaxTree.parse(annotatedText.getText());
        Compilation compilation = new Compilation(syntaxTree);
        EvaluationResult result = compilation.evaluate(new HashMap<>());

        String[] expectedDiagnostics = AnnotatedText.unindentLines(diagnosticText);

        assertEquals(expectedDiagnostics.length, result.getDiagnostics().size());
        

        for (int i = 0; i < expectedDiagnostics.length; i++) {
            String expectedMessage = expectedDiagnostics[i];
            String actualMessage = result.getDiagnostics().get(i).toString();
            assertEquals(expectedMessage, actualMessage);

            TextSpan expectedSpan = annotatedText.getSpans().get(i);
            TextSpan actualSpan = result.getDiagnostics().get(i).getSpan();
            

            // Ensure spans match by values, not object reference
            assertEquals(expectedSpan.getStart(), actualSpan.getStart(), "TextSpan start position mismatch!");
            assertEquals(expectedSpan.getLength(), actualSpan.getLength(), "TextSpan length mismatch!");
       
        }
    }
}
