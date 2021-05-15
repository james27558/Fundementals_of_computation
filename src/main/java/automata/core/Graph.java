package automata.core;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * This structure represents a graph of an automaton, whether it be deterministic or non deterministic
 */
public class Graph implements Serializable {
    private static final long serialVersionUID = -1474631563182818239L;

    private List<Node> nodes;

    private List<Node> currentPositions;
    private boolean currentlyTestingWord;

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

    public static Graph load(String path) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(path);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

        Graph g = (Graph) objectInputStream.readObject();

        fileInputStream.close();
        objectInputStream.close();

        return g;
    }

    public static Graph load(Path path) throws IOException, ClassNotFoundException {
        return load(path.toAbsolutePath().toString());
    }

    public void save(String path) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(path);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

        objectOutputStream.writeObject(this);

        objectOutputStream.close();
        fileOutputStream.close();
    }

    public void save(Path path) throws IOException {
        save(path.toAbsolutePath().toString());
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
    private void connectNodes(Node n1, Node n2, String transitionSymbol) {
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
     * Connects a node to itself with a certain transition symbol
     *
     * @param label            Label of the node to connect to itself
     * @param transitionSymbol Transition symbol to connect the node with
     */
    public void connectNodeToSelf(String label, String transitionSymbol) {
        connectNodes(label, label, transitionSymbol);
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
     * Starts testing a word. Any calls to {@link #stepTestingWord(String)} will throw an error if this function hasn't
     * been called beforehand
     */
    public void startTestingWord() {
        if (getStartNode() == null) throw new NoStartNodeException();

        currentPositions = new ArrayList<>();
        currentPositions.add(getStartNode());
        currentlyTestingWord = true;
    }

    /**
     * Passes a string through the automata, applying the string to all current nodes. All destination nodes become the
     * current nodes. For a DFA, there will only be 1 current node at anytime.
     *
     * @param string String to apply to all current nodes
     */
    public void stepTestingWord(String string) {
        if (!currentlyTestingWord)
            throw new RuntimeException("startTestingWord must be called before inputting strings test");

        ArrayList<Node> newCurrentPositions = new ArrayList<>();
        // Store the current positions so that we don't have to keep using the getter for it everytime we want to use it
        List<Node> currentPositionsLocal = getCurrentPositions();

        // Go through all the current positions and apply the transition, if we land at any nodes, make them the current positions
        for (int i = currentPositionsLocal.size() - 1; i >= 0; i--) {
            Node currentNode = currentPositionsLocal.get(i);
            // Get the destination nodes after applying the transitions
            List<Node> nodesAfterTransitions = currentNode.getDestinationNodesAfterTransition(Symbol.fromString(string));

            if (!nodesAfterTransitions.isEmpty()) newCurrentPositions.addAll(nodesAfterTransitions);
        }

        // Set the current positions to the positions after the transitions
        currentPositions = newCurrentPositions;
    }

    /**
     * End the feeding of a word through the automaton. After this call, trying to call {@link #stepTestingWord(String)}
     * without calling {@link #startTestingWord()} will throw an error.
     * <p>
     * This function, if any of the current positions are in an accepting state then it will return true (The word is
     * accepted by the automaton), otherwise none of the current positions are accepting and the function returns false
     * (The word is not accepted)
     *
     * @return true if the word is accepted, false if the word isn't accepted
     */
    public boolean endTestingWord() {
        currentlyTestingWord = false;

        for (Node node : getCurrentPositions()) {
            if (node.isAccepting()) return true;
        }

        return false;
    }

    public List<Node> getCurrentPositions() {
        return currentPositions;
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
    private void setStartNode(Node n) {
        startNode = n;
    }

    /**
     * Sets the starting node in the graph, checking if it's in the graph first
     *
     * @param label Node with this label to make the starting node
     * @throws NodeNotFoundException If <code>n</code> isn't found in the graph
     */
    public void setStartNode(String label) {
        setStartNode(getNode(label));
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Graph graph = (Graph) o;
        return currentlyTestingWord == graph.currentlyTestingWord && nodes.equals(graph.nodes) && Objects.equals(currentPositions, graph.currentPositions) && allTransitions.equals(graph.allTransitions) && Arrays.equals(alphabet, graph.alphabet) && Objects.equals(startNode, graph.startNode);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(nodes, currentPositions, currentlyTestingWord, allTransitions, startNode);
        result = 31 * result + Arrays.hashCode(alphabet);
        return result;
    }
}
