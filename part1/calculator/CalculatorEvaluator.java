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
        if (isDigit(lookahead)) {
            int digit = evalDigit(lookahead);
            consume(lookahead);
            return rest(digit);
        } else if (lookahead == '(') {
            consume(lookahead);
            int expression = exp();
            consume(')');
            return rest(expression);
        } else throw new ParseError();
    }

    private int rest(int prev) throws IOException, ParseError {
        if (lookahead == '^') {
            consume(lookahead);
            int next = exp();
            return (prev ^ next);
        } else if (lookahead == '&') {
            consume(lookahead);
            int next = exp();
            return (prev & next);
        } else if (lookahead == ')' || lookahead == -1 || lookahead == '\n') {
            return prev;
        } else throw new ParseError(); 
    }
}