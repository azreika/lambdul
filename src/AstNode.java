public abstract class AstNode {
    /**
     * Evaluates the node.
     *
     * @param   env the current program environment
     * @return      the resultant node after evaluation
     */
    public abstract AstNode evaluate(AstEnvironment env) throws EvaluationException;

    /**
     * Clones the node.
     *
     * @return a deep copy of the node
     */
    public abstract AstNode clone();

    @Override
    public boolean equals(Object other) {
        // AstProgram
        if (this instanceof AstProgram && other instanceof AstProgram) {
            AstProgram first = (AstProgram) this;
            AstProgram second = (AstProgram) other;

            return first.getRoot().equals(second.getRoot());
        }

        // AstVariable
        if (this instanceof AstVariable && other instanceof AstVariable) {
            return ((AstVariable) this).getName().equals(((AstVariable) other).getName());
        }

        // AstApplication
        if (this instanceof AstApplication && other instanceof AstApplication) {
            AstApplication first = (AstApplication) this;
            AstApplication second = (AstApplication) other;

            boolean leftEqual = first.getLeft().equals(second.getLeft());
            boolean rightEqual = first.getRight().equals(second.getRight());
            return leftEqual && rightEqual;
        }

        // AstAbstraction
        if (this instanceof AstAbstraction && other instanceof AstAbstraction) {
            // TODO: deeper equality (independent of variable names)
            AstAbstraction first = (AstAbstraction) this;
            AstAbstraction second = (AstAbstraction) other;

            boolean varsEqual = first.getVariable().equals(second.getVariable());
            boolean bodiesEqual = first.getBody().equals(second.getBody());

            return varsEqual && bodiesEqual;
        }

        // AstAssignment
        if (this instanceof AstAssignment && other instanceof AstAssignment) {
            AstAssignment first = (AstAssignment) this;
            AstAssignment second = (AstAssignment) other;

            boolean macrosEqual = first.getMacro().equals(second.getMacro());
            boolean valuesEqual = first.getValue().equals(second.getValue());

            return macrosEqual && valuesEqual;
        }

        // AstMacro
        if (this instanceof AstMacro && other instanceof AstMacro) {
            return ((AstMacro) this).getName().equals(((AstMacro) other).getName());
        }

        return false;
    }
}
