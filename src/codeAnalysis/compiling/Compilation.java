package codeAnalysis.compiling;

import codeAnalysis.Evaluator;
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

    public EvaluationResult Evaluate(Map<String, Object> variables) throws Exception {
       var binder = new Binder(variables);
       var boundExpression = binder.bindExpression(syntax.getRoot());
        List<Diagnostic> diagnostics = new ArrayList<>();
        diagnostics.addAll(syntax.getDiagnostics());
        diagnostics.addAll(binder.diagnostics().get_diagnostics());

        if (!diagnostics.isEmpty()) {
           return new EvaluationResult(diagnostics, null);
       }
       var evaluator = new Evaluator(boundExpression, variables);
       var value = evaluator.Evaluate();
       return new EvaluationResult(Collections.emptyList(), value);
    }



}

