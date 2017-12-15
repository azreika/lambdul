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
            return leftLambda.getBody().substitute(argument, evaluatedRight);
        } else {
            return this.clone();
        }
    }

    public String toString() {
        return "(" + leftExpr.toString() + " " + rightExpr.toString() + ")";
    }

    public AstExpression substitute(AstVariable var, AstExpression expr) {
        AstExpression subbedLeft = leftExpr.substitute(var, expr);
        AstExpression subbedRight = rightExpr.substitute(var, expr);
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
