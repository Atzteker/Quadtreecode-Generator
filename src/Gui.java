import javax.swing.*;
import java.awt.*;

public class Gui {
    //private static Synchronizer sync = new Synchronizer();
    private static Chess chess = new Chess(new Dimension(80, 80));
    private static Numbers numbers = new Numbers();

    //private static Quad quad;

    private static void initializeListener() {
        numbers.addQuadListener(chess.getQuadListener(), numbers.getTreeFormat());
        chess.addQuadListener(numbers.getQuadListener(), chess.getTreeFormat());
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
        mainPanel.add(chess, c);

        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 1;
        c.gridy = 0;
        mainPanel.add(numbers, c);
    }

    public static void main(String[] args) {
        JFrame mainFrame = new JFrame("Quadtreecode-Generator");
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        initializeListener();
        initializeComponents(mainFrame);
        mainFrame.pack();
        mainFrame.setVisible(true);
    }
}
