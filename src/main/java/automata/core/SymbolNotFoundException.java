package automata.core;

/**
 * This exception is thrown if a symbol the user is attempting to use isn't in the graph's alphabet
 */
public class SymbolNotFoundException extends RuntimeException {
    public SymbolNotFoundException(String symbol) {
        super("The symbol " + symbol + " isn't present in the alphabet of the graph");
    }
}
