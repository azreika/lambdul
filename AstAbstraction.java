public class AstAbstraction extends AstExpression {
    private AstVariable variable;
    private AstExpression body;

    public AstAbstraction(AstVariable variable, AstExpression body) {
        this.variable = variable;
        this.body = body;
    }

    public AstExpression evaluate() {
        return new AstAbstraction(this.variable.clone(), this.body.evaluate());
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

    public AstExpression substitute(AstVariable var, AstExpression expr) {
        if(var.getName().equals(this.variable.getName())) {
            // overlapping bindings - don't go any further
            // TODO: should this be cloned?
            return this.clone();
        } else {
            return new AstAbstraction(this.variable.clone(), this.body.substitute(var, expr));
        }
    }

    public AstAbstraction clone() {
        return new AstAbstraction(this.variable.clone(), this.body.clone());
    }
}
