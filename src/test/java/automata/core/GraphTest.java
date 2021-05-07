package automata.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GraphTest {

    Graph setUpDFA() {
        Graph g = setUpEmptyGraph();

        g.addNode(new Node("A"));
        g.addNode(new Node("B"));
        g.addNode(new Node("C"));

        g.connectNodes("A", "B", "a");
        g.connectNodes("B", "A", "a");
        g.connectNodes("A", "C", "b");
        g.connectNodes("C", "B", "b");

        return g;
    }

    Graph setUpEmptyGraph() {
        return new Graph(new String[]{"a", "b", "c"});
    }

    Graph setUpBasicNodes() {
        Graph g = new Graph(new String[]{"a", "b", "c"});

        g.addNode(new Node("A"));
        g.addNode(new Node("B"));
        g.addNode(new Node("C"));

        g.connectNodes("A", "B", "a");
        g.connectNodes("B", "A", "a");
        g.connectNodes("B", "C", "a");

        g.makeNodeAccepting("C");

        return g;
    }

    public Graph setUpBasicGraph() {
        Graph g = setUpBasicNodes();
        g.setStartNode("A");
        return g;
    }

    @Test
    public void testAddNodeAddsTheCorrectNode() {
        Graph g = setUpEmptyGraph();

        Node n = new Node("A");
        g.addNode(n);

        assertTrue(g.getNodes().contains(n));
    }

    @Test
    public void testAddNodes_WithDuplicateLabels_ThrowsLabelAlreadyExistsException() {
        Graph g = setUpEmptyGraph();

        Node n1 = new Node("A");
        Node n2 = new Node("A");
        g.addNode(n1);

        assertThrows(LabelAlreadyExistsException.class, () -> {
            g.addNode(n2);
        });
    }

    @Test
    public void testGetNode() {
        Graph g = setUpBasicNodes();
        // get(0) is node "A"
        assertEquals(g.getNodes().get(0), g.getNode("A"));
    }

    @Test
    public void testGetNode_WhenNodeDoesNotExist_ThrowsNodeNotFoundException() {
        Graph g = setUpEmptyGraph();

        assertThrows(NodeNotFoundException.class, () -> {
            g.getNode("A");
        });
    }

    @Test
    public void testAddingSingularNode() {
        Graph g = setUpEmptyGraph();

        g.addNode(new Node("A"));
        assertTrue(g.containsNode("A"));
    }

    @Test
    public void testContainsNode() {
        Graph g = setUpEmptyGraph();

        Node n = new Node("A");
        g.addNode(n);

        assertTrue(g.containsNode(n.getLabel()));
    }

    @Test
    public void testSetStartingNode() {
        Graph g = setUpBasicNodes();

        g.setStartNode("A");

        assertEquals(g.getStartNode(), g.getNode("A"));
    }

    @Test
    public void testMakeNodeAccepting() {
        Graph g = setUpEmptyGraph();

        Node n = new Node("A");
        g.addNode(n);
        g.makeNodeAccepting("A");

        assertTrue(n.isAccepting());
    }

    @Test
    public void testisNFA_True() {
        Graph g = setUpBasicGraph();

        assertTrue(g.isNFA());
    }

    @Test
    public void testisNFA_False() {
        Graph g = setUpDFA();

        assertFalse(g.isNFA());
    }
}
