package automata.visualisation;

import automata.core.Graph;
import automata.core.Node;
import automata.core.Transition;
import controlP5.*;
import processing.core.PApplet;
import processing.event.MouseEvent;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Window extends PApplet {
    static PApplet ref;

    ProcessingGraph pGraph;
    boolean start = false;

    ControlP5 controlP5;
    Textfield addNodeLabelTextfield;
    Textfield addTransitionTransitionSymbol;
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

        // Menu to make a new graph and input an alphabet for that graph, seperated by spaces
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

        // Menu for a list of tools
        Group g2 = controlP5.addGroup("Tools")
                .setPosition(180, 10)
                .setWidth(120)
                .setBackgroundHeight(100)
                .setBackgroundColor(color(21));

        RadioButton radioButton = controlP5.addRadio("Tools RadioButton")
                .addItem("No Tool", 0)
                .addItem("Add Node", 1)
                .addItem("Add Transition", 2)
                .addItem("Remove Node", 3)
                .addItem("Remove Transition", 4)
                .setGroup("Tools")
                .setPosition(10, 10)
                .setNoneSelectedAllowed(false)
                .activate(0);


        Group addNodeMenu = controlP5.addGroup("Add Node Menu")
                .setPosition(300, 10)
                .setWidth(100)
                .setBackgroundHeight(50)
                .setBackgroundColor(color(21))
                .hide()
                .disableCollapse();

        addNodeLabelTextfield = controlP5.addTextfield("Node Label")
                .setGroup("Add Node Menu")
                .setPosition(20, 10)
                .setWidth(60);

        Group addTransition = controlP5.addGroup("Add Transition Menu")
                .setPosition(300, 10)
                .setWidth(100)
                .setBackgroundHeight(50)
                .setBackgroundColor(color(21))
                .hide()
                .disableCollapse();

        addTransitionTransitionSymbol = controlP5.addTextfield("Transition Symbol")
                .setGroup("Add Transition Menu")
                .setPosition(20, 10)
                .setWidth(60);

        Group saveLoad = controlP5.addGroup("Save / Load")
                .setPosition(400, 10)
                .setWidth(80)
                .setBackgroundHeight(70)
                .setBackgroundColor(color(21));

        controlP5.addButton("Save Graph")
                .setPosition(10, 10)
                .setWidth(60)
                .setGroup("Save / Load")
                .addListener(new ControlListener() {
                    @Override
                    public void controlEvent(ControlEvent theEvent) {
                        // Make a new file chooser
                        JFileChooser jFileChooser = new JFileChooser();

                        // Show the file chooser to the user, if they select a file to save it in then call save the
                        // graph
                        if (jFileChooser.showSaveDialog(new JPanel()) == JFileChooser.APPROVE_OPTION) {
                            File file = jFileChooser.getSelectedFile();

                            try {
                                pGraph.getGraph().save(file.getAbsolutePath());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

        controlP5.addButton("Load Graph")
                .setPosition(10, 40)
                .setWidth(60)
                .setGroup("Save / Load")
                .addListener(new ControlListener() {
                    @Override
                    public void controlEvent(ControlEvent theEvent) {
                        // Make a new file chooser
                        JFileChooser jFileChooser = new JFileChooser();

                        // Show the file chooser to the user, if they select a file to save it in then call save the
                        // graph
                        if (jFileChooser.showOpenDialog(new JPanel()) == JFileChooser.APPROVE_OPTION) {
                            File file = jFileChooser.getSelectedFile();

                            try {
                                pGraph = new ProcessingGraph(Graph.load(file.getAbsolutePath()));
                            } catch (IOException | ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

        radioButton.getItem(0).addListener(new ControlListener() {
            @Override
            public void controlEvent(ControlEvent theEvent) {
                currentlySelectedTool = Tool.NO_TOOL;

                resetToolSideEffects();
            }
        });

        radioButton.getItem(1).addListener(new ControlListener() {
            @Override
            public void controlEvent(ControlEvent theEvent) {
                addNodeMenu.show();
                addTransition.hide();

                resetToolSideEffects();

                currentlySelectedTool = Tool.ADD_NODE;
            }
        });

        radioButton.getItem(2).addListener(new ControlListener() {
            @Override
            public void controlEvent(ControlEvent theEvent) {
                addNodeMenu.hide();
                addTransition.show();

                resetToolSideEffects();

                currentlySelectedTool = Tool.ADD_CONNECTION;
            }
        });

        radioButton.getItem(3).addListener(new ControlListener() {
            @Override
            public void controlEvent(ControlEvent theEvent) {
                addNodeMenu.hide();
                addTransition.hide();

                resetToolSideEffects();

                currentlySelectedTool = Tool.REMOVE_NODE;
            }
        });

        radioButton.getItem(4).addListener(new ControlListener() {
            @Override
            public void controlEvent(ControlEvent theEvent) {
                addNodeMenu.hide();
                addTransition.hide();


                resetToolSideEffects();

                currentlySelectedTool = Tool.REMOVE_CONNECTION;
            }
        });
    }

    /**
     * Any temporary changes to the display that a tool makes will be undone by this function
     */
    public void resetToolSideEffects() {
        // The Add Transition tool selects a node as a source node which is displayed in a different colour, reset this
        pGraph.getNodes().forEach(node -> node.setSelectedAsSource(false));
    }

    public void draw() {
        frameRate(60);
        background(51);

        drawTransitions();

        for (ProcessingNode node : pGraph.getNodes()) {
            fill(176);
            if (node.isCursorHoveringOver()) fill(255, 255, 0);
            if (node.isSelectedAsSource()) fill(0, 255, 0);

            ellipse(node.getX(), node.getY(), pGraph.ELLIPSE_DIAMETER, pGraph.ELLIPSE_DIAMETER);
            if (node.getNode().isAccepting()) ellipse(node.getX(), node.getY(), (float) (pGraph.ELLIPSE_DIAMETER - 0.5),
                    (float) (pGraph.ELLIPSE_DIAMETER - 0.5));
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
        ProcessingNode currentlyHoveredNode = getNodeCurrentlyBeingHoveredOver();
        if (currentlyHoveredNode != null) currentlyHoveredNode.setCursorHoveringOver(true);
    }

    public ProcessingNode getNodeCurrentlyBeingHoveredOver() {
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
                addTransition();
                break;

        }
    }

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

    /**
     * Adds a transition to the graph
     */
    public void addTransition() {
        ProcessingNode currentlyHoveringnode = getNodeCurrentlyBeingHoveredOver();

        if (currentlyHoveringnode != null) {
            ProcessingNode sourceNode = null;

            // If there is a node marked as the source node in the ProcessingGraph then store it
            for (ProcessingNode processingNode : pGraph.getNodes()) {
                if (processingNode.isSelectedAsSource()) {
                    sourceNode = processingNode;
                    break;
                }
            }

            // If we didn't find a source node then make the one being hovered over, the new source node
            if (sourceNode == null) {
                currentlyHoveringnode.setSelectedAsSource(true);
            } else {
                // Otherwise, we did find a source node and we want to make the currently being hovered over
                // node, the desination

                pGraph.addTransition(sourceNode, currentlyHoveringnode, addTransitionTransitionSymbol.getText());

                sourceNode.setSelectedAsSource(false);
                sourceNode = null;
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
