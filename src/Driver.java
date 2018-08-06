/**
 * REPL driver for a lambda interpreter.
 */

import java.util.Scanner;

import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;

public class Driver {
    public static void main(String[] args) {
        // Set up the environment
        AstEnvironment env = new AstEnvironment();

        // Start off the REPL
        boolean exit = false;
        String prompt = "> ";
        LineReader reader = LineReaderBuilder.builder().build();

        // Disable escape characters when reading input
        reader.setOpt(LineReader.Option.DISABLE_EVENT_EXPANSION);

        while(!exit) {
            // Read in user input
            String lineInput = "";
            try {
                lineInput = reader.readLine(prompt);
            } catch (UserInterruptException | EndOfFileException e) {
                break;
            }

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
                    // Wait for next input
                    System.out.println();
                }
            }
        }
    }
}
