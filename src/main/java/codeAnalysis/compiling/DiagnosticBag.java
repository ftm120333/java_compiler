package codeAnalysis.compiling;

import codeAnalysis.syntax.SyntaxKind;
import codeAnalysis.text.TextSpan;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class DiagnosticBag implements Iterable<Diagnostic> {
    private final List<Diagnostic> _diagnostics = new ArrayList<>();

    @Override
    public Iterator<Diagnostic> iterator() {
        return _diagnostics.iterator();
    }
    public List<Diagnostic> get_diagnostics() {
        return _diagnostics;
    }

    public void addRange(DiagnosticBag diagnostic) {
        _diagnostics.addAll(diagnostic.get_diagnostics());
    }
    private void report(TextSpan span, String message) {
        Diagnostic diagnostic = new Diagnostic(span, message);
        _diagnostics.add(diagnostic);
    }

  public void reportInvalidNumber(TextSpan span, String text, Class<?> type) {
        var message = "The number " + text + " is not a valid " + type.getSimpleName() + ".";
        report(span,message);

  }

    public void reportBadCharacter(int position, char character) {
        var span = new TextSpan(position, 1);
        var message = "Bad character input: " + character + ".";
        report(span, message);
    }

    public void reportUnexpectedToken(TextSpan span, SyntaxKind actualKind, SyntaxKind expectedKind) {
        var message = "Unexpected token <" + actualKind + ">. Expected <" + expectedKind + ">.";
        report(span, message);
    }

    public void reportUndefinedUnaryOperator(TextSpan span, String operatorText, Class<?> operandType) {
        var message = "The unary operator " + operatorText + " is not defined for type " + operandType + ".";
        report(span, message);
    }

    public void addUndefinedBinaryOperator(TextSpan span, String operatorText, Class<?> leftType, Class<?> rightType) {
        var message = "The binary operator " + operatorText + " is not defined for types " + leftType + " and " + rightType + ".";
        report(span, message);

    }

    public void ReportUndefinedName(TextSpan span, String name) {
        var message = "Variable  " + name + " does not exist "+ ".";
        report(span, message);
    }

    public void ReportVariableAlreadyDeclared(TextSpan span, String name) {
        var message = "Variable  " + name + " already declared "+ ".";
        report(span, message);

    }
}
