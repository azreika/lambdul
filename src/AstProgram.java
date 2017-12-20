public class AstProgram extends AstNode {
    private AstNode root;

    public AstProgram(AstNode root) {
        this.root = root;
    }

    /**
     * Evaluates a program assuming a clean environment.
     *
     * @return  the evaluated program assuming an empty environment
     */
    public AstNode evaluate() throws EvaluationException {
        return this.evaluate(new AstEnvironment());
    }

    /**
     * Get the root node of the program.
     *
     * @return  the root node
     */
    public AstNode getRoot() {
        return this.root;
    }

    @Override
    public AstNode evaluate(AstEnvironment env) throws EvaluationException {
        if (root instanceof AstExpression) {
            AstExpression expr = (AstExpression) root;

            // Continuously try to simplify the expression until we reach a fixed pont.
            AstExpression initialResult = expr.evaluate(env);
            AstExpression simplifiedResult = initialResult.evaluate(env);

            while(!simplifiedResult.equals(initialResult)) {
                // Fixed point not reached yet - try to simplify
                initialResult = simplifiedResult;
                simplifiedResult = initialResult.evaluate(env);
            }

            // Fixed point reached!
            return simplifiedResult;
        } else {
            // TODO: might be able to just shift this out
            // Not an expression, so just need to evaluate it once
            return root.evaluate(env);
        }
    }

    @Override
    public String toString() {
        return root.toString();
    }

    @Override
    public AstProgram clone() {
        return new AstProgram(root.clone());
    }
}
