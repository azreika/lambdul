import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

public class AstEnvironment {
    Set<String> boundVariables;

    Map<String, AstExpression> macroToValueMap;

    public AstEnvironment() {
        boundVariables = new HashSet<String>();
        macroToValueMap = new HashMap<String, AstExpression>();
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

    public void addAssignment(AstAssignment assignment) {
        // macro -> value, for evaluation
        macroToValueMap.put(assignment.getMacro().getName(), assignment.getValue());
        // TODO: add in value -> macro
    }

    public AstExpression getMacroValue(AstMacro macro) {
        return macroToValueMap.get(macro.getName());
    }

    @Override
    public String toString() {
        String result = "Environment: {\n";
        result += "Bound variables: " + boundVariables.toString() + "\n";
        result += "Macros: " + macroToValueMap.toString() + "\n";
        result += "}";
        return result;
    }
}
