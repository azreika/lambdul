public class Lexer {
    private int idx;
    public String identifier;
    private String input;

    public Lexer(String input) {
        this.input = input;
        this.idx = 0;
        this.identifier = null;
    }

    public int getIndex() {
        return this.idx;
    }

    public String getIdentifier() {
        // TODO: null check
        return this.identifier;
    }

    public Token next() {
        if (idx > input.length()) {
            throw new IllegalArgumentException();
        }

        if (idx == input.length()) {
            idx++;
            return Token.EOF;
        }

        while (Character.isWhitespace(input.charAt(this.idx))) {
            this.idx++;
        }

        char nextChar = input.charAt(idx);
        if (nextChar == '(') {
            this.idx++;
            return Token.LBRACKET;
        } else if (nextChar == ')') {
            this.idx++;
            return Token.RBRACKET;
        } else if (nextChar == '\\' || nextChar == 'λ') {
            this.idx++;
            return Token.LAMBDA;
        } else if (nextChar == '.') {
            this.idx++;
            return Token.DOT;
        } else if (Character.isLetter(nextChar)) {
            this.identifier = "";
            while(this.idx < input.length() && Character.isLetter(this.input.charAt(idx))) {
                nextChar = input.charAt(idx);
                identifier += nextChar;
                this.idx++;
            }
            return Token.VARIABLE;
        }

        throw new IllegalArgumentException();
    }
}
