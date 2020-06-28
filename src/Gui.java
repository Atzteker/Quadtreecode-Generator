import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Gui {
    private static JButton[][] chessButtons = new JButton[8][8];
    private static JButton[] zahlencode1LayerButtons = new JButton[4];
    private static JButton[] zahlencode2LayerButtons = new JButton[16];
    private static JButton[] zahlencode3LayerButtons = new JButton[64];

    private static EventManager eventManager = new EventManager(chessButtons, zahlencode1LayerButtons, zahlencode2LayerButtons, zahlencode3LayerButtons);

    private static JPanel initializeChessComponents() {
        JPanel chessPanel = new JPanel(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;

        Dimension chessFieldDimension = new Dimension(80,80);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                chessButtons[i][j] = new JButton();
                chessButtons[i][j].setBackground(Color.WHITE);
                chessButtons[i][j].addActionListener(eventManager);
                chessButtons[i][j].setPreferredSize(chessFieldDimension);

                c.gridy = i;
                c.gridx = j;
                chessPanel.add(chessButtons[i][j], c);
            }
        }

        return chessPanel;
    }

    private static JPanel initializeZahlenfolgeComponents() {
        JPanel zahlenfolgePanel = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
        zahlenfolgePanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        int[] quadrants = {1,2,3,4};

        Dimension numberDimension = new Dimension(13,30);
        for (int i = 0; i < 4; i++) {
            zahlencode1LayerButtons[i] = new JButton(String.valueOf(i + 1));
            zahlencode1LayerButtons[i].setPreferredSize(numberDimension);
            zahlencode1LayerButtons[i].setContentAreaFilled(false);
            zahlencode1LayerButtons[i].setBorderPainted(false);
            zahlencode1LayerButtons[i].setMargin(new Insets(0,0,0,0));
            zahlenfolgePanel.add(zahlencode1LayerButtons[i]);
        }

        zahlenfolgePanel.add(new JLabel("|"));

        for (int i = 0; i < 16; i++) {
            zahlencode2LayerButtons[i] = new JButton(String.valueOf(quadrants[i % 4]));
            zahlencode2LayerButtons[i].setPreferredSize(numberDimension);
            zahlencode2LayerButtons[i].setContentAreaFilled(false);
            zahlencode2LayerButtons[i].setBorderPainted(false);
            zahlencode2LayerButtons[i].setMargin(new Insets(0,0,0,0));
            zahlenfolgePanel.add(zahlencode2LayerButtons[i]);
            if ((i+1) % 4 == 0){
                if (i+1 != 16){
                    zahlenfolgePanel.add(new JLabel("-"));
                }
            }
        }

        zahlenfolgePanel.add(new JLabel("|"));

        for (int i = 0; i < 64; i++) {
            zahlencode3LayerButtons[i] = new JButton(String.valueOf(quadrants[i % 4]));
            zahlencode3LayerButtons[i].setPreferredSize(numberDimension);
            zahlencode3LayerButtons[i].setContentAreaFilled(false);
            zahlencode3LayerButtons[i].setBorderPainted(false);
            zahlencode3LayerButtons[i].setMargin(new Insets(0,0,0,0));
            zahlenfolgePanel.add(zahlencode3LayerButtons[i]);
            if ((i+1) % 4 == 0){
                if (i+1 != 64){
                    zahlenfolgePanel.add(new JLabel("-"));
                }
            }
        }

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
        c.gridheight = 1;
        c.gridx = 1;
        c.gridy = 0;
        mainPanel.add(initializeZahlenfolgeComponents(), c);
    }

    public static void main(String[] args) {
        JFrame mainFrame = new JFrame("Quadtreecode-Generator ");
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        initializeComponents(mainFrame);
        mainFrame.pack();
        mainFrame.setVisible(true);
    }
}
