public abstract class AstExpression extends AstNode {
    /**
     * Substitutes all free occurrences of a given variable with an expression.
     *
     * @param   var     the variable to replace
     * @param   expr    the expression to replace the variable with
     * @param   env     the current program environment
     * @return          an expression with all free occurrences of the variable replaced
     */
    public abstract AstExpression substitute(AstVariable var, AstExpression expr, AstEnvironment env) throws EvaluationException;

    /**
     * Checks if the expression uses a given free variable.
     * The environment is needed for macro-lookup.
     *
     * @param   var the free variable to check for
     * @param   env the current program environment
     * @return      true if the variable is used as a free variable
     */
    public abstract boolean usesFreeVariable(AstEnvironment env, AstVariable var) throws EvaluationException;

    @Override
    public abstract AstExpression evaluate(AstEnvironment env) throws EvaluationException;

    @Override
    public abstract AstExpression clone();
}
