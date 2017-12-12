public class AstProgram extends AstNode {
    private AstExpression expr;

    public AstProgram(AstExpression expr) {
        this.expr = expr;
    }

    public AstExpression evaluate() {
        return expr.evaluate();
    }
}
