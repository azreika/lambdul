public class AstApplication extends AstExpression {
    private AstExpression leftExpr;
    private AstExpression rightExpr;

    public AstApplication(AstExpression leftExpr, AstExpression rightExpr) {
        this.leftExpr = leftExpr;
        this.rightExpr = rightExpr;
    }

    /**
     * Get the left subexpression.
     *
     * @return  the LHS of the application
     */
    public AstExpression getLeft() {
        return this.leftExpr;
    }

    /**
     * Get the right subexpression
     *
     * @return  the RHS of the application
     */
    public AstExpression getRight() {
        return this.rightExpr;
    }

    @Override
    public boolean usesFreeVariable(AstEnvironment env, AstVariable var) throws EvaluationException {
        return leftExpr.usesFreeVariable(env, var) || rightExpr.usesFreeVariable(env, var);
    }

    @Override
    public AstExpression evaluate(AstEnvironment env) throws EvaluationException {
        // Evaluate both subexpressions first
        AstExpression evaluatedRight = rightExpr.evaluate(env);
        AstExpression evaluatedLeft = leftExpr.evaluate(env);

        if (evaluatedLeft instanceof AstAbstraction) {
            // Have an abstraction on the left, so we need to substitute!
            AstAbstraction leftLambda = (AstAbstraction) evaluatedLeft;
            AstVariable argument = leftLambda.getVariable();

            // Replace all free occurrences of the argument in the left-hand side's body
            // with the right hand side expression
            return leftLambda.getBody().substitute(argument, evaluatedRight, env).evaluate(env);
        } else {
            // No substitution needed, so move on
            return new AstApplication(evaluatedLeft, evaluatedRight);
        }
    }

    @Override
    public AstExpression substitute(AstVariable var, AstExpression expr, AstEnvironment env) throws EvaluationException {
        // Substitute on both sides of the application
        AstExpression subbedLeft = leftExpr.substitute(var, expr, env).evaluate(env);
        AstExpression subbedRight = rightExpr.substitute(var, expr, env).evaluate(env);

        return new AstApplication(subbedLeft, subbedRight);
    }

    @Override
    public String toString() {
        return "(" + leftExpr.toString() + " " + rightExpr.toString() + ")";
    }

    @Override
    public AstApplication clone() {
        return new AstApplication(leftExpr.clone(), rightExpr.clone());
    }
}
