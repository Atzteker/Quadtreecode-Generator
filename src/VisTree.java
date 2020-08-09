import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;

public class VisTree extends QuadTreeFormatPanel {
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

    private Color normalColorLine;
    private Color highlightColorLine;

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

    private void initNode(JButton nodeButton, JPanel nodePanel, Dimension nodeDimension) {
        nodeButton.addActionListener(this);
        nodeButton.setBackground(normalColor);
        nodeButton.setPreferredSize(nodeDimension);
        nodePanel.add(nodeButton);
    }

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

    private Point calcNodePositionAsSource(JButton source) {
        Point positionButtonRelative = source.getLocation();
        Point sourcePosition = source.getParent().getLocation();
        sourcePosition.translate(positionButtonRelative.x, positionButtonRelative.y);
        sourcePosition.translate(source.getWidth() / 2, source.getHeight());

        return sourcePosition;
    }

    private Point calcNodePositionAsDestination(JButton dest) {
        Point destPosition = calcNodePositionAsSource(dest);
        destPosition.translate(0, -dest.getHeight());

        return destPosition;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        paintConnections(g, new JButton[]{NODE_LAYER.ROOT.singleNode}, NODE_LAYER.LAYER_1.nodeLayerArray);
        paintConnections(g, NODE_LAYER.LAYER_1.nodeLayerArray, NODE_LAYER.LAYER_2.nodeLayerArray);
        paintConnections(g, NODE_LAYER.LAYER_2.nodeLayerArray, NODE_LAYER.LAYER_3.nodeLayerArray);
    }

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

    private void setNodesState(NODE_LAYER nodeLayer, int fromIdx, int toIdx, NODE_STATE nodeState) {
        for (int i = fromIdx; i <= toIdx; i++) {
            if (nodeState == NODE_STATE.ACTIVE) {
                nodeLayer.nodeLayerArray[i].setBackground(highlightColor);
            } else {
                nodeLayer.nodeLayerArray[i].setBackground(normalColor);
            }
        }
    }

    @Override
    protected String nameTreeFormat() {
        return "VisTree";
    }

    private enum NODE_STATE {
        ACTIVE, INACTIVE
    }
}
