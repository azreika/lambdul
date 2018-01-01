public class AstCommandEvaluate extends AstCommand {
    private AstExpression expr;

    public AstCommandEvaluate(AstExpression expr) {
        super("evaluate");
        this.expr = expr;
    }

    @Override
    public AstExpression evaluate(AstEnvironment env) throws EvaluationException {
        return this.expr.evaluate(env);
    }

    @Override
    public AstCommandEvaluate clone() {
        return new AstCommandEvaluate(this.expr.clone());
    }

    @Override
    public String toString() {
        return "@evaluate " + expr.toString();
    }
}
