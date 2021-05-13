package automata.core;

import java.util.ArrayList;
import java.util.List;

public class Node {

    /**
     * The label should be unique in a graph
     */
    private String label;

    private List<Transition> transitions = new ArrayList<>();

    private boolean isAccepting;

    protected Node() {
    }

    /**
     * Initialises a newly created node with a unique <code>label</code>
     *
     * @param label_ What this node should be labelled as
     */
    public Node(String label_) {
        label = label_;
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
    public void setAccepting(boolean accepting) {
        isAccepting = accepting;
    }

    public List<Transition> getTransitions() {
        return transitions;
    }

    /**
     * Adds a transition to the node
     *
     * @param t Transition to add
     */
    public void addTransition(Transition t) {
        transitions.add(t);
    }

    /**
     * Checks whether a symbol applied to this node would cause 1 or more transitions from it
     *
     * @param symbol
     * @return
     */
    public boolean willSymbolCauseTransition(Symbol symbol) {
        for (Transition t : getTransitions()) {
            if (t.willSymbolCauseTransition(symbol)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Applies a symbol to this node and returns the destination node of all the transitions from it that would be
     * activated by that symbol
     *
     * @param symbol Symbol to apply to the node
     * @return The destination nodes that have a transition over the given symbol
     */
    public List<Node> getDestinationNodesAfterTransition(Symbol symbol) {
        List<Node> destinationNodes = new ArrayList<>();
        for (Transition t : getTransitions()) {
            if (t.willSymbolCauseTransition(symbol)) destinationNodes.add(t.getDestination());
        }

        return destinationNodes;
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
        // Convert the to an array of strings
        String[] transitions_string_array = new String[transitions.size()];
        for (int i = 0; i < transitions.size(); i++)
            transitions_string_array[i] = transitions.get(i).getDestination().getLabel();

        return "{Label [" + getLabel() + " Connections: [" + String.join(",", transitions_string_array) + "]}";
    }


    public boolean equals(Node node) {
        return getLabel().equals(node.getLabel());
    }

    public boolean equals(String label) {
        return getLabel().equals(label);
    }

}
