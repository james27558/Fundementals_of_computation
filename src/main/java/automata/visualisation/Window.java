package automata.visualisation;

import automata.core.Graph;
import automata.core.Transition;
import controlP5.*;
import processing.core.PApplet;

import java.io.IOException;
import java.util.List;

public class Window extends PApplet {
    static PApplet ref;

    ProcessingGraph pGraph;
    boolean start = false;

    ControlP5 controlP5;

    public static void main(String[] args) {
        PApplet.main("automata.visualisation.Window");
    }

    public void settings() {
        size(800, 600);

        ref = this;

        Graph graph = null;

        try {
//            graph.save("out.ser");
            graph = Graph.load("out.ser");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        pGraph = new ProcessingGraph(graph);

    }

    public void setup() {
        ControlFont cf1 = new ControlFont(createFont("Arial", 15));
        controlP5 = new ControlP5(this);

        Button button = new Button(controlP5, "Yo");
        button.addCallback(new CallbackListener() {
            @Override
            public void controlEvent(CallbackEvent theEvent) {
                switch (theEvent.getAction()) {
                    case ControlP5.ACTION_CLICK:
                        System.out.println("aaa");
                }
            }
        });

        Textfield textfield = controlP5.addTextfield("Text");
    }

    public void draw() {
        frameRate(60);
        background(51);

        drawTransitions();

        for (ProcessingNode node : pGraph.getNodes()) {
            fill(0);
            if (node.isSelected()) fill(255, 255, 0);

            ellipse(node.getX(), node.getY(), pGraph.ELLIPSE_DIAMETER, pGraph.ELLIPSE_DIAMETER);
        }

//        if (start) pGraph.performForceDirectedStep();
    }

    public void drawTransitions() {
        stroke(0);

        for (ProcessingNode node : pGraph.getNodes()) {
            for (Transition transition : node.getNode().getTransitions()) {
                ProcessingNode transitionDestination = pGraph.getNode(transition.getDestination());

                // Get the midpoint of the line
                float midPointX = node.getX() + (transitionDestination.getX() - node.getX()) / 2;
                float midPointY = node.getY() + (transitionDestination.getY() - node.getY()) / 2;

                line(node.getX(), node.getY(), transitionDestination.getX(), transitionDestination.getY());

                fill(0);
                text(transition.getSymbol().toString(), midPointX, midPointY);
            }
        }
    }

    @Override
    public void mouseMoved() {
        List<ProcessingNode> nodes = pGraph.getNodes();

        // Find the selected node
        ProcessingNode selectedNode = null;
        for (int i = 0; i < nodes.size(); i++) {
            ProcessingNode n = (ProcessingNode) nodes.get(i);

            if (dist(mouseX, mouseY, n.getX(), n.getY()) < pGraph.ELLIPSE_DIAMETER / 2.0) {
                selectedNode = n;
            } else {
                n.setSelected(false);
            }
        }

        // Set the selected node
        if (selectedNode != null) selectedNode.setSelected(true);
    }

    @Override
    public void mouseDragged() {
        for (ProcessingNode node : pGraph.getNodes()) {
            if (node.isSelected()) {
                node.setX(mouseX);
                node.setY(mouseY);
            }
        }
    }

    @Override
    public void keyPressed() {
        if (keyCode == 32) start = true;
    }
}
