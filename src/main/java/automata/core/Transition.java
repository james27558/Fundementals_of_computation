package automata.core;

public class Transition {
    private Node source;
    private Node destination;

    private Symbol symbol;

    /**
     * Constructs a transition between nodes.
     *
     * @param source      Source node
     * @param destination Destination node
     * @param symbol      Symbol to transition over
     */
    public Transition(Node source, Node destination, Symbol symbol) {
        this.source = source;
        this.destination = destination;
        this.symbol = symbol;
    }

    /**
     * Constructs a transition between nodes.
     *
     * @param source      Source node
     * @param destination Destination node
     * @param symbol      Symbol to transition over
     */
    public Transition(Node source, Node destination, String symbol) {
        this(source, destination, Symbol.fromString(symbol));
    }

    /**
     * Given this transition between two nodes, return whether a given symbol would cause it
     *
     * @param testSymbol Symbol to test
     * @return Whether <code>testSymbol</code> would cause this transition to activate
     */
    public boolean willSymbolCauseTransition(Symbol testSymbol) {
        return symbol.equals(testSymbol);
    }

    /**
     * Given this transition between two nodes, return whether a given symbol would cause it
     *
     * @param testSymbol Symbol to test
     * @return Whether <code>testSymbol</code> would cause this transition to activate
     */
    public boolean willSymbolCauseTransition(String testSymbol) {
        return Symbol.fromString(testSymbol).equals(getSymbol());
    }

    public Node getSource() {
        return source;
    }

    public Node getDestination() {
        return destination;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public String toString() {
        return "Transition: " + getSource() + " => " + getDestination() + " by '" + getSymbol() + "'";
    }
}
