package codeAnalysis.compiling;

import java.util.ArrayList;

public final class EvaluationResult {

    final Iterable<Diagnostic> diagnostics;
    Object value;

    public EvaluationResult(ArrayList<Object> diagnostics, Object value) {
        this.diagnostics = diagnostics;
        this.value = value;
    }

    public Iterable<Diagnostic> getDiagnostics() {
        return diagnostics;
    }

    public Object getValue() {
        return value;
    }
}
