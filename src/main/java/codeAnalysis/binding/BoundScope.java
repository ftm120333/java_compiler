package codeAnalysis.binding;

import codeAnalysis.VariableSymbol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoundScope {
    private final BoundScope parent;
    /*
        A map that stores the variables declared in the current scope, with the variable name
        (String) as the key and the VariableSymbol object as the value.
     */
    private Map<String, VariableSymbol> _variables =  new HashMap<>();

    public BoundScope(BoundScope parent) {
        this.parent = parent;
    }

    public BoundScope getParent() {
        return parent;
    }

    public boolean tryDeclare(VariableSymbol variable) {
        if (_variables.containsKey(variable.getName()))
            return false;  // Variable already exists in this scope.
        _variables.put(variable.getName(), variable);
        return true;
    }

    public boolean tryLookup(String name) {
        if(_variables.containsKey(name))
            return true; // Variable exists in the current scope.
        if(parent != null)
            return parent.tryLookup(name); // Recursively check parent scopes.
        return false;// Variable not found in any scope.
    }


  public List getDeclareVariables() {
    return List.copyOf(_variables.values());
  }
}
