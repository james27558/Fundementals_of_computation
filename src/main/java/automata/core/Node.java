package automata.core;

import java.util.ArrayList;
import java.util.List;

public class Node {
    static private int nodeCount;

    /**
     * The label should be unique in a graph
     */
    private String label;

    List<Node> connections = new ArrayList<>();

    private boolean isAccepting;

    protected Node() {
        nodeCount++;
    }

    /**
     * Initialises a newly created node with a unique <code>label</code>
     *
     * @param label_ What this node should be labelled as
     */
    public Node(String label_) {
        label = label_;
        nodeCount++;
    }

    /**
     * Initialises a newly created node with a unique <code>label</code>
     *
     * @param label_       What this node should be labeled as
     * @param isAccepting_ Whether this node is accepting or not
     */
    public Node(String label_, boolean isAccepting_) {
        this(label_);

        isAccepting = isAccepting_;
    }

    /**
     * @return Is the node accepting
     */
    public boolean isAccepting() {
        return isAccepting;
    }

    /**
     * @param accepting Sets whether the node is accepting
     */
    void setAccepting(boolean accepting) {
        isAccepting = accepting;
    }

    /**
     * @return Gets the label of the node
     */
    public String getLabel() {
        return label;
    }

    /**
     * Overrides the toString method in <code>java.lang.Object</code>
     *
     * @return String representation of the node, including its label and list of connections
     */
    public String toString() {
        // Convert the connections ArrayList to a String[] of the labels of the connections
        String[] connections_string_array = new String[connections.size()];
        for (int i = 0; i < connections.size(); i++) connections_string_array[i] = connections.get(i).getLabel();

        return "Label: " + getLabel() + ", Connections: " + String.join(",", connections_string_array);
    }


    public boolean equals(Node node) {
        return getLabel().equals(node.getLabel());
    }

    public boolean equals(String label) {
        return getLabel().equals(label);
    }

}
