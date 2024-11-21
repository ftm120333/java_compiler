package codeAnalysis.binding;

import codeAnalysis.VariableSymbol;
import codeAnalysis.compiling.Diagnostic;
import codeAnalysis.compiling.DiagnosticBag;

import java.util.List;

public class BoundGlobalScope {
    BoundGlobalScope previous;
    List<Diagnostic> diagnostics;
    List<VariableSymbol> variables;
    BoundStatement statement;

    public BoundGlobalScope(BoundGlobalScope previous, List<Diagnostic> diagnostics, List<VariableSymbol> variables, BoundStatement statement) {
        this.previous = previous;
        this.diagnostics = diagnostics;
        this.variables = variables;
        this.statement = statement;
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

    public BoundStatement getStatement() {
        return statement;
    }
}
