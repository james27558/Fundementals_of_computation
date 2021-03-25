public class ProcessingGraph {
    Graph g;

    ProcessingGraph(Graph g) {
        this.g = g;
    }

    /**
     * Takes a point and repels this processing node away from it by a small step
     */
    public void performForceDirectedStep() {
        for (Node n1 : g.nodes) {
            ProcessingNode processingNode1 = (ProcessingNode) n1;

            for (Node n2 : g.nodes) {
                ProcessingNode processingNode2 = (ProcessingNode) n2;
                processingNode1.performForceDirectedStep(processingNode2.x, processingNode2.y);

            }
        }

    }
}
