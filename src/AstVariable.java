public class AstVariable extends AstExpression {
    private String name;

    public AstVariable(String name) {
        this.name = name;
    }

    /**
     * Returns the identifier name of the variable.
     *
     * @return  the variable name
     */
    public String getName() {
        return this.name;
    }

    @Override
    public boolean usesFreeVariable(AstEnvironment env, AstVariable var) {
        return this.equals(var);
    }

    @Override
    public AstExpression evaluate(AstEnvironment env) {
        // Variables are already at their most evaluated form
        return this.clone();
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public AstExpression substitute(AstVariable var, AstExpression expr, AstEnvironment env) {
        if(var.getName().equals(name)) {
            // The variable is the one we are trying to substitute.
            // Return a clone of the given expression.
            return expr.clone();
        } else {
            // The variable is different, so nothing changes.
            return this.clone();
        }
    }

    @Override
    public AstVariable clone() {
        return new AstVariable(name);
    }
}
