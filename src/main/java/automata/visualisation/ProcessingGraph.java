package automata.visualisation;

import automata.core.Graph;
import automata.core.Node;
import automata.core.NodeNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * This class holds the necessary information (x,y coordinates of nodes, which one is selected) for drawing a Graph to a
 * canvas
 */
public class ProcessingGraph {
    final int ELLIPSE_DIAMETER = 25;
    private Graph g;
    private List<ProcessingNode> nodes;

    /**
     * Takes a Graph and adds the nodes in the graph to this automata.visualisation.ProcessingGraph object with the
     * coordinates of the nodes initialised to a default value
     *
     * @param g The graph to display
     */
    public ProcessingGraph(Graph g) {
        this.g = g;
        nodes = new ArrayList<>();

        // Link the nodes in the graph to this automata.visualisation.ProcessingGraph
        for (Node n : g.getNodes()) {
            nodes.add(new ProcessingNode(n));
        }

    }

    public Graph getGraph() {
        return g;
    }

    public List<ProcessingNode> getNodes() {
        return nodes;
    }

    public ProcessingNode getNode(Node node) {
        for (ProcessingNode processingNode : getNodes()) {
            if (processingNode.getNode().equals(node)) return processingNode;
        }

        throw new NodeNotFoundException(node);
    }

    public void addNode(Node node, int nodeX, int nodeY) {
        getGraph().addNode(node);

        ProcessingNode newProcessingNode = new ProcessingNode(node);
        newProcessingNode.setX(nodeX);
        newProcessingNode.setY(nodeY);

        nodes.add(newProcessingNode);
    }

    public void removeNode(ProcessingNode processingNode) {
        // Remove the node from 'nodes'
        getNodes().remove(processingNode);
        // remove the node from the underlying Graph
        getGraph().removeNode(processingNode.getNode().getLabel());
    }

    public void addTransition(ProcessingNode sourceNode, ProcessingNode destinationNode,
                              String transitionSymbolString) {

        getGraph().connectNodes(sourceNode.getNode().getLabel(), destinationNode.getNode().getLabel(),
                transitionSymbolString);
    }

    //    /**
    //     * Takes a point and repels this processing node away from it by a small step
    //     */
    //    public void performForceDirectedStep() {
    //        for (Node n1 : g.nodes) {
    //            if (n1 instanceof automata.visualisation.ProcessingNode) automata.visualisation.ProcessingNode
    //            processingNode1 = (automata.visualisation.ProcessingNode) n1;
    //
    //            for (Node n2 : g.nodes) {
    //                automata.visualisation.ProcessingNode processingNode2 = (automata.visualisation.ProcessingNode)
    //                n2;
    //                processingNode1.performForceDirectedStep(processingNode2.x, processingNode2.y);
    //
    //            }
    //        }
    //
    //    }
}
