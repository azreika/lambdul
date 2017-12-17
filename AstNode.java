public abstract class AstNode {
    public abstract AstNode evaluate(AstEnvironment env);
    public abstract AstNode clone();
}
