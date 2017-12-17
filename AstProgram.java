public class AstProgram extends AstNode {
    private AstNode root;

    public AstProgram(AstNode root) {
        this.root = root;
    }

    public AstNode evaluate() {
        AstEnvironment env = new AstEnvironment();
        return this.evaluate(env);
    }

    public AstNode evaluate(AstEnvironment env) {
        if (root instanceof AstExpression) {
            AstExpression expr = (AstExpression) root;
            AstExpression initialResult = expr.evaluate(env);
            AstExpression simplifiedResult = initialResult.evaluate(env);

            while(!simplifiedResult.equals(initialResult)) {
                initialResult = simplifiedResult;
                simplifiedResult = initialResult.evaluate(env);
            }

            return simplifiedResult;
        } else {
            return root.evaluate(env);
        }
    }

    public String toString() {
        return root.toString();
    }

    public AstProgram clone() {
        return new AstProgram(root.clone());
    }
}
