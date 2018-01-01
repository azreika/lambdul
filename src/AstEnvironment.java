import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

public class AstEnvironment {
    private Set<String> boundVariables;
    private Map<String, AstExpression> macroToValueMap;

    public AstEnvironment() {
        boundVariables = new HashSet<String>();
        macroToValueMap = new HashMap<String, AstExpression>();
    }

    /**
     * Check if a variable is already bound.
     *
     * @param   var the name of the variable to check
     * @return      true if the variable is bound
     */
    public boolean isBound(String var) {
        // TODO: change these to take variables instead of strings
        return this.boundVariables.contains(var);
    }

    /**
     * Get a new unbound variable based on a given variable.
     *
     * @param   var the name of the variable to rename to an unbound version
     * @return      a new unbound variable based on var's name
     */
    public AstVariable renameVariable(String var) {
        String nextVar = var;
        while (this.isBound(nextVar)) {
            nextVar += "'";
        }
        return new AstVariable(nextVar);
    }

    /**
     * Bind a variable.
     *
     * @param   var the name of the variable to bind
     */
    public void bindVariable(String var) {
        boundVariables.add(var);
    }

    /**
     * Unbind a variable.
     *
     * @param   var the name of the variable to free
     */
    public void freeVariable(String var) {
        boundVariables.remove(var);
    }

    /**
     * Add a new macro assignment to the environment.
     *
     * @param   assignment  the assignment to add
     */
    public void addAssignment(AstAssignment assignment) {
        // TODO: add in value -> macro substitution?
        // macro -> value map, used for evaluation
        macroToValueMap.put(assignment.getMacro().getName(), assignment.getValue());
    }

    /**
     * Get the expression associated with a given macro.
     *
     * @param   macro   the macro to check
     */
    public AstExpression getMacroValue(AstMacro macro) throws EvaluationException {
        AstExpression matchedException = macroToValueMap.get(macro.getName());
        if (matchedException == null) {
            throw new EvaluationException(EvaluationErrorType.MACRO_LOOKUP_ERROR, macro, "undefined macro value");
        }
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
