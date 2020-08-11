import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;

/**
 * Represents the Quad-Tree as a tree with nodes and connections
 */
public class VisTree extends QuadTreeFormatPanel {
    /**
     * Organizes the visual components.
     * It divided the visual components into three layer and the root.
     * In this case the visual components are nodes which are connected with lines.
     */
    private enum NODE_LAYER {
        ROOT(1), LAYER_1(4), LAYER_2(16), LAYER_3(64);
        public JButton[] nodeLayerArray;
        public JButton singleNode;

        NODE_LAYER(int amount) {
            if (amount == 1) {
                this.nodeLayerArray = null;
                this.singleNode = new JButton();
            } else {
                this.nodeLayerArray = new JButton[amount];
                this.singleNode = null;
            }
        }
    }

    /**
     * The ambivalent color for the line between the node if the higher node are inactive/unused
     */
    private Color normalColorLine;
    /**
     * The ambivalent color for the line between the node if the higher node are active/used
     */
    private Color highlightColorLine;

    /**
     * Creates a new JPanel were the 'root + 3 layer Quad-Tree' is represented as a visual tree structure with nodes and connections.
     *
     * @param nodeDimension      The size of one single node.
     * @param smallestGap        The gap in the lowest layer between the nodes
     * @param verticalGap        The between each layer in the tree structure.
     * @param normalColor        {@link #normalColor}
     * @param highlightColor     {@link #highlightColor}
     * @param normalColorLine    {@link #normalColorLine}
     * @param highlightColorLine {@link #highlightColorLine}
     */
    public VisTree(Dimension nodeDimension, int smallestGap, int verticalGap, Color normalColor, Color highlightColor, Color normalColorLine, Color highlightColorLine) {
        super(normalColor, highlightColor);
        this.normalColorLine = normalColorLine;
        this.highlightColorLine = highlightColorLine;

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        int secondLayerGap = (smallestGap + nodeDimension.width) * NUMBER_OF_DIRECTIONS - nodeDimension.width;
        int firstLayerGap = (secondLayerGap + nodeDimension.width) * 4 - nodeDimension.width;

        JPanel rootLayer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        JPanel firstLayer = new JPanel(new FlowLayout(FlowLayout.CENTER, firstLayerGap, 0));
        JPanel secondLayer = new JPanel(new FlowLayout(FlowLayout.CENTER, secondLayerGap, 0));
        JPanel thirdLayer = new JPanel(new FlowLayout(FlowLayout.CENTER, smallestGap, 0));

        rootLayer.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        firstLayer.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        secondLayer.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        thirdLayer.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        initNodes(NODE_LAYER.ROOT, rootLayer, nodeDimension);
        initNodes(NODE_LAYER.LAYER_1, firstLayer, nodeDimension);
        initNodes(NODE_LAYER.LAYER_2, secondLayer, nodeDimension);
        initNodes(NODE_LAYER.LAYER_3, thirdLayer, nodeDimension);

        this.add(Box.createRigidArea(new Dimension(0, 10)));
        this.add(rootLayer);
        this.add(Box.createRigidArea(new Dimension(0, verticalGap)));
        this.add(firstLayer);
        this.add(Box.createRigidArea(new Dimension(0, verticalGap)));
        this.add(secondLayer);
        this.add(Box.createRigidArea(new Dimension(0, verticalGap)));
        this.add(thirdLayer);
        this.add(Box.createRigidArea(new Dimension(0, 10)));

        rootLayer.setMaximumSize(rootLayer.getPreferredSize());
        firstLayer.setMaximumSize(firstLayer.getPreferredSize());
        secondLayer.setMaximumSize(secondLayer.getPreferredSize());
        thirdLayer.setMaximumSize(thirdLayer.getPreferredSize());
    }

    /**
     * Initializes all nodes from a given layer as inactive/unused
     *
     * @param nodeLayer     The layer which should initialize.
     * @param nodePanel     The panel on which the layer should be added.
     * @param nodeDimension The size of one single node.
     */
    private void initNodes(NODE_LAYER nodeLayer, JPanel nodePanel, Dimension nodeDimension) {
        if (nodeLayer == NODE_LAYER.ROOT) {
            initNode(nodeLayer.singleNode, nodePanel, nodeDimension);
            return;
        }

        for (int i = 0; i < nodeLayer.nodeLayerArray.length; i++) {
            nodeLayer.nodeLayerArray[i] = new JButton();
            initNode(nodeLayer.nodeLayerArray[i], nodePanel, nodeDimension);
        }
    }

