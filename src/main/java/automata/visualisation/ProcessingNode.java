package automata.visualisation;

import automata.core.Node;
import processing.core.PVector;

/**
 * This class holds information about nodes neccessary to draw them to the screen like coorindates and whether it's
 * sleected
 */
public class ProcessingNode {
    private PVector position;

    private Node node;

    private boolean isCursorHoveringOver;

    private boolean isSelectedAsSource;

    public ProcessingNode(Node node) {
        this.node = node;
        position = new PVector(20, 20);
    }

    public PVector getPosition() {
        return position;
    }

    public void setPosition(PVector position) {
        this.position = position;
    }

    public int getX() {
        return (int) position.x;
    }

    public void setX(int x) {
        position.x = x;
    }

    public int getY() {
        return (int) position.y;
    }

    public void setY(int y) {
        position.y = y;
    }

    public Node getNode() {
        return node;
    }

    public boolean isCursorHoveringOver() {
        return isCursorHoveringOver;
    }

    public void setCursorHoveringOver(boolean cursorHoveringOver) {
        this.isCursorHoveringOver = cursorHoveringOver;
    }

    public boolean isSelectedAsSource() {
        return isSelectedAsSource;
    }

    public void setSelectedAsSource(boolean selectedAsSource) {
        isSelectedAsSource = selectedAsSource;
    }

    public void performForceDirectedStep(int repulsiveX, int repulsiveY) {

    }


}
