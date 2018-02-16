public class Parser {
    private Lexer lexer;

    public Parser(String input) {
        this.lexer = new Lexer(input);
    }

    // TODO: add a method to reset parsing to the start of the original input

    /**
     * Parses a program.
     * Grammar Rules:
     * P -> E, where E is an expression.
     * P -> A, where A is an assignment.
     * P -> C, where C is a command.
     *
     * @return  a program node representing the parsed program
     */
    public AstProgram parseProgram() throws ParseException {
        AstProgram program;
        if (lexer.peek() == Token.AT) {
            // Current case: Parsing a command
            AstCommand command = this.parseCommand();
            program = new AstProgram(command);
        } else if (lexer.peek() == Token.MACRO && lexer.lookAhead(2) == Token.OP_ASSIGNMENT) {
            // Current case: Parsing an assignment
            AstAssignment assignment = this.parseAssignment();
            program = new AstProgram(assignment);
        } else {
            // Current case: Parsing an expression
            AstExpression expression = this.parseExpression();

            // Check for more expressions
            while (lexer.peek() != Token.EOF) {
                // Expecting another expression
                // Assuming left-associativity
                AstExpression nextExpression = this.parseExpression();
                expression = new AstApplication(expression, nextExpression);
            }

            program = new AstProgram(expression);
        }

        // Check that we have reached the end of the input
        Token token = lexer.next();
        if (token != Token.EOF) {
            throw new ParseException("end of input", lexer.getLastToken());
        }

        return program;
    }

    /**
     * Parses an input command.
     * Grammar Rules:
     * C -> @CommandName Arg1 Arg2 ... ArgN
     *
     * @return  a command node representing the parsed command
     */
    public AstCommand parseCommand() throws ParseException {
        Token token = lexer.next();

        if (token == Token.AT) {
            // C -> @CommandName Arg1 ... ArgN

            // Parse the command name
            // Command names are the same as variable names
            token = lexer.next();
            if (token != Token.VARIABLE) {
                throw new ParseException("command name", lexer.getLastToken());
            }

            // Parse arguments based on the entered command
            String commandName = lexer.getIdentifier();
            if (commandName.equals("evaluate")) {
                // @evaluate EXPR
                AstExpression expr = this.parseExpression();
                return new AstCommandEvaluate(expr);
            } else if (commandName.equals("exit")) {
                // @exit
                return new AstCommandExit();
            } else if (commandName.equals("assign")) {
                // @assign MACRO EXPR

                // Parse the macro
                token = lexer.next();
                if (token != Token.MACRO) {
                    throw new ParseException("macro", lexer.getLastToken());
                }
                AstMacro macro = new AstMacro(lexer.getIdentifier());

                // Parse the expression
                AstExpression expr = this.parseExpression();

                return new AstCommandAssign(macro, expr);
            } else if (commandName.equals("import")) {
                // @import STRING

                // Parse the string
                token = lexer.next();
                if (token != Token.STRING) {
                    throw new ParseException("string", lexer.getLastToken());
                }

                return new AstCommandImport(lexer.getString());
            } else {
                throw new ParseException("invalid command '" + lexer.getIdentifier() + "'");
            }
        } else {
            throw new ParseException("command", lexer.getLastToken());
        }
    }

     // TODO: refactor to allow A -> M case
    /**
     * Parses an input assignment.
     * Grammar Rules:
     * A -> M := E, where M is a macro, E is an expression.
     *
     * @return  an assignment node representing the parsed assignment
     */
    public AstAssignment parseAssignment() throws ParseException {
        Token token = lexer.next();

        if (token == Token.MACRO) {
            // A -> M := E

            // Parse the macro M
            AstMacro macro = new AstMacro(lexer.getIdentifier());

            // Parse the token ':='
            token = lexer.next();
            if (token != Token.OP_ASSIGNMENT) {
                throw new ParseException(":=", lexer.getLastToken());
            }

            // Parse the expression E
            AstExpression rhs = this.parseExpression();

            return new AstAssignment(macro, rhs);
        } else {
            throw new ParseException("assignment", lexer.getLastToken());
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
     * @return  an expression node representing the parsed expression
     */
    public AstExpression parseExpression() throws ParseException {
        Token token = lexer.next();

        if (token == Token.LBRACKET) {
            // Two possible cases: E -> (E+)

            // Parse the first expression
            AstExpression currentExpression = this.parseExpression();

            // Check if we have another expression to parse
            while (lexer.peek() != Token.RBRACKET) {
                // Expecting another expression
                // Assuming left-associativity, so EEE... = (EE)E...
                AstExpression nextExpression = this.parseExpression();
                currentExpression = new AstApplication(currentExpression, nextExpression);
            }

            // Read off the right bracket
            lexer.next();

            // Return the final expression
            return currentExpression;
        } else if (token == Token.LAMBDA) {
            // E -> \var.E

            // Parse the head variable
            token = lexer.next();
            if(token != Token.VARIABLE) {
                throw new ParseException("variable", lexer.getLastToken());
            }
            AstVariable variable = new AstVariable(lexer.getIdentifier());

            // Parse '.'
            token = lexer.next();
            if(token != Token.DOT) {
                throw new ParseException("'.'", lexer.getLastToken());
            }

            // Parse the expression E
            AstExpression subExpression = this.parseExpression();

            return new AstAbstraction(variable, subExpression);
        } else if (token == Token.VARIABLE) {
            // E -> V
            return new AstVariable(lexer.getIdentifier());
        } else if (token == Token.MACRO) {
            // E -> M
            return new AstMacro(lexer.getIdentifier());
        } else {
            throw new ParseException("expression", lexer.getLastToken());
        }
    }
}
