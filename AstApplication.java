public class AstApplication extends AstExpression {
    private AstExpression leftExpr;
    private AstExpression rightExpr;

    public AstApplication(AstExpression leftExpr, AstExpression rightExpr) {
        this.leftExpr = leftExpr;
        this.rightExpr = rightExpr;
    }

    public AstExpression evaluate(Environment env) {
        AstExpression evaluatedLeft = leftExpr.evaluate(env);
        AstExpression evaluatedRight = rightExpr.evaluate(env);

        if (evaluatedLeft instanceof AstAbstraction) {
            AstAbstraction leftLambda = (AstAbstraction) evaluatedLeft;
            AstVariable argument = leftLambda.getVariable();
            return leftLambda.getBody().substitute(argument, evaluatedRight, env);
        } else {
            return new AstApplication(evaluatedLeft, evaluatedRight);
        }
    }

    public String toString() {
        return "(" + leftExpr.toString() + " " + rightExpr.toString() + ")";
    }

    public AstExpression substitute(AstVariable var, AstExpression expr, Environment env) {
        AstExpression subbedLeft = leftExpr.substitute(var, expr, env);
        AstExpression subbedRight = rightExpr.substitute(var, expr, env);
        return new AstApplication(subbedLeft, subbedRight);
    }

    public AstExpression getLeft() {
        return this.leftExpr;
    }

    public AstExpression getRight() {
        return this.rightExpr;
    }

    public AstApplication clone() {
        return new AstApplication(leftExpr.clone(), rightExpr.clone());
    }
}