    /**
     * Initializes the given node as inactive/unused on the given panel.
     *
     * @param nodeButton    The node to initialize.
     * @param nodePanel     The panel on which the node should be added.
     * @param nodeDimension The size of the node.
     */
    private void initNode(JButton nodeButton, JPanel nodePanel, Dimension nodeDimension) {
        nodeButton.addActionListener(this);
        nodeButton.setBackground(normalColor);
        nodeButton.setPreferredSize(nodeDimension);
        nodePanel.add(nodeButton);
    }

    /**
     * Draws the lines between the nodes in the source and the nodes in destination.
     * The first node in source will be connected to the first four nodes in destination and so on.
     * The color of the lines are depending on the node state(active/inactive) from the source node.
     * They will drawn in the ambivalent colors from {@link #normalColor} and {@link #highlightColor}
     *
     * @param g           The graphic object to draw on.
     * @param source      The nodes to connect with four nodes in destination.
     * @param destination The nodes to connect with one node in source.
     */
    private void paintConnections(Graphics g, JButton[] source, JButton[] destination) {
        Graphics2D g2d = (Graphics2D) g;

        Point tmpSrcPosition;
        Point tmpDstPosition;
        int i = 0;
        for (JButton sourceButton : source) {
            tmpSrcPosition = calcNodePositionAsSource(sourceButton);

            if (sourceButton.getBackground() == normalColor) {
                g2d.setColor(normalColorLine);
            } else {
                g2d.setColor(highlightColorLine);
            }

            for (int j = 0; j < 4; j++) {
                tmpDstPosition = calcNodePositionAsDestination(destination[i * NUMBER_OF_DIRECTIONS + j]);
                g2d.drawLine(tmpSrcPosition.x, tmpSrcPosition.y, tmpDstPosition.x, tmpDstPosition.y);
            }

            i++;
        }
    }

    /**
     * Calculates the start position for the line between to nodes
     *
     * @param source The node where the line should start.
     * @return The start position for the connection line.
     */
    private Point calcNodePositionAsSource(JButton source) {
        Point positionButtonRelative = source.getLocation();
        Point sourcePosition = source.getParent().getLocation();
        sourcePosition.translate(positionButtonRelative.x, positionButtonRelative.y);
        sourcePosition.translate(source.getWidth() / 2, source.getHeight());

        return sourcePosition;
    }

    /**
     * Calculates the end position for the line between to nodes
     *
     * @param dest The node where the line should end.
     * @return The end position for the connection line.
     */
    private Point calcNodePositionAsDestination(JButton dest) {
        Point destPosition = calcNodePositionAsSource(dest);
        destPosition.translate(0, -dest.getHeight());

        return destPosition;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Draw all the connection lines in the tree structure.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        paintConnections(g, new JButton[]{NODE_LAYER.ROOT.singleNode}, NODE_LAYER.LAYER_1.nodeLayerArray);
        paintConnections(g, NODE_LAYER.LAYER_1.nodeLayerArray, NODE_LAYER.LAYER_2.nodeLayerArray);
        paintConnections(g, NODE_LAYER.LAYER_2.nodeLayerArray, NODE_LAYER.LAYER_3.nodeLayerArray);
    }

    /**
     * {@inheritDoc}
     * <p>
     * For every layer in the Quad-Tree it transmits the active state of the elements to the equivalent nodes.
     */
    @Override
    protected void updateAppearance() {
        Direction[] tmpDirection = new Direction[3];

        NODE_LAYER.ROOT.singleNode.setBackground(getColorDependingOnQuadState(null));

        for (int i = 0; i < NUMBER_OF_DIRECTIONS; i++) {
            tmpDirection[0] = POSSIBLE_DIRECTIONS[i];
            NODE_LAYER.LAYER_1.nodeLayerArray[i].setBackground(getColorDependingOnQuadState(Arrays.copyOfRange(tmpDirection, 0, 1)));

            for (int j = 0; j < NUMBER_OF_DIRECTIONS; j++) {
                tmpDirection[1] = POSSIBLE_DIRECTIONS[j];
                NODE_LAYER.LAYER_2.nodeLayerArray[i * 4 + j].setBackground(getColorDependingOnQuadState(Arrays.copyOfRange(tmpDirection, 0, 2)));

                for (int k = 0; k < NUMBER_OF_DIRECTIONS; k++) {
                    tmpDirection[2] = POSSIBLE_DIRECTIONS[k];
                    NODE_LAYER.LAYER_3.nodeLayerArray[i * 16 + j * 4 + k].setBackground(getColorDependingOnQuadState(tmpDirection));
                }
            }
        }

        repaint();
    }

