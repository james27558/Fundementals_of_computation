package automata.visualisation;

import automata.core.Graph;
import automata.core.Node;
import automata.core.NodeNotFoundException;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

/**
 * This class holds the necessary information (x,y coordinates of nodes, which one is selected) for drawing a Graph to a
 * canvas
 */
public class ProcessingGraph {
    public static float ELLIPSE_DIAMETER = 25;
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

    public List<ProcessingNode> getAllDestinationNodes(ProcessingNode node) {
        List<ProcessingNode> destinations = new ArrayList<>();

        for (Node underlyingNode : node.getNode().getAllDestinationNodes()) {
            destinations.add(getNode(underlyingNode));
        }

        return destinations;
    }

    public void startForceDirectedLayout() {
        for (ProcessingNode node : getNodes()) {
            PVector position = new PVector(Window.ref.random(1), Window.ref.random(1));
            position.x = position.x * Window.CANVAS_WIDTH;
            position.y = position.y * Window.CANVAS_HEIGHT;
            node.setPosition(position);
        }
    }

    /**
     * Takes a point and repels this processing node away from it by a small step
     */
    public void stepForceDirectedLayout() {
        int c1 = 2;
        int c2 = 1;
        int c3 = 1;
        float c4 = 0.1f;

        PVector overallForce = new PVector();

        for (ProcessingNode sourceNode : getNodes()) {
            PVector sourceNodePos = sourceNode.getPosition();

            for (ProcessingNode adjacentNode : getAllDestinationNodes(sourceNode)) {
                PVector otherNodePos = adjacentNode.getPosition();

                PVector directionToApplyForce = sourceNodePos.copy()
                        .sub(otherNodePos)
                        .normalize();

                PVector distance = otherNodePos.copy().sub(sourceNodePos);
                PVector unitDistance = distance.copy().normalize();
                PVector force = new PVector(1 / unitDistance.x, 1 / unitDistance.y);

                overallForce.add(force);
            }

            for (ProcessingNode otherNode : getNodes()) {
                if (otherNode.equals(sourceNode)) continue;

                PVector otherNodePos = otherNode.getPosition();


            }

            sourceNodePos.add(overallForce);
        }

    }

    private static class PVectorHelper {
        private static PVector mult(PVector v1, PVector v2) {
            return new PVector(v1.x * v2.x, v1.y * v2.y);
        }

        private static PVector mult(PVector vector, float xMult, float yMult) {
            return new PVector(vector.x * xMult, vector.y * yMult);
        }

        private static PVector abs(PVector vector) {
            return new PVector(Window.abs(vector.x), Window.abs(vector.x));
        }

        private static PVector pow(PVector vector, int power) {
            return new PVector((float) Math.pow(vector.x, power), (float) Math.pow(vector.y, power));
        }

        private static PVector div(PVector v1, PVector v2) {
            return new PVector(v1.x / v2.x, v1.y / v2.y);
        }

        private static PVector log(PVector vector) {
            return new PVector((float) Math.log(vector.x), (float) Math.log(vector.y));
        }
    }
}
