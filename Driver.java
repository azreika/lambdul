/**
 * REPL driver for the interpreter
 */

import java.util.Scanner;

public class Driver {
    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        System.out.print("> ");
        while(keyboard.hasNextLine()) {
            String lineInput = keyboard.nextLine();
            Lexer lexer = new Lexer(lineInput);
            try {
                // Parse the program
                AstProgram program = parseProgram(lexer);

                // Evaluate the resultant expression
                AstExpression result = program.evaluate();

                // Print the reduced lambda expression
                System.out.println(result);
            } catch (ParseException pe) {
                // TODO: create a proper exception class
                System.out.println("Parsing error: " + pe.getMessage());
            } finally {
                // Wait for the next input
                System.out.println();
                System.out.print("> ");
            }
        }
    }

    /**
     * Parses an input program.
     * Grammar Rules:
     * P -> E, where E is an expression.
     */
    public static AstProgram parseProgram(Lexer input) throws ParseException {
        // P -> E

        // Parse E
        AstExpression expression = parseExpression(input);
        Token token = input.next();
        if (token != Token.EOF) {
            throw new ParseException("end of input", input.getLastToken());
        }

        return new AstProgram(expression);
    }

    /**
     * Parses an input expression.
     * Grammar Rules:
     * E -> (EE) | \x.E | E | V
     */
    public static AstExpression parseExpression(Lexer input) throws ParseException {
        Token token = input.next();

        if (token == Token.LBRACKET) {
            // E -> (EE) | (E)

            // Parse E
            AstExpression left = parseExpression(input);

            if (input.peek() == Token.RBRACKET) {
                // current case: E -> (E)
                // read off the right bracket
                input.next();

                return left;
            }

            // current case: E -> (EE)
            // Parse E
            AstExpression right = parseExpression(input);

            // Parse )
            token = input.next();
            if(token != Token.RBRACKET) {
                throw new ParseException("')'", input.getLastToken());
            }

            return new AstApplication(left, right);
        } else if (token == Token.LAMBDA) {
            // E -> \var.E

            // Parse the head variable
            token = input.next();
            if(token != Token.VARIABLE) {
                throw new ParseException("variable", input.getLastToken());
            }
            AstVariable variable = new AstVariable(input.getIdentifier());

            // Parse '.'
            token = input.next();
            if(token != Token.DOT) {
                throw new ParseException("'.'", input.getLastToken());
            }

            // Parse E
            AstExpression subExpression = parseExpression(input);

            return new AstAbstraction(variable, subExpression);
        } else if (token == Token.VARIABLE) {
            // E -> V
            return new AstVariable(input.getIdentifier());
        } else {
            throw new ParseException("expression", input.getLastToken());
        }
    }
}
