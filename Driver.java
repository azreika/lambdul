/**
 * REPL Driver for the interpreter
 */

import java.util.Scanner;

public class Driver {
    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        while(keyboard.hasNextLine()) {
            String lineInput = keyboard.nextLine();
            Lexer lexer = new Lexer(lineInput);
            AstProgram program = parseProgram(lexer);
            AstExpression result = program.evaluate();
            System.out.println(result);
        }
    }

    public static AstProgram parseProgram(Lexer input) {
        AstExpression expression = parseExpression(input);
        Token token = input.next();
        if (token != Token.EOF) {
            throw new IllegalArgumentException();
        }
        return new AstProgram(expression);
    }

    public static AstExpression parseExpression(Lexer input) {
        Token token = input.next();

        if (token == Token.LBRACKET) {
            // E -> (EE)

            // E
            AstExpression left = parseExpression(input);

            // E
            AstExpression right = parseExpression(input);

            // )
            token = input.next();
            if(token != Token.RBRACKET) {
                throw new IllegalArgumentException();
            }

            return new AstApplication(left, right);
        } else if (token == Token.LAMBDA) {
            // E -> \var.E

            // var
            token = input.next();
            if(token != Token.VARIABLE) {
                throw new IllegalArgumentException();
            }
            AstVariable variable = new AstVariable(input.getIdentifier());

            // .
            token = input.next();
            if(token != Token.DOT) {
                throw new IllegalArgumentException();
            }

            // E
            AstExpression subExpression = parseExpression(input);

            return new AstAbstraction(variable, subExpression);
        } else if (token == Token.VARIABLE) {
            return new AstVariable(input.getIdentifier());
        } else {
            throw new IllegalArgumentException();
        }
    }
}
