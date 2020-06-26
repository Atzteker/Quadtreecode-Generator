import javax.swing.*;
import java.awt.*;

public class Gui {
    private static JButton[][] chessButtons = new JButton[8][8];

    private static void initializeComponents(JFrame frame) {
        JPanel pane = new JPanel(new GridBagLayout());
        frame.setContentPane(pane);

        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                chessButtons[i][j] = new JButton();
                chessButtons[i][j].setBackground(Color.WHITE);
                c.gridy = i;
                c.gridx = j;
                c.gridwidth = 1;
                pane.add(chessButtons[i][j], c);
            }
        }
    }

    public static void main(String[] args) {
        JFrame mainFrame = new JFrame("Quadtreecode-Generator ");
        mainFrame.setSize(800, 1000);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        initializeComponents(mainFrame);
        mainFrame.setVisible(true);
    }
}
