public class AstAbstraction extends AstExpression {
    private AstVariable variable;
    private AstExpression body;

    public AstAbstraction(AstVariable variable, AstExpression body) {
        this.variable = variable;
        this.body = body;
    }

    public AstExpression evaluate() {
        // TODO: evaluation
    }
}
