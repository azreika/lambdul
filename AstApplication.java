public class AstApplication extends AstExpression {
    private AstExpression leftExpr;
    private AstExpression rightExpr;

    public AstApplication(AstExpression leftExpr, AstExpression rightExpr) {
        this.leftExpr = leftExpr;
        this.rightExpr = rightExpr;
    }

    public AstExpression evaluate() {
        // TODO: evaluation
        return this;
    }
}
