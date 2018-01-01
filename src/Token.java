public enum Token {
    // (
    LBRACKET,

    // )
    RBRACKET,

    // \
    LAMBDA,

    // [a-zA-Z]+
    VARIABLE,

    // _[a-zA-Z]+
    MACRO,

    // \"(\\.|[^"])*\"
    STRING,

    // :=
    OP_ASSIGNMENT,

    // .
    DOT,

    // @
    AT,

    // EOF
    EOF
}
