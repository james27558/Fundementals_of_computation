package automata.visualisation;

import automata.core.Graph;
import automata.core.Node;
import automata.core.Transition;
import controlP5.*;
import processing.core.PApplet;
import processing.event.MouseEvent;

import javax.swing.*;
import java.io.IOException;
import java.util.List;

public class Window extends PApplet {
    static PApplet ref;

    ProcessingGraph pGraph;
    boolean start = false;

    ControlP5 controlP5;
    Textfield addNodeLabelTextfield;
    Tool currentlySelectedTool = Tool.NO_TOOL;


    public static void main(String[] args) {
        PApplet.main("automata.visualisation.Window");
    }

    public void settings() {
        size(800, 600);

        // Sets the style for Swing components to reflect the native style on their OS
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //        JFileChooser jFileChooser = new JFileChooser();
        //        if (jFileChooser.showOpenDialog(new JPanel()) == JFileChooser.APPROVE_OPTION) {
        //            File file = jFileChooser.getSelectedFile();
        //        }

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
        controlP5 = new ControlP5(this);

        Group g1 = controlP5.addGroup("Make New Graph")
                .setPosition(0, 10)
                .setWidth(180)
                .setBackgroundHeight(100)
                .setBackgroundColor(color(21));

        Textfield textfield = controlP5.addTextfield("Alphabet (Seperated by spaces)")
                .setGroup("Make New Graph")
                .setPosition(20, 10)
                .setWidth(140);

        controlP5.addButton("New Graph")
                .setGroup("Make New Graph")
                .setPosition(20, 60)
                .addCallback(new CallbackListener() {
                    @Override
                    public void controlEvent(CallbackEvent theEvent) {
                        if (theEvent.getAction() == ControlP5.ACTION_CLICK) {
                            String[] alphabet = textfield.getText().split(" ");

                            if (alphabet.length != 0) {
                                Graph graph = new Graph(alphabet);
                                pGraph = new ProcessingGraph(graph);
                            }
                        }
                    }
                });

        Group g2 = controlP5.addGroup("Tools")
                .setPosition(180, 10)
                .setWidth(120)
                .setBackgroundHeight(100)
                .setBackgroundColor(color(21));

        RadioButton radioButton = controlP5.addRadio("Pick a tool");

        radioButton.addItem("No Tool", 0)
                .addItem("Add Node Tool", 1)
                .addItem("Add Connection Tool", 2)
                .addItem("Remove Node Tool", 3)
                .addItem("Remove Connection Tool", 4)
                .setGroup("Tools")
                .setPosition(10, 10)
                .setNoneSelectedAllowed(false)
                .activate(0);


        Group addNodeMenu = controlP5.addGroup("Add Node Menu")
                .setPosition(300, 10)
                .setWidth(100)
                .setBackgroundHeight(50)
                .setBackgroundColor(color(21));

        addNodeLabelTextfield = controlP5.addTextfield("Node Label")
                .setGroup("Add Node Menu")
                .setPosition(20, 10)
                .setWidth(60);

        radioButton.getItem(0).addListener(new ControlListener() {
            @Override
            public void controlEvent(ControlEvent theEvent) {
                addNodeMenu.close();
                addNodeMenu.enableCollapse();

                currentlySelectedTool = Tool.NO_TOOL;
            }
        });

        radioButton.getItem(1).addListener(new ControlListener() {
            @Override
            public void controlEvent(ControlEvent theEvent) {
                addNodeMenu.open();
                addNodeMenu.disableCollapse();

                currentlySelectedTool = Tool.ADD_NODE;
            }
        });

        radioButton.getItem(2).addListener(new ControlListener() {
            @Override
            public void controlEvent(ControlEvent theEvent) {
                addNodeMenu.close();
                addNodeMenu.enableCollapse();

                currentlySelectedTool = Tool.ADD_CONNECTION;
            }
        });

        radioButton.getItem(3).addListener(new ControlListener() {
            @Override
            public void controlEvent(ControlEvent theEvent) {
                addNodeMenu.close();
                addNodeMenu.enableCollapse();
                currentlySelectedTool = Tool.REMOVE_NODE;
            }
        });

        radioButton.getItem(4).addListener(new ControlListener() {
            @Override
            public void controlEvent(ControlEvent theEvent) {
                addNodeMenu.close();
                addNodeMenu.enableCollapse();
                currentlySelectedTool = Tool.REMOVE_CONNECTION;
            }
        });
    }

