package codeAnalysis.compiling;

public final class EvaluationResult {

    final Iterable<Diagnostic> diagnostics;
    Object value;

    public EvaluationResult(Iterable<Diagnostic> diagnostics, Object value) {
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
