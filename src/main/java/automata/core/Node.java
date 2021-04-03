package automata.core;

import java.util.ArrayList;
import java.util.List;

public class Node {
    static private int nodeCount;

    private final int ID;

    public String label;
    List<Node> connections = new ArrayList<>();
    private boolean isAccepting;

    /**
     * Initialises a newly created node with a unique <code>ID</code> and a <code>label</code> set by <code>label_</code>
     *
     * @param label_ What this node should be labelled as
     */
    public Node(String label_) {
        label = label_;
        ID = nodeCount;
        nodeCount++;
    }

    /**
     * Initialises a newly created node with a unique <code>ID</code> and a <code>label</code> set by <code>label_</code>.
     * Also sets the <code>isAccepting</code> field to the <code>isAccepting_</code> parameter
     *
     * @param label_       What this node should be labeled as
     * @param isAccepting_ Weather this node is accepting or not
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
     * @param accepting Sets weather the node is accepting
     */
    void setAccepting(boolean accepting) {
        isAccepting = accepting;
    }

    /**
     * @return Gets the <code>ID</code> of the node
     */
    public int getID() {
        return ID;
    }

    /**
     * Overrides the toString method in <code>java.lang.Object</code>
     *
     * @return String representation of the node, including its label, ID and list of connections
     */
    public String toString() {
        String[] connections_string_array = new String[connections.size()];
        for (int i = 0; i < connections.size(); i++) {
            connections_string_array[i] = connections.get(i).label;
        }

        return "Label: " + label + ", Id: " + ID + ", Connections: " + String.join(",", connections_string_array);
    }
}
