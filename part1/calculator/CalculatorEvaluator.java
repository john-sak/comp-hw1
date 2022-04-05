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
    public int eval() throws IOException, ParseError {
        // todo
    }
    private int prog() throws IOException, ParseError {
        // todo
    }
    private int exp() throws IOException, ParseError {
        // todo
    }
    private int exp2() throws IOException, ParseError {
        // todo
    }
    private int op() throws IOException, ParseError {
        // todo
    }
    private int num() throws IOException, ParseError {
        // todo
    }
}