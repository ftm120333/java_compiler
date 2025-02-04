package codeAnalysis.compiling;
import java.util.List;

public final class EvaluationResult {

    final List<Diagnostic>  diagnostics;
    Object value;

    public EvaluationResult(List<Diagnostic>  diagnostics, Object value) {
        this.diagnostics = diagnostics;
        this.value = value;
    }

    public List<Diagnostic> getDiagnostics() {
        return diagnostics;
    }

    public Object getValue() {
        return value;
    }
}
