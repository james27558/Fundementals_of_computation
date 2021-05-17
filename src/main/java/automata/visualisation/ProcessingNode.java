package automata.visualisation;

import automata.core.Node;

/**
 * This class holds information about nodes neccessary to draw them to the screen like coorindates and whether it's
 * sleected
 */
public class ProcessingNode {
    private int x;
    private int y;

    private Node node;

    private boolean isCursorHoveringOver;

    private boolean isSelectedAsSource;

    public ProcessingNode(Node node) {
        this.node = node;
        x = 20;
        y = 20;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
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
        x += x - repulsiveX;
        y += y - repulsiveY;
    }


}
