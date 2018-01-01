public abstract class AstCommand extends AstNode {
    private String commandName;

    public AstCommand(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return this.commandName;
    }
}
