import java.util.ArrayList;
import java.util.List;

public class Node {
    static private int nodeCount;
    private final int ID;

    public String label;
    public boolean isAccepting;

    List<Node> connections = new ArrayList<>();

    public Node(String label_) {
        label = label_;
        ID = nodeCount;
        nodeCount++;
    }

    public Node(String label_, boolean isAccepting) {
        this(label_);

        isAccepting = true;
    }


    public String toString() {
        String[] connections_string_array = new String[connections.size()];
        for (int i = 0; i < connections.size(); i++) {
            connections_string_array[i] = connections.get(i).label;
        }

        return "Label: " + label + ", Id: " + ID + ", Connections: " + String.join(",", connections_string_array);
    }
}
