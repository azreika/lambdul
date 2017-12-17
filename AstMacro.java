public class AstMacro extends AstExpression {
    String name;

    public AstMacro(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public AstExpression evaluate(AstEnvironment env) {
        // TODO: throw evaluation errors
        return this.getValue(env).clone();
    }

    public AstExpression substitute(AstVariable var, AstExpression expr, AstEnvironment env) {
        return this.getValue(env).substitute(var, expr, env);
    }

    public AstExpression getValue(AstEnvironment env) {
        return env.getMacroValue(this);
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
