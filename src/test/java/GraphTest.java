import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GraphTest {

    private Graph g;

    @BeforeEach
    public void setUp() {
        g = new Graph(new String[]{"a", "b", "c"});
    }

    public void setUpBasicNodes() {
        g.addNode(new Node("A"));
        g.addNode(new Node("B"));
        g.addNode(new Node("C"));

        g.connectNodes("A", "B");
        g.connectNodes("B", "A");
        g.connectNodes("B", "C");

        g.makeNodeAccepting("C");
    }

    public void setUpBasicGraph() {
        setUpBasicNodes();

        g.setStartNode("A");
    }

    @Test
    public void testGetNodeGood() {
        setUpBasicNodes();
        // get(0) is node "A"
        assertEquals(g.nodes.get(0), g.getNode("A"));
    }

    @Test
    public void testGetNodeBad() {
        setUpBasicNodes();
        // get(1) is node "B"
        assertNotEquals(g.nodes.get(1), g.getNode("A"));
    }

    @Test
    public void testAddingSingularNode() {
        g.addNode(new Node("A"));
        assertTrue(g.containsNodeViaLabel("A"));
    }

    @Test
    public void testSetStartingNode() {
        setUpBasicNodes();

        g.setStartNode("A");

        assertEquals(g.startNode, g.getNode("A"));
    }

}
