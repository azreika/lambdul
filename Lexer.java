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

    // TODO: use Token string output

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
            if (this.idx == input.length()) {
                this.lastToken = "EOF";
                return Token.EOF;
            }
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
        } else if (nextChar == ':') {
            this.idx++;
            if (this.idx == input.length()) {
                throw new ParseException("=", "EOF");
            }
            nextChar = this.input.charAt(idx);
            if (nextChar != '=') {
                throw new ParseException("=", Character.toString(nextChar));
            }
            this.lastToken = ":=";
            this.idx++;
            return Token.OP_ASSIGNMENT;
        } else if (nextChar == '_') {
            this.identifier = "_";
            idx++;
            if (idx == input.length()) {
                throw new ParseException("identifier", "EOF");
            }
            if (!Character.isLetter(input.charAt(idx))) {
                throw new ParseException("identifier", Character.toString(input.charAt(idx)));
            }

            while(this.idx < input.length() && Character.isLetter(this.input.charAt(idx))) {
                nextChar = input.charAt(idx);
                identifier += nextChar;
                this.idx++;
            }
            this.lastToken = this.identifier;
            return Token.MACRO;
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
