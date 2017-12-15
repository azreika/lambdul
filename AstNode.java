public abstract class AstNode {
    public abstract AstExpression evaluate(Environment env);
    public abstract AstNode clone();
}
