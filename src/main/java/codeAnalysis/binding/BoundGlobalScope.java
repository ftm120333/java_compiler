package codeAnalysis.binding;

import codeAnalysis.compiling.DiagnosticBag;

public class BoundGlobalScope {
    BoundGlobalScope previous;
    DiagnosticBag diagnostics;
    BoundScope variables;
    BoundExpression expression;

    public BoundGlobalScope(BoundGlobalScope previous, DiagnosticBag diagnostics, BoundScope variables, BoundExpression expression) {
        this.previous = previous;
        this.diagnostics = diagnostics;
        this.variables = variables;
        this.expression = expression;
    }


    public BoundGlobalScope getPrevious() {
        return previous;
    }

    public DiagnosticBag getDiagnostics() {
        return diagnostics;
    }

    public BoundScope getVariables() {
        return variables;
    }

    public BoundExpression getExpression() {
        return expression;
    }
}
