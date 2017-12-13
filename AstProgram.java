public class AstProgram extends AstNode {
    private AstExpression expr;

    public AstProgram(AstExpression expr) {
        this.expr = expr;
    }

    public AstExpression evaluate() {
        return expr.evaluate();
    }

    public String toString() {
        return expr.toString();
    }

    public AstProgram clone() {
        return new AstProgram(expr.clone());
    }
}
