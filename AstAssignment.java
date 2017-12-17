public class AstAssignment extends AstNode {
    AstMacro macro;
    AstExpression value;

    public AstAssignment(AstMacro macro, AstExpression value) {
        this.macro = macro;
        this.value = value;
    }

    public AstAssignment evaluate(Environment env) {
        env.addAssignment(this);
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
