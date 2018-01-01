public class AstCommandAssign extends AstCommand {
    private AstMacro macro;
    private AstExpression value;

    public AstCommandAssign(AstMacro macro, AstExpression value) {
        super("assign");
        this.macro = macro;
        this.value = value;
    }

    @Override
    public AstAssignment evaluate(AstEnvironment env) throws EvaluationException {
        AstAssignment correspondingAssignment = new AstAssignment(macro, value);
        return correspondingAssignment.evaluate(env);
    }

    @Override
    public AstCommandAssign clone() {
        return new AstCommandAssign(this.macro.clone(), this.value.clone());
    }

    @Override
    public String toString() {
        return "@assign " + macro.toString() + " " + value.toString();
    }
}
