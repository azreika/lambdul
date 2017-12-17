public enum Token {
    // (
    LBRACKET,

    // )
    RBRACKET,

    // \
    LAMBDA,

    // [a-zA-Z]*
    VARIABLE,

    // _[a-zA-Z]*
    MACRO,

    // :=
    OP_ASSIGNMENT,

    // .
    DOT,

    // EOF
    EOF
}
