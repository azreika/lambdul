public class AstAssignment extends AstNode {
    private AstMacro macro;
    private AstExpression value;

    public AstAssignment(AstMacro macro, AstExpression value) {
        this.macro = macro;
        this.value = value;
    }

    @Override
    public AstAssignment evaluate(AstEnvironment env) {
        // Add the assignment to the program environment
        env.addAssignment(this);

        // The evaluated node is unchanged
        return this.clone();
    }

    public AstMacro getMacro() {
        return this.macro;
    }

    public AstExpression getValue() {
        return this.value;
    }

    @Override
    public AstAssignment clone() {
        return new AstAssignment(this.macro.clone(), this.value.clone());
    }

    @Override
    public String toString() {
        return macro.toString() + " := " + value.toString();
    }
}
