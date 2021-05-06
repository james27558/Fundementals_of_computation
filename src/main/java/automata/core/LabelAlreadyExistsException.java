package automata.core;

/**
 * This exception should be thrown when a node is being added to the graph or its label is being changed and the label
 * already exists in teh graph
 */
public class LabelAlreadyExistsException extends RuntimeException {

    public LabelAlreadyExistsException(String label) {
        super("A node with the label '" + label + "' already exists in the graph");
    }
}
