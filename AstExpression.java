public abstract class AstExpression extends AstNode {
    public abstract AstExpression substitute(AstVariable var, AstExpression expr);

    @Override
    public abstract AstExpression clone();
}
