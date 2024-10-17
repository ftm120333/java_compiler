package codeAnalysis.binding;

import codeAnalysis.VariableSymbol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoundScope {
    private final BoundScope parent;
    private Map<String, VariableSymbol> _variables =  new HashMap<>();

    public BoundScope(BoundScope parent) {
        this.parent = parent;
    }

    public BoundScope getParent() {
        return parent;
    }

    public boolean tryDeclare(VariableSymbol variable) {
        if (_variables.containsKey(variable.getName()))
            return false;
        _variables.put(variable.getName(), variable);
        return true;
    }

    public boolean tryLookup(String name, VariableSymbol variable) {
        if(_variables.containsKey(name)) {
            return true;
        }
        if(parent != null)
            return parent.tryLookup(name, variable);
        return false;
    }


  public List getDeclareVariables() {
    return List.of(_variables.values().toArray());
  }
}
