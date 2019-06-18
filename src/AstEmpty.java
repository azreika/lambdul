public class AstEmpty extends AstNode {
    public AstEmpty() {
    }

    @Override
    public AstEmpty evaluate(AstEnvironment env) {
        return this;
    }

    @Override
    public AstEmpty clone() {
        return new AstEmpty();
    }

    @Override
    public String toString() {
        return "";
    }
}
