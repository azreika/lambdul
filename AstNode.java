public abstract class AstNode {
    public abstract AstNode evaluate(Environment env);
    public abstract AstNode clone();
}
