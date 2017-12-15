import java.util.Set;
import java.util.HashSet;

// TODO: rename to AstEnvironmnet
public class Environment {
    Set<String> boundVariables;

    public Environment() {
        boundVariables = new HashSet<String>();
    }

    // TODO: change these to take variables instead of strings
    public boolean isBound(String var) {
        return this.boundVariables.contains(var);
    }

    public AstVariable renameVariable(String var) {
        String nextVar = var;
        while (this.isBound(nextVar)) {
            nextVar += "'";
        }
        return new AstVariable(nextVar);
    }

    public void bindVariable(String var) {
        boundVariables.add(var);
    }

    // TODO: rename to unbindvariable?
    public void freeVariable(String var) {
        boundVariables.remove(var);
    }

    @Override
    public String toString() {
        String result = "Environment: {\n";
        result += "Bound variables: " + boundVariables.toString();
        result += "}";
        return result;
    }
}