    public void draw() {
        frameRate(60);
        background(51);

        drawTransitions();

        for (ProcessingNode node : pGraph.getNodes()) {
            fill(0);
            if (node.isCursorHoveringOver()) fill(255, 255, 0);

            ellipse(node.getX(), node.getY(), pGraph.ELLIPSE_DIAMETER, pGraph.ELLIPSE_DIAMETER);
        }

        if (currentlySelectedTool == Tool.ADD_NODE) {
            fill(255, 50);
            ellipse(mouseX, mouseY, pGraph.ELLIPSE_DIAMETER, pGraph.ELLIPSE_DIAMETER);
        }

        //        if (start) pGraph.performForceDirectedStep();
    }

    /**
     * Draws all transitions in the Graph
     */
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

        // Set all nodes' isCursorHoveringOver to false
        nodes.forEach(node -> node.setCursorHoveringOver(false));

        // If we're currently hovering over a node, set isCursorHoveringOver to true
        ProcessingNode currentlyHoveredNode = getNodeCurrentlyBingHoveredOver();
        if (currentlyHoveredNode != null) currentlyHoveredNode.setCursorHoveringOver(true);
    }

    public ProcessingNode getNodeCurrentlyBingHoveredOver() {
        List<ProcessingNode> nodes = pGraph.getNodes();

        /*
        Iterate backwards through the nodes, if the cursor is in the node then return that node. As we draw nodes in
        the order they lie in the nodes list then this will ensure that any nodes that are drawn 'above' the rest on
        the canvas
         */
        for (int i = nodes.size() - 1; i >= 0; i--) {
            ProcessingNode node = nodes.get(i);

            if (dist(mouseX, mouseY, node.getX(), node.getY()) < pGraph.ELLIPSE_DIAMETER / 2.0) {
                return node;
            }
        }

        return null;
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        // Return if the mouse is over a controlp5 window. Any clicks on those windows are handled by the controlp5
        // library automatically
        if (controlP5.getWindow().isMouseOver()) return;

        switch (currentlySelectedTool) {
            case NO_TOOL:
                break;
            case ADD_NODE:
                addNode();
                break;
            case REMOVE_NODE:
                removeNode();
                break;
            case ADD_CONNECTION:
                //                addConnection();
                break;

        }


    }

    /**
     * Adds a node to the graph at the current mouse coordinates with a label from the addNodeLabelTextfield. If
     * addNodeLabelTextfield is empty then it will flash red and not create the node.
     */
    public void addNode() {
        // If the node label box is empty then flash the box red
        if (addNodeLabelTextfield.getText().isEmpty()) {
            addNodeLabelTextfield.setColorBackground(0xA0FF0000);
            // After 250 milliseconds, turn the box back to its regular background colour
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            addNodeLabelTextfield.setColorBackground(0xff003652);
                        }
                    },
                    250
            );

        } else {
            // Otherwise a node with that label is good to add
            pGraph.addNode(new Node(addNodeLabelTextfield.getText()), mouseX, mouseY);
        }
    }

    /**
     * Removes the currently selected node from the graph
     */
    public void removeNode() {
        for (ProcessingNode node : pGraph.getNodes()) {
            if (node.isCursorHoveringOver()) {
                pGraph.removeNode(node);
                return;
            }
        }
    }

    //    /**
    //     * Adds a connection to the graph
    //     */
    //    public void addConnection() {
    //        ProcessingNode currentlyHoveringnode = getNodeCurrentlyBingHoveredOver();
    //
    //        if (currentlyHoveringnode)
    //    }

    @Override
    public void mouseDragged() {
        // If no tool is selected then the user should be able to drag nodes around
        if (currentlySelectedTool == Tool.NO_TOOL) {
            for (ProcessingNode node : pGraph.getNodes()) {
                if (node.isCursorHoveringOver()) {
                    node.setX(mouseX);
                    node.setY(mouseY);
                }
            }
        }
    }

    @Override
    public void keyPressed() {
        if (keyCode == 32) System.out.println(pGraph.getGraph());
    }

    enum Tool {
        NO_TOOL,
        ADD_NODE,
        ADD_CONNECTION,
        REMOVE_NODE,
        REMOVE_CONNECTION
    }
}
