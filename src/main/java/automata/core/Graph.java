package automata.core;

import java.util.ArrayList;
import java.util.List;

/**
 * This structure represents a graph of an automaton, whether it be deterministic or non deterministic
 */
public class Graph {


    private List<Node> nodes;

    private List<Transition> allTransitions;

    /**
     * This is the alphabet the Finite State Automata uses. It it set in the constructor and nowhere else
     */
    private String[] alphabet;

    private Node startNode;

    /**
     * Instantiates a graph with an alphabet and no nodes
     *
     * @param alphabet The alphabet the graph will use
     */
    public Graph(String[] alphabet) {
        this.alphabet = alphabet;
        nodes = new ArrayList<>();
        allTransitions = new ArrayList<>();
    }

    /**
     * Checks whether a symbol is valid
     *
     * @param symbol Symbol to check
     * @return Whether the symbol is valid
     */
    private static boolean isValidSymbol(String symbol) {
        return true;
    }

    /**
     * Adds a node to the graph, checking if the label already exists
     *
     * @param n The node to add to the graph
     * @throws LabelAlreadyExistsException If the label of <code>n</code> already exists
     */
    public void addNode(Node n) {
        // Check if a node with that label already exists
        for (Node node : getNodes()) {
            if (n.equals(node)) throw new LabelAlreadyExistsException(n.getLabel());
        }

        // Otherwise, add the node to the graph
        nodes.add(n);
    }

    /**
     * Check whether a symbol is in the alphabet
     *
     * @param testSymbol Symbol to test
     * @return Whether the symbol is in the alphabet
     */
    private boolean doesAlphabetContain(String testSymbol) {
        for (String symbol : alphabet) {
            if (symbol.equals(testSymbol)) return true;
        }

        return false;
    }

    /**
     * Adds a transition from a source node to a destination node when a certain symbol is seen
     *
     * @param n1               Source node
     * @param n2               Destination node
     * @param transitionSymbol The transition is performed when this symbol is seen
     * @throws InvalidSymbolException If the symbol is invalid
     */
    public void connectNodes(Node n1, Node n2, String transitionSymbol) {
        // Validate the transitionSymbol
        if (!isValidSymbol(transitionSymbol)) throw new InvalidSymbolException(transitionSymbol);
        if (!doesAlphabetContain(transitionSymbol)) throw new SymbolNotFoundException(transitionSymbol);

        // Check if the nodes exist in the graph
        if (!nodes.contains(n1)) throw new NodeNotFoundException(n1);
        if (!nodes.contains(n2)) throw new NodeNotFoundException(n2);

        Transition transition = new Transition(n1, n2, transitionSymbol);
        // Add the transition
        if (!n1.getTransitions().contains(transition)) {
            // Add the transition to the node
            n1.addTransition(transition);
            // Add the transition to the Graph
            allTransitions.add(transition);
        }
    }

    /**
     * Adds a transition from a source node to a destination node when a certain symbol is seen
     *
     * @param label1           The node in the graph with this label will be the source node
     * @param label2           The node in the graph with this label will be the destination node node
     * @param transitionSymbol The transition is performed when this symbol is seen
     */
    public void connectNodes(String label1, String label2, String transitionSymbol) {
        connectNodes(getNode(label1), getNode(label2), transitionSymbol);
    }

    /**
     * Returns whether a node is in the graph
     *
     * @param label The label to search
     * @return Weather a node with that label is in the graph
     */
    public boolean containsNode(String label) {
        for (Node n : nodes) {
            if (n.getLabel().equals(label)) return true;
        }

        return false;
    }

    /**
     * Looks through all nodes in the graph and determines whether the graph is an NFA or not (a DFA).
     *
     * @return true if the graph is an NFA, false if not
     */
    public boolean isNFA() {
        for (Node node : getNodes()) {
            // Make a list of the symbols that this node transitions over
            List<Symbol> transitionSymbols = new ArrayList<>();

            for (Transition transition : node.getTransitions()) {
                // If the symbol already exists then it is an NFA, otherwise add the symbol to the list and keep going
                if (transitionSymbols.contains(transition.getSymbol())) {
                    return true;
                } else {
                    transitionSymbols.add(transition.getSymbol());
                }
            }
        }

        // If there has been no duplicate symbols in any node then it isn't an NFA (it is a DFA)
        return false;
    }

    /**
     * @return Gets the starting node
     */
    public Node getStartNode() {
        return startNode;
    }

    /**
     * Sets the starting node in the graph, checking if it's in the graph first
     *
     * @param n Node to make the staring node
     * @throws NodeNotFoundException If <code>n</code> isn't found in the graph
     */
    void setStartNode(Node n) {
        // Check if the graph contains the node
        if (!containsNode(n.getLabel())) throw new NodeNotFoundException(n);

        startNode = n;
    }

    /**
     * Sets the starting node in the graph, checking if it's in the graph first
     *
     * @param label Node with this label to make the starting node
     */
    public void setStartNode(String label) {
        startNode = getNode(label);
    }

    public void makeNodeAccepting(String label) {
        getNode(label).setAccepting(true);
    }

    public List<Node> getNodes() {
        return nodes;
    }

    /**
     * Gets the node with a specific label if it's in the graph, throws an exception if it isn't in the graph
     *
     * @param label Label of the node to get
     * @return Node with label of <code>label</code> if it's in the graph
     * @throws NodeNotFoundException If the node isn't found
     */
    public Node getNode(String label) {
        for (Node n : nodes) {
            if (n.equals(label)) return n;
        }

        throw new NodeNotFoundException(label);
    }

    public String toString() {
        StringBuilder s = new StringBuilder();

        for (Node n : nodes) {
            s.append(n.toString()).append("\n");
        }

        return s.toString();
    }


}
