import java.util.ArrayList;

public class Node {
    static int node_count;
    String label;
    int id;
    boolean isAccepting;

    ArrayList<Node> connections = new ArrayList<>();

    Node(String label_) {
        label = label_;
        id = node_count;
        node_count++;
    }

    Node(String label_, boolean isAccepting) {
        this(label_);

        isAccepting = true;
    }


    public String toString() {
        String[] connections_string_array = new String[connections.size()];
        for (int i = 0; i < connections.size(); i++) {
            connections_string_array[i] = connections.get(i).label;
        }

        return "Label: " + label + ", Id: " + id + ", Connections: " + String.join(",", connections_string_array);
    }
}
