public class AstVariable extends AstExpression {
    String name;

    public AstVariable(String name) {
        this.name = name;
    }

    public AstExpression evaluate() {
        return this;
    }
}
