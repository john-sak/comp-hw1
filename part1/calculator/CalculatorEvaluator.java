import java.io.InputStream;
import java.io.IOException;

class Calculator {
    private final InputStream in;
    private int lookahead;

    public Calculator(InputStream in) throws IOException {
        this.in = in;
        lookahead = in.read();
    }

    private void consume(int symbol) throws IOException, ParseError {
        if (lookahead == symbol) lookahead = in.read();
        else throw new ParseError();
    }

    private boolean isDigit(int c) {
        return ('0' <= c && c <= '9');
    }
    
    private int evalDigit(int c) {
        return (c - '0');
    }

    public int eval() throws IOException, ParseError {
        int result = prog();
        if (lookahead != -1 && lookahead != '\n') throw new ParseError();
        return result;
    }
    
    private int prog() throws IOException, ParseError {
        return exp();
    }

    private int exp() throws IOException, ParseError {
        int curr = term();
        return exp2(curr);
    }

    private int exp2(int prev) throws IOException, ParseError {
        switch (lookahead) {
            case '^':
                consume('^');
                int curr = term();
                return exp2(prev ^ curr);
            case ')':
            case '\n':
            case -1:
                return prev;
            default:
                throw new ParseError();
        }
    }

    private int term() throws IOException, ParseError {
        int factor = fact();
        return term2(factor);
    }

    private int term2(int prev) throws IOException, ParseError {
        switch (lookahead) {
            case '&':
                consume('&');
                int curr = fact();
                return term2(prev & curr);
            case '^':
            case ')':
            case '\n':
            case -1:
                return prev;
            default:
                throw new ParseError();
        }
    }

    private int fact() throws IOException, ParseError {
        if (isDigit(lookahead)) return evalDigit(lookahead);
        else if (lookahead == '(') {
            int expression = exp();
            consume(')');
            return expression;
        }
        else throw new ParseError();
    }
}