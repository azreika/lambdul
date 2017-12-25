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

    /**
     * Parses a program.
     * Grammar Rules:
     * P -> E, where E is an expression.
     * P -> A, where A is an assignment.
     * P -> C, where C is a command.
     *
     * @param   input   the lexer/scanner for the current program pass
     * @return          a program node representing the parsed program
     */
    public static AstProgram parseProgram(Lexer input) throws ParseException {
        AstProgram program;
        if (input.peek() == Token.AT) {
            // Current case: Parsing a command
            AstCommand command = parseCommand(input);
            program = new AstProgram(command);
        } else if (input.peek() == Token.MACRO) {
            // Current case: Parsing an assignment
            AstAssignment assignment = parseAssignment(input);
            program = new AstProgram(assignment);
        } else {
            // Current case: Parsing an expression
            AstExpression expression = parseExpression(input);
            program = new AstProgram(expression);
        }

        // Check that we have reached the end of the input
        Token token = input.next();
        if (token != Token.EOF) {
            throw new ParseException("end of input", input.getLastToken());
        }

        return program;
    }

    /**
     * Parses an input command.
     * Grammar Rules:
     * C -> @CommandName Arg1 Arg2 ... ArgN
     *
     * @param   input   the lexer/scanner for the current program pass
     * @return          a command node representing the parsed command
     */
    public static AstCommand parseCommand(Lexer input) throws ParseException {
        Token token = input.next();

        if (token == Token.AT) {
            // C -> @CommandName Arg1 ... ArgN

            // Parse the command name
            // Command names are the same as variable names
            token = input.next();
            if (token != Token.VARIABLE) {
                throw new ParseException("command name", input.getLastToken());
            }

            // Parse arguments based on the entered command
            String commandName = input.getIdentifier();
            if (commandName.equals("evaluate")) {
                // @evaluate EXPR
                AstExpression expr = parseExpression(input);
                return new AstCommandEvaluate(expr);
            } else if (commandName.equals("exit")) {
                // @exit
                return new AstCommandExit();
            } else if (commandName.equals("assign")) {
                // @assign MACRO EXPR

                // Parse the macro
                token = input.next();
                if (token != Token.MACRO) {
                    throw new ParseException("macro", input.getLastToken());
                }
                AstMacro macro = new AstMacro(input.getIdentifier());

                // Parse the expression
                AstExpression expr = parseExpression(input);

                return new AstCommandAssign(macro, expr);
            } else {
                throw new ParseException("invalid command '" + input.getIdentifier() + "'");
            }
        } else {
            throw new ParseException("command", input.getLastToken());
        }
    }

    // TODO: refactor to allow A -> M case
    /**
     * Parses an input assignment.
     * Grammar Rules:
     * A -> M := E, where M is a macro, E is an expression.
     *
     * @param   input   the lexer/scanner for the current program pass
     * @return          an assignment node representing the parsed assignment
     */
    public static AstAssignment parseAssignment(Lexer input) throws ParseException {
        Token token = input.next();

        if (token == Token.MACRO) {
            // A -> M := E

            // Parse the macro M
            AstMacro macro = new AstMacro(input.getIdentifier());

            // Parse the token ':='
            token = input.next();
            if (token != Token.OP_ASSIGNMENT) {
                throw new ParseException(":=", input.getLastToken());
            }

            // Parse the expression E
            AstExpression rhs = parseExpression(input);

            return new AstAssignment(macro, rhs);
        } else {
            throw new ParseException("assignment", input.getLastToken());
        }
    }

    /**
     * Parses an input expression.
     * Grammar Rules:
     * E -> (E+) - an application (of one or more expressions)
     * E -> \x.E - an abstraction
     * E -> V    - a variable symbol
     * E -> M    - a macro symbol
     *
     * @param   input   the lexer/scanner for the current program pass
     * @return          an expression node representing the parsed expression
     */
    public static AstExpression parseExpression(Lexer input) throws ParseException {
        Token token = input.next();

        if (token == Token.LBRACKET) {
            // Two possible cases: E -> (E+)

            // Parse the first expression
            AstExpression currentExpression = parseExpression(input);

            // Check if we have another expression to parse
            while (input.peek() != Token.RBRACKET) {
                // Expecting another expression
                // Assuming left-associativity, so EEE... = (EE)E...
                AstExpression nextExpression = parseExpression(input);
                currentExpression = new AstApplication(currentExpression, nextExpression);
            }

            // Read off the right bracket
            input.next();

            // Return the final expression
            return currentExpression;
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

            // Parse the expression E
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
