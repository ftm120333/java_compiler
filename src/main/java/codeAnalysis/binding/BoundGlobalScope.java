package codeAnalysis.binding;

import codeAnalysis.VariableSymbol;
import codeAnalysis.compiling.Diagnostic;
import codeAnalysis.compiling.DiagnosticBag;

import java.util.List;

public class BoundGlobalScope {
    BoundGlobalScope previous;
    List<Diagnostic> diagnostics;
    List<VariableSymbol> variables;
    BoundExpression expression;

    public BoundGlobalScope(BoundGlobalScope previous, List<Diagnostic> diagnostics, List<VariableSymbol> variables, BoundExpression expression) {
        this.previous = previous;
        this.diagnostics = diagnostics;
        this.variables = variables;
        this.expression = expression;
    }


    public BoundGlobalScope getPrevious() {
        return previous;
    }

    public List<Diagnostic> getDiagnostics() {
        return diagnostics;
    }

    public List<VariableSymbol> getVariables() {
        return variables;
    }

    public BoundExpression getExpression() {
        return expression;
    }
}
