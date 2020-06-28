import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Gui implements ActionListener {
    private static JButton[][] chessButtons = new JButton[8][8];
    private static EventManager eventManager = new EventManager(chessButtons);

    private static JPanel initializeChessComponents() {
        JPanel chessPanel = new JPanel(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                chessButtons[i][j] = new JButton();
                chessButtons[i][j].setBackground(Color.WHITE);
                chessButtons[i][j].addActionListener(eventManager);
                c.gridy = i;
                c.gridx = j;
                c.gridwidth = 1;
                chessPanel.add(chessButtons[i][j], c);
            }
        }

        return chessPanel;
    }

    private static JPanel initializeZahlenfolgeComponents() {
        JPanel zahlenfolgePanel = new JPanel(new FlowLayout());


        return zahlenfolgePanel;
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
        c.gridx = 0;
        c.gridy = 1;
        mainPanel.add(initializeZahlenfolgeComponents(), c);
    }

    public static void main(String[] args) {
        JFrame mainFrame = new JFrame("Quadtreecode-Generator ");
        mainFrame.setSize(800, 1000);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        initializeComponents(mainFrame);
        mainFrame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
