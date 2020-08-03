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

    public VisTree(Dimension nodeDimension, int smallestGap, int verticalGap) {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        int secondLayerGap = (smallestGap + nodeDimension.width) * 4 - nodeDimension.width;
        int firstLayerGap = (secondLayerGap + nodeDimension.width) * 4 - nodeDimension.width;

        JPanel rootLayer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, verticalGap));
        JPanel firstLayer = new JPanel(new FlowLayout(FlowLayout.CENTER, firstLayerGap, verticalGap));
        JPanel secondLayer = new JPanel(new FlowLayout(FlowLayout.CENTER, secondLayerGap, verticalGap));
        JPanel thirdLayer = new JPanel(new FlowLayout(FlowLayout.CENTER, smallestGap, verticalGap));

        rootLayer.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        firstLayer.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        secondLayer.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        thirdLayer.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        initNodes(NODE_LAYER.ROOT, rootLayer, nodeDimension);
        initNodes(NODE_LAYER.LAYER_1, firstLayer, nodeDimension);
        initNodes(NODE_LAYER.LAYER_2, secondLayer, nodeDimension);
        initNodes(NODE_LAYER.LAYER_3, thirdLayer, nodeDimension);

        this.add(rootLayer);
        this.add(firstLayer);
        this.add(secondLayer);
        this.add(thirdLayer);
    }

    private void initNodes(NODE_LAYER node_layer, JPanel nodePanel, Dimension nodeDimension) {
        JButton[] nodeButtons;
        switch (node_layer) {
            case ROOT:
                initNode(root, nodePanel, nodeDimension);
                return;
            case LAYER_1:
                nodeButtons = nodes1LayerButtons;
                break;
            case LAYER_2:
                nodeButtons = nodes2LayerButtons;
                break;
            case LAYER_3:
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
        nodeButton = new JButton();
        nodeButton.addActionListener(this);
        nodeButton.setBackground(normalColor);
        nodeButton.setPreferredSize(nodeDimension);
        nodePanel.add(nodeButton);
    }

    @Override
    protected void updateAppearance() {

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
