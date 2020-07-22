import javax.swing.*;
import java.awt.*;

public class Gui {
    private static Chess chess = new Chess(new Dimension(80, 80));
    private static Numbers numbers = new Numbers();
    private static Quad quad;

    private static JPanel initializeChessComponents() {
        return chess.getChessPanel();
    }

    private static JPanel initializeNumbersComponents() {
        return numbers.getNumbersPanel();
    }

    private static void initializeQuadTree() {
        Quad firstLayer[] = new Quad[4];
        Quad secondLayer[] = new Quad[4];
        Quad thirdLayer;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                secondLayer[j] = new Quad(new Quad(false), new Quad(false), new Quad(false), new Quad(false));
            }
            firstLayer[i] = new Quad(secondLayer[0], secondLayer[0], secondLayer[0], secondLayer[0]);
        }

        quad = new Quad(firstLayer[0], firstLayer[1], firstLayer[2], firstLayer[3]);
    }

    private static void initializeComponents(JFrame frame) {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        frame.setContentPane(mainPanel);
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;

        c.gridwidth = 1;
        c.gridheight = 2;
        c.gridx = 0;
        c.gridy = 0;
        mainPanel.add(initializeChessComponents(), c);

        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 1;
        c.gridy = 0;
        mainPanel.add(initializeNumbersComponents(), c);
    }

    public static void main(String[] args) {
        JFrame mainFrame = new JFrame("Quadtreecode-Generator ");
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        initializeQuadTree();
        initializeComponents(mainFrame);
        mainFrame.pack();
        mainFrame.setVisible(true);
    }
}
