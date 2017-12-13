public class AstProgram extends AstNode {
    private AstExpression expr;

    public AstProgram(AstExpression expr) {
        this.expr = expr;
    }

    public AstExpression evaluate() {
        AstExpression initialResult = expr.evaluate();
        AstExpression simplifiedResult = initialResult.evaluate();

        while(!simplifiedResult.equals(initialResult)) {
            initialResult = simplifiedResult;
            simplifiedResult = initialResult.evaluate();
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
