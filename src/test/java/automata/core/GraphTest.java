package automata.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GraphTest {

    private Graph g;

    @BeforeEach
    void setUp() {
        g = new Graph(new String[]{"a", "b", "c"});
    }

    void setUpBasicNodes() {
        g.addNode(new Node("A"));
        g.addNode(new Node("B"));
        g.addNode(new Node("C"));

        g.connectNodes("A", "B", "a");
        g.connectNodes("B", "A", "a");
        g.connectNodes("B", "C", "a");

        g.makeNodeAccepting("C");
    }

    public void setUpBasicGraph() {
        setUpBasicNodes();

        g.setStartNode("A");
    }

    @Test
    public void testAddNodeAddsTheCorrectNode() {
        Node n = new Node("A");
        g.addNode(n);

        assertTrue(g.getNodes().contains(n));
    }

    @Test
    public void testAddNodes_WithDuplicateLabels_ThrowsLabelAlreadyExistsException() {
        Node n1 = new Node("A");
        Node n2 = new Node("A");
        g.addNode(n1);

        assertThrows(LabelAlreadyExistsException.class, () -> {
            g.addNode(n2);
        });
    }

    @Test
    public void testGetNode() {
        setUpBasicNodes();
        // get(0) is node "A"
        assertEquals(g.getNodes().get(0), g.getNode("A"));
    }

    @Test
    public void testGetNode_WhenNodeDoesNotExist_ThrowsNodeNotFoundException() {
        assertThrows(NodeNotFoundException.class, () -> {
            g.getNode("A");
        });
    }

    @Test
    public void testAddingSingularNode() {
        g.addNode(new Node("A"));
        assertTrue(g.containsNode("A"));
    }

    @Test
    public void testContainsNode() {
        Node n = new Node("A");
        g.addNode(n);

        assertTrue(g.containsNode(n.getLabel()));
    }

    @Test
    public void testSetStartingNode() {
        setUpBasicNodes();

        g.setStartNode("A");

        assertEquals(g.getStartNode(), g.getNode("A"));
    }

    @Test
    public void testMakeNodeAccepting() {
        Node n = new Node("A");
        g.addNode(n);
        g.makeNodeAccepting("A");

        assertTrue(n.isAccepting());
    }
}
