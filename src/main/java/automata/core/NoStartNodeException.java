package automata.core;

/**
 * This exception is thrown when a word is being tested and no start node exists
 */
public class NoStartNodeException extends RuntimeException {

    NoStartNodeException() {
        super("A start node is needed to start testing a word");
    }
}
