public class AstMacro extends AstExpression {
    String name;

    public AstMacro(String name) {
        this.name = name;
    }

    /**
     * Get the identifier name of the macro.
     *
     * @return  the name of the macro
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the expression associated with the macro.
     *
     * @param env   the current program environment
     */
    public AstExpression getValue(AstEnvironment env) throws EvaluationException {
        return env.getMacroValue(this);
    }

    @Override
    public boolean usesFreeVariable(AstEnvironment env, AstVariable var) throws EvaluationException {
        return this.getValue(env).usesFreeVariable(env, var);
    }

    @Override
    public AstExpression evaluate(AstEnvironment env) throws EvaluationException {
        // TODO: possibly just treat as a variable if undefined?
        // Get the associated expression from the environment
        return this.getValue(env).clone();
    }

    @Override
    public AstExpression substitute(AstVariable var, AstExpression expr, AstEnvironment env) throws EvaluationException {
        // TODO: should this ever be reached?
        // Return the substituted version of the associated expression
        return this.getValue(env).substitute(var, expr, env);
    }

    @Override
    public AstMacro clone() {
        return new AstMacro(this.name);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
