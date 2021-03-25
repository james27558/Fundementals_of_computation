public class ProcessingNode extends Node {
    int x;
    int y;

    boolean selected;

    public ProcessingNode(String label) {
        super(label);
    }


    public void performForceDirectedStep(int repulsiveX, int repulsiveY) {
        x += x - repulsiveX;
        y += y - repulsiveY;
    }


}
