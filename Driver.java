/**
 * REPL driver for the interpreter
 */

import java.util.Scanner;

public class Driver {
    public static void main(String[] args) {
        // Set up the environment
        AstEnvironment env = new AstEnvironment();

        // Start off the REPL
        Scanner keyboard = new Scanner(System.in);
        System.out.print("> ");
        while(keyboard.hasNextLine()) {
            String lineInput = keyboard.nextLine();
            Lexer lexer = new Lexer(lineInput);
            try {
                // Parse the program
                AstProgram program = parseProgram(lexer);

                // Evaluate the resultant expression
                AstNode result = program.evaluate(env);

                // Print the reduced lambda expression
                System.out.println(result);
            } catch (ParseException pe) {
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
     * P -> A, where M is a macro assignment.
     */
    public static AstProgram parseProgram(Lexer input) throws ParseException {
        // P -> E

        AstProgram program;
        if (input.peek() == Token.MACRO) {
            // Parse A
            AstAssignment assignment = parseAssignment(input);
            program = new AstProgram(assignment);
        } else {
            // Parse E
            AstExpression expression = parseExpression(input);
            program = new AstProgram(expression);
        }

        Token token = input.next();
        if (token != Token.EOF) {
            throw new ParseException("end of input", input.getLastToken());
        }

        return program;
    }

    /**
     * Parses an input assignment.
     * Grammar Rules:
     * A -> M := E
     */
    public static AstAssignment parseAssignment(Lexer input) throws ParseException {
        Token token = input.next();

        if (token == Token.MACRO) {
            // A -> M := E

            // Parse M
            AstMacro macro = new AstMacro(input.getIdentifier());

            // Parse :=
            token = input.next();
            if (token != Token.OP_ASSIGNMENT) {
                throw new ParseException(":=", input.getLastToken());
            }

            // Parse E
            AstExpression rhs = parseExpression(input);

            return new AstAssignment(macro, rhs);
        } else {
            throw new ParseException("assignment", input.getLastToken());
        }
    }

    /**
     * Parses an input expression.
     * Grammar Rules:
     * E -> (EE) | \x.E | E | V | M
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
        } else if (token == Token.MACRO) {
            // E -> M
            return new AstMacro(input.getIdentifier());
        } else {
            throw new ParseException("expression", input.getLastToken());
        }
    }
}
