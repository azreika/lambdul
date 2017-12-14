public class Lexer {
    private int idx;
    private String identifier;
    private String input;
    private String lastToken;

    public Lexer(String input) {
        this.input = input;
        this.idx = 0;
        this.identifier = null;
    }

    public int getIndex() {
        return this.idx;
    }

    public String getIdentifier() {
        // TODO: handle null case
        return this.identifier;
    }

    public Token peek() throws ParseException {
        // store the initial index value
        int initialIdx = this.idx;

        Token nextToken = this.next();

        // restore the value
        this.idx = initialIdx;

        return nextToken;
    }

    public String getLastToken() {
        return lastToken;
    }

    public Token next() throws ParseException {
        if (idx > input.length()) {
            throw new ParseException("unexpected end of input");
        }

        if (idx == input.length()) {
            idx++;
            this.lastToken = "EOF";
            return Token.EOF;
        }

        while (Character.isWhitespace(input.charAt(this.idx))) {
            this.idx++;
        }

        char nextChar = input.charAt(idx);
        if (nextChar == '(') {
            this.idx++;
            this.lastToken = "(";
            return Token.LBRACKET;
        } else if (nextChar == ')') {
            this.idx++;
            this.lastToken = ")";
            return Token.RBRACKET;
        } else if (nextChar == '\\' || nextChar == 'λ') {
            this.idx++;
            this.lastToken = Character.toString(nextChar);
            return Token.LAMBDA;
        } else if (nextChar == '.') {
            this.idx++;
            this.lastToken = ".";
            return Token.DOT;
        } else if (Character.isLetter(nextChar)) {
            this.identifier = "";
            while(this.idx < input.length() && Character.isLetter(this.input.charAt(idx))) {
                nextChar = input.charAt(idx);
                identifier += nextChar;
                this.idx++;
            }
            this.lastToken = this.identifier;
            return Token.VARIABLE;
        }

        throw new ParseException("unexpected character: " + nextChar);
    }
}
