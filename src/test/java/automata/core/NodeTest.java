package automata.core;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NodeTest {

    Graph setUpNFA() {
        Graph g = new Graph(new String[]{"a", "b", "c"});
        g.addNode(new Node("A"));
        g.addNode(new Node("B"));
        g.addNode(new Node("C"));

        g.connectNodes("A", "B", "a");
        g.connectNodes("A", "C", "a");

        g.connectNodes("B", "C", "b");
        g.connectNodes("C", "B", "c");

        return g;
    }

    @Test
    void testWillSymbolCauseTransitionTrue() {
        Graph g = setUpNFA();

        Node nodeA = g.getNode("A");

        assertTrue(nodeA.willSymbolCauseTransition(Symbol.fromString("a")));
    }

    @Test
    void testWillSymbolCauseTransitionFalse() {
        Graph g = setUpNFA();

        Node nodeA = g.getNode("A");

        assertFalse(nodeA.willSymbolCauseTransition(Symbol.fromString("b")));
    }


    @Test
    void testGetDestinationNodesAfterTransition() {
        Graph g = setUpNFA();

        Node nodeA = g.getNode("A");
        Node nodeB = g.getNode("B");
        Node nodeC = g.getNode("C");

        Node[] expectedOutput = new Node[]{nodeB, nodeC};
        List<Node> actualOutput = nodeA.getDestinationNodesAfterTransition(Symbol.fromString("a"));


        assertArrayEquals(expectedOutput, actualOutput.toArray());
    }
}