/**
 * REPL driver for a lambda interpreter.
 */

import java.util.Scanner;

public class Driver {
    public static void main(String[] args) {
        // Set up the environment
        AstEnvironment env = new AstEnvironment();

        // Start off the REPL
        boolean exit = false;
        Scanner keyboard = new Scanner(System.in);

        System.out.print("> ");
        while(!exit && keyboard.hasNextLine()) {
            String lineInput = keyboard.nextLine();
            Parser parser = new Parser(lineInput);
            try {
                // Parse the program
                AstProgram program = parser.parseProgram();

                // Evaluate the resultant expression
                AstNode result = program.evaluate(env);

                // Print the reduced lambda expression
                System.out.println(result);
            } catch (ParseException pe) {
                System.out.println("Parsing error: " + pe.getMessage());
            } catch (EvaluationException ee) {
                if (ee.getType() == EvaluationErrorType.EXIT) {
                    System.out.println("bye!");
                    exit = true;
                } else {
                    System.out.println("Evaluation error: " + ee.getMessage());
                }
            } catch (StackOverflowError soe) {
                System.out.println("Evaluation error: stack overflow");
            } finally {
                if (!exit) {
                    // Wait for the next input
                    System.out.println();
                    System.out.print("> ");
                }
            }
        }
    }
}
