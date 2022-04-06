import java.io.InputStream;
import java.io.IOException;

class CalculatorEvaluator {
    private final InputStream in;
    private int lookahead;

    public CalculatorEvaluator(InputStream in) throws IOException {
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

    private boolean isOR(int c) {
        return (c == '^');
    }

    private boolean isAND(int c) {
        return (c == '&');
    }

    private boolean isLPar(int c) {
        return (c == '(');
    }

    private boolean isRPar(int c) {
        return (c == ')');
    }

    public int eval() throws IOException, ParseError {
        int result = prog();
        if (lookahead != -1 && lookahead != '\n') throw new ParseError();
        return result;
    }
    
    private int prog() throws IOException, ParseError {
        int result = exp();
        if (lookahead != -1 && lookahead != '\n') throw new ParseError();
        return result;
    }

    private int exp() throws IOException, ParseError {
        if (isDigit(lookahead)) {
            int digit = evalDigit(lookahead);
            consume(lookahead);
            return exp2(digit);
        } else if (lookahead == '(') {
            consume(lookahead);
            int expression = exp();
            consume(')');
            return expression;
        } else throw new ParseError();
    }

    private int exp2(int left) throws IOException, ParseError {
        if (lookahead == '^') {
            consume(lookahead);
            int right = exp();
            return (left ^ right);
        } else if (lookahead == '&') {
            consume(lookahead);
            int right = exp();
            return (left & right);
        } else if (lookahead == ')' || lookahead == -1) {
            return left;
        } else throw new ParseError(); 
    }
