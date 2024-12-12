package codeAnalysis.compiling;

import codeAnalysis.Evaluator;
import codeAnalysis.VariableSymbol;
import codeAnalysis.binding.Binder;
import codeAnalysis.binding.BoundGlobalScope;
import codeAnalysis.syntax.SyntaxTree;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Compilation {
    Compilation previous;
    SyntaxTree syntaxTree;

    private BoundGlobalScope _globalScope;
    public Compilation(SyntaxTree syntaxTree) {

        this.syntaxTree = syntaxTree;
        this.previous = null;
    }

    private Compilation(Compilation previous, SyntaxTree syntaxTree ){
        this.previous = previous;
        this.syntaxTree = syntaxTree;

    }

    public SyntaxTree getSyntaxTree() {
        return syntaxTree;
    }

    ///minute 50 - 53 of episode 6 check for threading safety
    private BoundGlobalScope getGlobalScope() {
        if(_globalScope == null) {

            _globalScope = Binder.bindGlobalScope(previous != null ? previous._globalScope : null , syntaxTree.getRoot());
        }

        return _globalScope;

    }

     public Compilation continueWith(SyntaxTree syntaxTree) {
         return new Compilation(this, syntaxTree);
     }


    public EvaluationResult Evaluate(Map<VariableSymbol, Object> variables) throws Exception {

        var diagnostics = syntaxTree.getDiagnostics();

        diagnostics.addAll(getGlobalScope().getDiagnostics());
        if (!diagnostics.isEmpty()) {

           return new EvaluationResult((ArrayList) diagnostics, null);
       }
       var evaluator = new Evaluator(getGlobalScope().getStatement(), variables);

       var value = evaluator.Evaluate();
        ArrayList ArrayList = new ArrayList();
        return new EvaluationResult(ArrayList, value);
    }

}

