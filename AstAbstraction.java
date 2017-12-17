public class AstAbstraction extends AstExpression {
    private AstVariable variable;
    private AstExpression body;

    public AstAbstraction(AstVariable variable, AstExpression body) {
        this.variable = variable;
        this.body = body;
    }

    /**
     * Get the argument of the abstraction.
     *
     * @return  the argument variable
     */
    public AstVariable getVariable() {
        // TODO: consider changing to getArgument()
        return this.variable;
    }

    /**
     * Get the body expression of the abstraction.
     *
     * @return  the expression in the body
     */
    public AstExpression getBody() {
        return this.body;
    }

    @Override
    public AstExpression evaluate(AstEnvironment env) {
        if (env.isBound(this.variable.getName())) {
            // Variable already being used - perform an alpha-reduction

            // Get a new unused variable name to use
            AstVariable newVariable = env.renameVariable(this.variable.getName());

            // Replace all body variables bound to this occurrence with the new name
            AstExpression newBody = this.body.substitute(this.variable, newVariable, env);

            // Evaluate the alpha-reduced expression instead
            AstExpression reducedExpression = new AstAbstraction(newVariable, newBody);
            AstExpression result = reducedExpression.evaluate(env);

            return result;
        }

        /* Check if an eta-reduction applies:
         *      \x.(f x) <=> f
         * Note that:
         *      -> The body must be an application where the RHS is the abstraction's argument
         *      -> f must be independent of x
         */
        if (this.body instanceof AstApplication) {
            AstApplication application = (AstApplication) this.body;
            if (application.getRight() instanceof AstVariable) {
                AstVariable subvariable = (AstVariable) application.getRight();
                if (subvariable.equals(this.variable)) {
                    // Check if f is independent of x
                    // f is independent of x <=> if we sub in another variable for x in f, f is unchanged
                    // TODO: more efficient way of doing this - make visitor patterns
                    AstVariable newVariable = new AstVariable(subvariable.getName() + "'");
                    AstExpression substitutedVersion = application.getLeft().substitute(subvariable, newVariable, env);
                    if (substitutedVersion.equals(application.getLeft())) {
                        // eta-application applies!
                        // Abstraction is of the form \x.(f x), so just evaluate f
                        return application.getLeft().evaluate(env);
                    }
                }
            }
        }

        // Bind the variable before evaluating
        env.bindVariable(this.variable.getName());

        // Evaluate the body
        AstExpression evaluatedBody = this.body.evaluate(env);

        // Free the variable - no longer being used
        env.freeVariable(this.variable.getName());

        return new AstAbstraction(this.variable.clone(), evaluatedBody);
    }

    @Override
    public AstExpression substitute(AstVariable var, AstExpression expr, AstEnvironment env) {
        if (var.equals(this)) {
            // Variable names overlap - don't go any further
            return this.clone();
        } else {
            // Bindings do not change otherwise, so just substitute in the body
            return new AstAbstraction(this.variable.clone(), this.body.substitute(var, expr, env));
        }
    }


    @Override
    public String toString() {
        return "λ" + variable.toString() + ".(" + body.toString() + ")";
    }

    @Override
    public AstAbstraction clone() {
        return new AstAbstraction(this.variable.clone(), this.body.clone());
    }
}
