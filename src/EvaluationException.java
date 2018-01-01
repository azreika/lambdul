@SuppressWarnings("serial")
public class EvaluationException extends Exception {
    private EvaluationErrorType errorType;

    public EvaluationException(EvaluationErrorType type, String message) {
        super(message);
        this.errorType = type;
    }

    public EvaluationException(String message) {
        this(EvaluationErrorType.OTHER, message);
    }

    public EvaluationException(AstExpression expression, String message) {
        this(EvaluationErrorType.OTHER, expression, message);
    }

    public EvaluationException(EvaluationErrorType type, AstExpression expression, String message) {
        super(message + " when evaluating '" + expression + "'");
        this.errorType = type;
    }

    public EvaluationErrorType getType() {
        return this.errorType;
    }
}
