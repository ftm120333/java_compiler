package codeAnalysis.syntax;

import codeAnalysis.text.TextSpan;

import java.util.ArrayList;
import java.util.List;

public class SyntaxToken extends SyntaxNode{
    public final SyntaxKind kind;
    public final int position;
    public final String text;
    public final Object value;

    public SyntaxToken(SyntaxKind kind, int position, String text, Object value){

        this.kind = kind;
        this.position = position;
        this.text = text;
        this.value = value;
    }

    public TextSpan span() {
        
        return new TextSpan(position, text.length());
    }
    @Override
    public SyntaxKind getKind() {
        return kind;
    }


    public List<SyntaxNode> GetChildren() {
        return new ArrayList<SyntaxNode>();
    }
}
