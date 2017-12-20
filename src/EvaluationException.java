@SuppressWarnings("serial")
public class EvaluationException extends Exception {
    public final EvaluationErrorType errorType;

    public EvaluationException(String message) {
        super(message);
        this.errorType = EvaluationErrorType.OTHER;
    }

    public EvaluationException(AstExpression expression, String message) {
        this(EvaluationErrorType.OTHER, expression, message);
    }

    public EvaluationException(EvaluationErrorType type, AstExpression expression, String message) {
        super(message + " when evaluating '" + expression + "'");
        this.errorType = type;
    }
}
