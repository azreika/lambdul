/**
 * REPL Driver for the interpreter
 */

import java.util.Scanner;

public class Driver {
    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        while(keyboard.hasNextLine()) {
            String lineInput = keyboard.nextLine();
            AstProgram program = parseProgram(lineInput);
            AstExpression result = program.evaluate();
            System.out.println(result);
        }
    }

    public static AstProgram parseProgram(String input) {
        return new AstProgram(parseExpression(input));
    }

    public static AstExpression parseExpression(String input) {
        // TODO: parsing
    }
}
