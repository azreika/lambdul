public class AstVariable extends AstExpression {
    private String name;

    public AstVariable(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public AstExpression evaluate(AstEnvironment env) {
        return this.clone();
    }

    public String toString() {
        return this.name;
    }

    public AstExpression substitute(AstVariable var, AstExpression expr, AstEnvironment env) {
        if(var.getName().equals(name)) {
            return expr.clone();
        } else {
            return this.clone();
        }
    }

    public AstVariable clone() {
        return new AstVariable(name);
    }
}
