public abstract class AstExpression extends AstNode {
    /**
     * Substitutes all free occurrences of a given variable with an expression.
     *
     * @param   var     the variable to replace
     * @param   expr    the expression to replace the variable with
     * @param   env     the current program environment
     * @return          an expression with all free occurrences of the variable replaced
     */
    public abstract AstExpression substitute(AstVariable var, AstExpression expr, AstEnvironment env);

    @Override
    public abstract AstExpression evaluate(AstEnvironment env);

    @Override
    public abstract AstExpression clone();
}
