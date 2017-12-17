public abstract class AstExpression extends AstNode {
    public abstract AstExpression substitute(AstVariable var, AstExpression expr, Environment env);

    @Override
    public abstract AstExpression evaluate(Environment env);

    @Override
    public abstract AstExpression clone();

    @Override
    public boolean equals(Object other) {
        if (this instanceof AstVariable && other instanceof AstVariable) {
            return ((AstVariable) this).getName().equals(((AstVariable) other).getName());
        }

        if (this instanceof AstApplication && other instanceof AstApplication) {
            AstApplication first = (AstApplication) this;
            AstApplication second = (AstApplication) other;

            boolean leftEqual = first.getLeft().equals(second.getLeft());
            boolean rightEqual = first.getRight().equals(second.getRight());
            return leftEqual && rightEqual;
        }

        if (this instanceof AstAbstraction && other instanceof AstAbstraction) {
            // TODO: deeper equality (independent of variable names)
            AstAbstraction first = (AstAbstraction) this;
            AstAbstraction second = (AstAbstraction) other;

            boolean varsEqual = first.getVariable().equals(second.getVariable());
            boolean bodiesEqual = first.getBody().equals(second.getBody());

            return varsEqual && bodiesEqual;
        }

        return false;
    }
}
