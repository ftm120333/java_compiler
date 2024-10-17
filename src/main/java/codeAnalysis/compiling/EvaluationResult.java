package codeAnalysis.compiling;

import java.util.ArrayList;
import java.util.List;

public final class EvaluationResult {

    final List diagnostics;
    Object value;

    public EvaluationResult(ArrayList diagnostics, Object value) {
        this.diagnostics = diagnostics;
        this.value = value;
    }

    public Iterable<Object> getDiagnostics() {
        return diagnostics;
    }

    public Object getValue() {
        return value;
    }
}
