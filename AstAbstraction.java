public class AstAbstraction extends AstExpression {
    private AstVariable variable;
    private AstExpression body;

    public AstAbstraction(AstVariable variable, AstExpression body) {
        this.variable = variable;
        this.body = body;
    }

    public AstExpression evaluate(Environment env) {
        if (env.isBound(this.variable.getName())) {
            // Variable already being used - perform an alpha-reduction
            AstVariable newVariable = env.renameVariable(this.variable.getName());
            AstExpression newBody = this.body.substitute(this.variable, newVariable, env);
            AstExpression reducedExpression = new AstAbstraction(newVariable, newBody);

            // Evaluate the alpha-reduced expression
            AstExpression result = reducedExpression.evaluate(env);

            return result;
        }

        // Check if an eta-reduction applies
        // \x.(f x) <=> f
        if (this.body instanceof AstApplication) {
            AstApplication application = (AstApplication) this.body;
            if (application.getRight() instanceof AstVariable) {
                AstVariable subvariable = (AstVariable) application.getRight();
                if (subvariable.getName().equals(this.variable.getName())) {
                    // eta-application applies!
                    // Abstraction is of the form \x.(f x), so just evaluate f
                    return application.getLeft().evaluate(env);
                }
            }
        }

        // Bind the variable
        env.bindVariable(this.variable.getName());

        // eta-reduction does not apply; evaluate the full body
        AstExpression evaluatedBody = this.body.evaluate(env);

        // Unbind the variable
        env.freeVariable(this.variable.getName());

        return new AstAbstraction(this.variable.clone(), evaluatedBody);
    }

    public String toString() {
        return "λ" + variable.toString() + ".(" + body.toString() + ")";
    }

    // TODO: should this be getArgument instead?
    public AstVariable getVariable() {
        return this.variable;
    }

    public AstExpression getBody() {
        return this.body;
    }

    public AstExpression substitute(AstVariable var, AstExpression expr, Environment env) {
        if(var.getName().equals(this.variable.getName())) {
            // overlapping bindings - don't go any further
            return this.clone();
        } else {
            return new AstAbstraction(this.variable.clone(), this.body.substitute(var, expr, env));
        }
    }

    public AstAbstraction clone() {
        return new AstAbstraction(this.variable.clone(), this.body.clone());
    }
}
