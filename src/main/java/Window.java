import processing.core.PApplet;

public class Window extends PApplet {
    static PApplet ref;
    final int ELLIPSE_DIAMETER = 15;
    Graph graph;
    ProcessingGraph pGraph;
    boolean start = false;

    public static void main(String[] args) {
        PApplet.main("Window");
    }

    public void settings() {
        size(400, 400);

        ref = this;

        graph = new Graph(new String[]{"a", "b", "c"});
        graph.addNode(new ProcessingNode("A"));
        graph.addNode(new ProcessingNode("B"));
        graph.connectNodes("A", "B");
        graph.setStartNode(0);

        System.out.println();

        pGraph = new ProcessingGraph(graph);

    }

    public void draw() {
        frameRate(60);
        background(51);

        for (Node n : graph.nodes) {
            ProcessingNode node = (ProcessingNode) n;

            fill(0);
            if (node.selected) fill(255, 255, 0);

            ellipse(node.x, node.y, ELLIPSE_DIAMETER, ELLIPSE_DIAMETER);
        }

        if (start) pGraph.performForceDirectedStep();
    }

    @Override
    public void mouseMoved() {
        ProcessingNode selectedNode = null;
        for (int i = 0; i < graph.nodes.size(); i++) {
            ProcessingNode n = (ProcessingNode) graph.nodes.get(i);

            if (dist(mouseX, mouseY, n.x, n.y) < ELLIPSE_DIAMETER / 2.0) {
                selectedNode = n;
            } else {
                n.selected = false;
            }
        }

        if (selectedNode != null) selectedNode.selected = true;
    }

    @Override
    public void mouseDragged() {
        for (Node n : graph.nodes) {
            ProcessingNode node = (ProcessingNode) n;
            if (node.selected) {
                node.x = mouseX;
                node.y = mouseY;
            }
        }
    }

    @Override
    public void keyPressed() {
        if (keyCode == 32) start = true;
    }
}
