package automata.core;

/**
 * This exception is thrown when an invalid symbol is used in the graph
 */
public class InvalidSymbolException extends RuntimeException {
    public InvalidSymbolException(String symbol) {
        super("The symbol " + symbol + " is an invalid symbol. Symbols must be 1 character and not a space");
    }
}
