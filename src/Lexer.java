public class Lexer {
    private int idx;
    private String identifier;
    private String string;
    private String input;
    private String lastToken;

    public Lexer(String input) {
        // TODO: rename input to source?
        this.input = input;
        this.idx = 0;
        this.identifier = null;
        this.string = null;
        this.lastToken = null;
    }

    /**
     * Get the current index of the lexer wrt the source.
     *
     * @return  the index
     */
    public int getIndex() {
        return this.idx;
    }

    /**
     * Get the value of the last found identifier.
     * These can be for either a macro or a variable.
     *
     * @return  the last identifier
     */
    public String getIdentifier() {
        // TODO: handle null case
        return this.identifier;
    }

    /**
     * Get the value of the last found string.
     *
     * @return  the last string
     */
    public String getString() {
        // TODO: handle null case
        return this.string;
    }

    // TODO: use Token string output

    /**
     * Get the next token without altering the lexer position.
     * This method is idempotent, and is equivalent to lookAhead(1).
     *
     * @return  the next token
     */
    public Token peek() throws ParseException {
        return this.lookAhead(1);
    }

    /**
     * Get the token a given number of steps ahead without altering the lexer position.
     * This method is idempotent.
     *
     * @param steps the number of tokens to look ahead by
     * @return      the token at that position
     */
    public Token lookAhead(int steps) throws ParseException {
        // Store the initial index value
        int initialIdx = this.idx;

        // Find the token
        for (int i = 0; i < steps-1; i++) {
            this.next();
        }
        Token result = this.next();

        // Restore the position of the lexer
        this.idx = initialIdx;

        return result;
    }

    /**
     * Get the last token scanned.
     *
     * @return  the last token scanned
     */
    public String getLastToken() {
        return lastToken;
    }

    /**
     * Get the next token.
     *
     * @return  the next token
     */
    public Token next() throws ParseException {
        // Check if already out of range
        if (idx > input.length()) {
            throw new ParseException("unexpected end of input");
        }

        // Reached the end of the input
        if (idx == input.length()) {
            idx++;
            this.lastToken = "EOF";
            return Token.EOF;
        }

        // Ignore whitespace
        while (Character.isWhitespace(input.charAt(this.idx))) {
            this.idx++;

            if (this.idx == input.length()) {
                // Reached the end of the input
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
        } else if (nextChar == '@') {
            this.idx++;
            this.lastToken = "@";
            return Token.AT;
        } else if (nextChar == ':') {
            // Expected token: ':='
            this.idx++;

            // Read in the '='
            if (this.idx == input.length()) {
                throw new ParseException("'='", "EOF");
            }
            nextChar = this.input.charAt(idx);
            if (nextChar != '=') {
                throw new ParseException("'='", Character.toString(nextChar));
            }

            this.lastToken = ":=";
            this.idx++;
            return Token.OP_ASSIGNMENT;
        } else if (nextChar == '"') {
            // Expecting a string
            this.idx++;
            this.string = "";

            boolean isTerminatedString = false;
            while (this.idx < input.length()) {
                if (this.input.charAt(idx) == '"') {
                    this.idx++;
                    isTerminatedString = true;
                    break;
                }

                nextChar = input.charAt(idx);
                if (nextChar == '\\') {
                    // Escaped character
                    this.idx++;
                    if (this.idx == input.length()) {
                        throw new ParseException("character", "EOF");
                    }
                    nextChar = input.charAt(idx);
                }
                this.string += nextChar;
                this.idx++;
            }

            if (!isTerminatedString) {
                throw new ParseException("'\"'", "EOF");
            }

            this.lastToken = "\"" + this.string + "\"";
            return Token.STRING;
        } else if (nextChar == '_') {
            // Expecting a macro - identifiers that begin with _
            this.idx++;

            if (idx == input.length()) {
                throw new ParseException("identifier", "EOF");
            }
            if (!Character.isLetter(input.charAt(idx))) {
                throw new ParseException("identifier", Character.toString(input.charAt(idx)));
            }

            // Keep reading in the identifier
            this.identifier = "_";
            while(this.idx < input.length() && Character.isLetter(this.input.charAt(idx))) {
                nextChar = input.charAt(idx);
                identifier += nextChar;
                this.idx++;
            }
            this.lastToken = this.identifier;
            return Token.MACRO;
        } else if (Character.isLetter(nextChar)) {
            // Keep reading in the identifier
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
