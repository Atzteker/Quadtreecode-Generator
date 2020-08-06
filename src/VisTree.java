import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class VisTree extends QuadTreeFormatPanel {
    private JButton root = new JButton();
    private JButton[] nodes1LayerButtons = new JButton[4];
    private JButton[] nodes2LayerButtons = new JButton[16];
    private JButton[] nodes3LayerButtons = new JButton[64];

    private static Color normalColor = new Color(100, 100, 100, 10);
    private static Color highlightColor = Color.BLACK;
    private static Color normalColorLine = new Color(0, 44, 138, 10);
    private static Color highlightColorLine = new Color(0, 44, 138);

    public VisTree(Dimension nodeDimension, int smallestGap, int verticalGap) {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        int secondLayerGap = (smallestGap + nodeDimension.width) * 4 - nodeDimension.width;
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

    private void initNodes(NODE_LAYER node_layer, JPanel nodePanel, Dimension nodeDimension) {
        JButton[] nodeButtons;
        switch (node_layer) {
            case ROOT:
                root = new JButton();
                initNode(root, nodePanel, nodeDimension);
                return;
            case LAYER_1:
                for (int i = 0; i < nodes1LayerButtons.length; i++) {
                    nodes1LayerButtons[i] = new JButton();
                }
                nodeButtons = nodes1LayerButtons;
                break;
            case LAYER_2:
                for (int i = 0; i < nodes2LayerButtons.length; i++) {
                    nodes2LayerButtons[i] = new JButton();
                }
                nodeButtons = nodes2LayerButtons;
                break;
            case LAYER_3:
                for (int i = 0; i < nodes3LayerButtons.length; i++) {
                    nodes3LayerButtons[i] = new JButton();
                }
                nodeButtons = nodes3LayerButtons;
                break;
            default:
                return;
        }

        for (JButton nodeButton : nodeButtons) {
            initNode(nodeButton, nodePanel, nodeDimension);
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
                tmpDstPosition = calcNodePositionAsDestination(destination[i * 4 + j]);
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

        paintConnections(g, new JButton[]{root}, nodes1LayerButtons);
        paintConnections(g, nodes1LayerButtons, nodes2LayerButtons);
        paintConnections(g, nodes2LayerButtons, nodes3LayerButtons);
    }

    @Override
    protected void updateAppearance() {
        Direction[] possibleDirections = new Direction[]{Direction.NW, Direction.NE, Direction.SE, Direction.SW};
        Direction[] tmpDirectionLayer1 = new Direction[1];
        Direction[] tmpDirectionLayer2 = new Direction[2];
        Direction[] tmpDirectionLayer3 = new Direction[3];

        if (quad.isActive()) {
            root.setBackground(highlightColor);
        } else {
            root.setBackground(normalColor);
        }

        for (int i = 0; i < 4; i++) {
            tmpDirectionLayer1[0] = possibleDirections[i];
            tmpDirectionLayer2[0] = possibleDirections[i];
            tmpDirectionLayer3[0] = possibleDirections[i];
            matchButtonStateAndQuadState(nodes1LayerButtons[i], tmpDirectionLayer1);

            for (int j = 0; j < 4; j++) {
                tmpDirectionLayer2[1] = possibleDirections[j];
                tmpDirectionLayer3[1] = possibleDirections[j];
                matchButtonStateAndQuadState(nodes2LayerButtons[i * 4 + j], tmpDirectionLayer2);

                for (int k = 0; k < 4; k++) {
                    tmpDirectionLayer3[2] = possibleDirections[k];
                    matchButtonStateAndQuadState(nodes3LayerButtons[i * 16 + j * 4 + k], tmpDirectionLayer3);
                }
            }
        }

        repaint();
    }

    private void matchButtonStateAndQuadState(JButton node, Direction[] quadPath) {
        if (quad.isActive(quadPath)) {
            node.setBackground(highlightColor);
        } else {
            node.setBackground(normalColor);
        }
    }

    @Override
    protected void updateQuad() {
        Direction[] possibleDirections = new Direction[]{Direction.NW, Direction.NE, Direction.SE, Direction.SW};
        Direction[] tmpDirection = new Direction[3];

        for (int i = 0; i < 4; i++) {
            tmpDirection[0] = possibleDirections[i];
            for (int j = 0; j < 4; j++) {
                tmpDirection[1] = possibleDirections[j];
                for (int k = 0; k < 4; k++) {
                    tmpDirection[2] = possibleDirections[k];
                    if (nodes3LayerButtons[i * 16 + j * 4 + k].getBackground() == highlightColor) {
                        quad.setActive(tmpDirection);
                    } else {
                        quad.setInactive(tmpDirection);
                    }
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

        if (actionNode == root) {
            setNodesState(nodes1LayerButtons, 0, 3, newNodeState);
            setNodesState(nodes2LayerButtons, 0, 15, newNodeState);
            setNodesState(nodes3LayerButtons, 0, 63, newNodeState);
            repaint();
            return;
        }

        int i = 0;
        for (JButton nodeLayer1 : nodes1LayerButtons) {
            if (nodeLayer1 == actionNode) {
                setNodesState(nodes2LayerButtons, i * 4, (i + 1) * 4 - 1, newNodeState);
                setNodesState(nodes3LayerButtons, i * 16, (i + 1) * 16 - 1, newNodeState);
                repaint();
                return;
            }
            i++;
        }

        int j = 0;
        for (JButton nodeLayer2 : nodes2LayerButtons) {
            if (nodeLayer2 == actionNode) {
                setNodesState(nodes3LayerButtons, j * 4, (j + 1) * 4 - 1, newNodeState);
                repaint();
                return;
            }
            j++;
        }


        // If there are state changes in Layer 1, 2 or 3, their parent layer 2 and 1 and root must getting corrected.
        // It's easier to do this with the function 'updateAppearance', after the quad was updated depending on layer 3
    }

    private void setNodesState(JButton[] nodeLayer, int fromIdx, int toIdx, NODE_STATE node_state) {
        for (int i = fromIdx; i <= toIdx; i++) {
            if (node_state == NODE_STATE.ACTIVE) {
                nodeLayer[i].setBackground(highlightColor);
            } else {
                nodeLayer[i].setBackground(normalColor);
            }

        }
    }

    @Override
    protected String nameTreeFormat() {
        return "VisTree";
    }

    private enum NODE_LAYER {
        ROOT, LAYER_1, LAYER_2, LAYER_3
    }

    private enum NODE_STATE {
        ACTIVE, INACTIVE
    }
}
