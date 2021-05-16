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

    private boolean selected;

    public ProcessingNode(Node node) {
        this.node = node;
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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void performForceDirectedStep(int repulsiveX, int repulsiveY) {
        x += x - repulsiveX;
        y += y - repulsiveY;
    }


}
