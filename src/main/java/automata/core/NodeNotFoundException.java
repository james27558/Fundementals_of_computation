package automata.core;

/**
 * This exception should be thrown when it is essential for a node to be in the graph but when being searched for in the
 * graph it doesn't exist.
 */
public class NodeNotFoundException extends RuntimeException {
    public NodeNotFoundException(Node node) {
        super("Graph does not contain the node with label: " + node.getLabel());
    }

    public NodeNotFoundException(String label) {
        super("Graph does not contain the node with label: " + label);
    }
}
