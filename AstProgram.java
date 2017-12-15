public class AstProgram extends AstNode {
    private AstExpression expr;

    public AstProgram(AstExpression expr) {
        this.expr = expr;
    }

    public AstExpression evaluate() {
        Environment env = new Environment();
        return this.evaluate(env);
    }

    public AstExpression evaluate(Environment env) {
        AstExpression initialResult = expr.evaluate(env);
        AstExpression simplifiedResult = initialResult.evaluate(env);

        while(!simplifiedResult.equals(initialResult)) {
            initialResult = simplifiedResult;
            simplifiedResult = initialResult.evaluate(env);
        }

        return simplifiedResult;
    }

    public String toString() {
        return expr.toString();
    }

    public AstProgram clone() {
        return new AstProgram(expr.clone());
    }
}
