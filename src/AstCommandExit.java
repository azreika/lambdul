public class AstCommandExit extends AstCommand {
    public AstCommandExit() {
        super("exit");
    }

    @Override
    public AstNode evaluate(AstEnvironment env) throws EvaluationException {
        // TODO: possibly avoid throwing an exception?
        throw new EvaluationException(EvaluationErrorType.EXIT, "exit command reached");
    }

    @Override
    public AstCommandExit clone() {
        return new AstCommandExit();
    }

    @Override
    public String toString() {
        return "@exit";
    }
}
