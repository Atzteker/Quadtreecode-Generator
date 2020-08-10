import javax.swing.*;
import java.awt.*;

/**
 * This class organizes the main components of the application.
 */
public class Gui {
    private static Chess chess = new Chess(new Dimension(70, 70), Color.WHITE, Color.BLACK);
    private static Numbers numbers = new Numbers(new Dimension(13, 20), new Color(100, 100, 100, 50), Color.BLACK);
    private static VisTree visTree = new VisTree(new Dimension(13, 13), 4, 100,new Color(100, 100, 100, 10),Color.BLACK,new Color(0, 44, 138, 10), new Color(0, 44, 138));

    /**
     * Connects the main components of the Gui with listeners.
     */
    private static void initializeListener() {
        numbers.addQuadListener(chess.getQuadListener(), numbers.getTreeFormat());
        numbers.addQuadListener(visTree.getQuadListener(), numbers.getTreeFormat());
        chess.addQuadListener(numbers.getQuadListener(), chess.getTreeFormat());
        chess.addQuadListener(visTree.getQuadListener(), chess.getTreeFormat());
        visTree.addQuadListener(numbers.getQuadListener(), visTree.getTreeFormat());
        visTree.addQuadListener(chess.getQuadListener(), visTree.getTreeFormat());
    }

    /**
     * Specifies the arrangement of the different main components.
     * @param frame The frame on which the different main components will be arranged
     */
    private static void initializeComponents(JFrame frame) {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;

        c.gridwidth = 1;
        c.gridheight = 4;
        c.gridx = 0;
        c.gridy = 0;
        mainPanel.add(chess, c);

        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 1;
        c.gridy = 0;
        mainPanel.add(numbers, c);

        c.gridwidth = 1;
        c.gridheight = 3;
        c.gridx = 1;
        c.gridy = 1;
        mainPanel.add(visTree, c);

        frame.setContentPane(mainPanel);
    }

    public static void main(String[] args) {
        JFrame mainFrame = new JFrame("Quadtree-Konverter");
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        initializeListener();
        initializeComponents(mainFrame);
        mainFrame.pack();
        mainFrame.setVisible(true);
    }
}
