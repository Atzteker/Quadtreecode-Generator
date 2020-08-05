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
        System.out.println();
        repaint();
    }

    @Override
    protected void updateQuad() {

    }

    @Override
    protected void processAppearanceChange(ActionEvent e) {

    }

    @Override
    protected String nameTreeFormat() {
        return "VisTree";
    }

    private enum NODE_LAYER {
        ROOT, LAYER_1, LAYER_2, LAYER_3
    }
}