    /**
     * {@inheritDoc}
     * <p>
     * The quad will be updated based on the third layer of the tree structure, which is the lowest layer in the Quad-Tree.
     * At the end the whole Quad-Tree can be updated depending on the lowest layer.
     * Also the appearance of this Quad-Tree format will be updated based on the updated Quad-Tree,
     * because it's easier to change the whole appearance based on the new quad tree,
     * instead of do this hardcoded in {@link #processAppearanceChange(ActionEvent)},
     */
    @Override
    protected void updateQuad() {
        Direction[] tmpDirection = new Direction[3];

        for (int i = 0; i < NUMBER_OF_DIRECTIONS; i++) {
            tmpDirection[0] = POSSIBLE_DIRECTIONS[i];
            for (int j = 0; j < NUMBER_OF_DIRECTIONS; j++) {
                tmpDirection[1] = POSSIBLE_DIRECTIONS[j];
                for (int k = 0; k < NUMBER_OF_DIRECTIONS; k++) {
                    tmpDirection[2] = POSSIBLE_DIRECTIONS[k];
                    changeQuadStateDependingOnColor(NODE_LAYER.LAYER_3.nodeLayerArray[i * 16 + j * 4 + k].getBackground(), tmpDirection);
                }
            }
        }

        quad.updateQuadActiveState();
        updateAppearance();
    }

    /**
     * {@inheritDoc}
     * <p>
     * If the user clicked on a node their state switches from active to inactive or vice versa.
     * Later the Quad-Tree will be updated based on the third nodes layer.
     * So if there are changes in the upper layer it's required to adjust the third layer and transmit the changes downwards.
     * Changes in the third layer must not be transmit upwards, because the upper layer aren't needed to update the Quad-Tree
     * and their appearance can be easily changed with the updated Quad-Tree.
     * To guarantee that the tree structure remains correct after the user had activated or deactivated a node in the upper layers or the root node,
     * also the child nodes of this activated or deactivated node needs to be set accordingly.
     */
    @Override
    protected void processAppearanceChange(ActionEvent e) {
        JButton actionNode = (JButton) e.getSource();
        NODE_STATE newNodeState;

        if (actionNode.getBackground() == normalColor) {
            actionNode.setBackground(highlightColor);
            newNodeState = NODE_STATE.ACTIVE;
        } else {
            actionNode.setBackground(normalColor);
            newNodeState = NODE_STATE.INACTIVE;
        }

        if (actionNode == NODE_LAYER.ROOT.singleNode) {
            setNodesState(NODE_LAYER.LAYER_1, 0, 3, newNodeState);
            setNodesState(NODE_LAYER.LAYER_2, 0, 15, newNodeState);
            setNodesState(NODE_LAYER.LAYER_3, 0, 63, newNodeState);
            repaint();
            return;
        }

        int i = 0;
        for (JButton nodeLayer1 : NODE_LAYER.LAYER_1.nodeLayerArray) {
            if (nodeLayer1 == actionNode) {
                setNodesState(NODE_LAYER.LAYER_2, i * 4, (i + 1) * 4 - 1, newNodeState);
                setNodesState(NODE_LAYER.LAYER_3, i * 16, (i + 1) * 16 - 1, newNodeState);
                repaint();
                return;
            }
            i++;
        }

        int j = 0;
        for (JButton nodeLayer2 : NODE_LAYER.LAYER_2.nodeLayerArray) {
            if (nodeLayer2 == actionNode) {
                setNodesState(NODE_LAYER.LAYER_3, j * 4, (j + 1) * 4 - 1, newNodeState);
                repaint();
                return;
            }
            j++;
        }

        // If there are state changes in Layer 1, 2 or 3, their parent layer 2 and 1 and root must getting corrected.
        // It's easier to do this with the function 'updateAppearance', after the quad was updated depending on layer 3
    }

    /**
     * The function changes the color of the nodes corresponding to the given state.
     * Only the nodes in the given Index range will be changed.
     *
     * @param nodeLayer The root or one of the three layers which contains the nodes to be changed.
     * @param fromIdx   The first node which should set to the given state
     * @param toIdx     The last node which should set to the given state
     * @param nodeState The state on which the nodes should set
     */
    private void setNodesState(NODE_LAYER nodeLayer, int fromIdx, int toIdx, NODE_STATE nodeState) {
        for (int i = fromIdx; i <= toIdx; i++) {
            if (nodeState == NODE_STATE.ACTIVE) {
                nodeLayer.nodeLayerArray[i].setBackground(highlightColor);
            } else {
                nodeLayer.nodeLayerArray[i].setBackground(normalColor);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String nameTreeFormat() {
        return "VisTree";
    }

    /**
     * Can be used to describe if a node is active/used or inactive/unused
     */
    private enum NODE_STATE {
        ACTIVE, INACTIVE
    }
}
