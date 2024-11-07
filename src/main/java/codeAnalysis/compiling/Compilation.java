package codeAnalysis.compiling;

import codeAnalysis.Evaluator;
import codeAnalysis.VariableSymbol;
import codeAnalysis.binding.Binder;
import codeAnalysis.syntax.SyntaxTree;

import java.util.*;

public class Compilation {
    SyntaxTree syntax;

    public Compilation(SyntaxTree syntax) {
        this.syntax = syntax;
    }

    public SyntaxTree getSyntax() {
        return syntax;
    }

    public EvaluationResult Evaluate(Map<VariableSymbol, Object> variables) throws Exception {
       var globalScope = Binder.bindGlobalScope(syntax.getRoot());
        var diagnostics = new ArrayList<>();
        diagnostics.addAll(syntax.getDiagnostics());
        if (!diagnostics.isEmpty()) {
           return new EvaluationResult(diagnostics, null);
       }
       var evaluator = new Evaluator(globalScope.getExpression(), variables);
       var value = evaluator.Evaluate();
       return new EvaluationResult((ArrayList<Object>) Collections.emptyList(), value);
    }

}

