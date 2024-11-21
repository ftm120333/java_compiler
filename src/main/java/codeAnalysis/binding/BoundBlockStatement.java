package codeAnalysis.binding;

import java.util.List;

public class BoundBlockStatement extends BoundStatement {

    private final List<BoundStatement> statements;

    public BoundBlockStatement(List<BoundStatement> statements){

        this.statements = statements;

    }

    public BoundNodeKind getKind() {
        return BoundNodeKind.BlockStatement;
    }

    public List<BoundStatement> getStatements() {
        return statements;
    }


}
