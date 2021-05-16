package automata.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;

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

    Graph setUpBasicGraph() {
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
    public void testContainsNodeReturnsFalse() {
        Graph g = setUpEmptyGraph();

        Node n = new Node("A");

        assertFalse(g.containsNode(n.getLabel()));
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
    public void testConnectNodes() {
        Graph g = new Graph(new String[]{"a"});
        Node nodeA = new Node("A");
        Node nodeB = new Node("B");
        g.addNode(nodeA);
        g.addNode(nodeB);
        g.connectNodes("A", "B", "a");

        assertEquals(nodeA.getTransitions().get(0).getDestination(), nodeB);
    }

    @Test
    public void testConnectNodes_ThrowsSymbolNotFoundException() {
        Graph g = new Graph(new String[]{"a"});
        Node nodeA = new Node("A");
        Node nodeB = new Node("B");
        g.addNode(nodeA);
        g.addNode(nodeB);
        assertThrows(SymbolNotFoundException.class, () -> {
            g.connectNodes("A", "B", "b");
        });
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

    /**
     * This function returns a graph that accepts words over the language {"a"} that have an even number of a's
     *
     * @return Automaton as specified
     */
    public Graph setUpEvenNumberOfAs_DFA() {
        Graph g = new Graph(new String[]{"a"});

        g.addNode(new Node("Accepting"));
        g.addNode(new Node("Non-Accepting"));

        g.connectNodes("Accepting", "Non-Accepting", "a");
        g.connectNodes("Non-Accepting", "Accepting", "a");

        g.makeNodeAccepting("Accepting");

        g.setStartNode("Accepting");

        return g;
    }

    @Test
    public void testStepTestingWord_ThrowsRuntimeException_WhenStartTestingWordHasntBeenCalled() {
        Graph g = setUpEvenNumberOfAs_DFA();

        assertThrows(RuntimeException.class, () -> {
            g.stepTestingWord("a");
        });
    }

    @Test
    public void testPassingWordThroughEvenAsDFA_AcceptValidWords() {
        Graph g = setUpEvenNumberOfAs_DFA();

        // Test words with an even number of a's.
        // 2 a's, 4 a's, 6 a's, 8 a's, 10 a's
        for (int i = 0; i <= 10; i += 2) {
            g.startTestingWord();

            // Pass the number of a's currently being tested
            for (int j = 0; j < i; j++) {
                g.stepTestingWord("a");
            }

            assertTrue(g.endTestingWord(), "Word with " + i + " a's was not accepted by the automaton when it should've been");
        }
    }

    @Test
    public void testPassingWordThroughEvenAsDFA_NotAcceptInvalidWords() {
        Graph g = setUpEvenNumberOfAs_DFA();

        // Test words with an odd number of a's.
        // 1 a's, 3 a's, 5 a's, 7 a's, 9 a's, 11 a's
        for (int i = 1; i <= 11; i += 2) {
            g.startTestingWord();

            // Pass the number of a's currently being tested
            for (int j = 0; j < i; j++) {
                g.stepTestingWord("a");
            }

            assertFalse(g.endTestingWord(), "Word with " + i + " a's was accepted by the automaton when it should've been");
        }
    }

    /**
     * This function returns a DFA detailed in the notes on page 22. It accepts all words in the language defined by the
     * regular expression (ab*a)|(ba*b)
     *
     * @return Automaton as specified
     */
    public Graph setUpDFA1() {
        Graph g = new Graph(new String[]{"a", "b"});

        g.addNode(new Node("0"));
        g.addNode(new Node("0a"));
        g.addNode(new Node("0b"));
        g.addNode(new Node("1"));
        g.addNode(new Node("2"));

        g.setStartNode("0");

        g.connectNodes("0", "0a", "a");
        g.connectNodeToSelf("0a", "b");
        g.connectNodes("0a", "1", "a");

        g.connectNodes("0", "0b", "b");
        g.connectNodeToSelf("0b", "a");
        g.connectNodes("0b", "1", "b");

        g.connectNodes("1", "2", "a");
        g.connectNodes("1", "2", "b");

        g.connectNodeToSelf("2", "a");
        g.connectNodeToSelf("2", "b");

        g.makeNodeAccepting("1");

        return g;
    }

    @Test
    public void testPassingWordsThroughDFA1_AcceptValidWords() {
        Graph g = setUpDFA1();

        // Test words that are accepted by this automaton
        String[] validWords = new String[]{"aba", "abba", "abbba", "bab", "baab", "baaab"};

        for (String validWord : validWords) {
            g.startTestingWord();

            // Pass the word through the automaton
            for (int i = 0; i < validWord.length(); i++) {
                String symbol = String.valueOf(validWord.charAt(i));

                g.stepTestingWord(symbol);
            }

            // It should be accepted
            assertTrue(g.endTestingWord(), "The word " + validWord + " was not accepted by the automaton when it should've been");
        }
    }

    @Test
    public void testPassingWordsThroughDFA1_NotAcceptInvalidWords() {
        Graph g = setUpDFA1();

        // Test words that are not accepted by this automaton
        String[] invalidWords = new String[]{"a", "b", "abb", "abbb", "abaa", "abaaa", "aaba"};

        for (String invalidWord : invalidWords) {
            g.startTestingWord();

            // Pass the word through the automaton
            for (int i = 0; i < invalidWord.length(); i++) {
                String symbol = String.valueOf(invalidWord.charAt(i));

                g.stepTestingWord(symbol);
            }

            // It should not be accepted
            assertFalse(g.endTestingWord(), "The word " + invalidWord + " was accepted by the automaton when it shouldn't have been");
        }
    }

    @Test
    public void serializationTestEmptyGraph(@TempDir Path tempDir) throws IOException, ClassNotFoundException {
        Graph g = new Graph(new String[]{"a"});

        Path filepath = tempDir.resolve("serializationTestOut.ser");

        g.save(filepath);

        Graph loadedGraph = Graph.load(filepath);

        assertEquals(loadedGraph, g);
    }

    @Test
    public void serializationTestGraphWithOneNode(@TempDir Path tempDir) throws IOException, ClassNotFoundException {
        Graph g = new Graph(new String[]{"a"});
        g.addNode(new Node("A"));

        Path filepath = tempDir.resolve("serializationTestOut.ser");

        g.save(filepath);

        Graph loadedGraph = Graph.load(filepath);

        assertEquals(loadedGraph, g);
    }

    @Test
    public void serializationTestGraphWithConnections(@TempDir Path tempDir) throws IOException, ClassNotFoundException {
        Graph g = new Graph(new String[]{"a"});
        g.addNode(new Node("A"));
        g.addNode(new Node("B"));
        g.connectNodes("A", "B", "a");

        Path filepath = tempDir.resolve("serializationTestOut.ser");

        g.save(filepath);

        Graph loadedGraph = Graph.load(filepath);

        assertEquals(loadedGraph, g);
    }

}
