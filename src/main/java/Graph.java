import java.util.ArrayList;

public class Graph {
    ArrayList<Node> nodes = new ArrayList<>();
    String[] alphabet;
    Node startNode;

    Graph(String[] alphabet) {
        this.alphabet = alphabet;
    }

    void addNode(Node n) {
        nodes.add(n);
    }

    void connectNodes(Node n1, Node n2) {
        // Check if the nodes exist in the graph
        if (!nodes.contains(n1)) {
            throw new RuntimeException("Graph does not contain the node " + n1.label);
        }

        if (!nodes.contains(n2)) {
            throw new RuntimeException("Graph does not contain the node " + n2.label);
        }

        // Add the node
        if (!n1.connections.contains(n2)) n1.connections.add(n2);
    }

    void connectNodes(int id1, int id2) {
        Node n1 = null;
        Node n2 = null;
        for (Node n : nodes) {
            if (n.id == id1) n1 = n;
            if (n.id == id2) n2 = n;
        }

        connectNodes(n1, n2);
    }

    void connectNodes(String label1, String label2) {
        Node n1 = null;
        Node n2 = null;
        for (Node n : nodes) {
            if (n.label == label1) n1 = n;
            if (n.label == label2) n2 = n;
        }

        connectNodes(n1, n2);
    }

    boolean containsNodeViaLabel(String label) {
        for (Node n : nodes) {
            if (n.label == label) return true;
        }

        return false;
    }

    void setStartNode(Node n) {
        startNode = n;
    }

    void setStartNode(int id) {
        for (Node n : nodes) {
            if (n.id == id) {
                startNode = n;
                return;
            }
        }

        throw new RuntimeException("Node with id " + id + " isn't in the graph");
    }

    void setStartNode(String label) {
        for (Node n : nodes) {
            if (n.label == label) {
                startNode = n;
                return;
            }
        }

        throw new RuntimeException("Node with label " + label + " isn't in the graph");
    }

    void makeNodeAccepting(String label) {
        for (Node n : nodes) {
            if (n.label == label) {
                n.isAccepting = true;
                return;
            }
        }

        throw new RuntimeException("Node with label " + label + " is not in the graph");
    }

    Node getNode(String label) {
        for (Node n : nodes) {
            if (n.label == label) return n;
        }

        throw new RuntimeException("Node with label " + label + " isn't present in the graph");
    }

    public String toString() {
        StringBuilder s = new StringBuilder();

        for (Node n : nodes) {
            s.append(n.toString() + "\n");
        }

        return s.toString();
    }


}
