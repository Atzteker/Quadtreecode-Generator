import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Chess implements ActionListener {
    private JButton[][] chessButtons = new JButton[8][8];
    private JPanel chessPanel = new JPanel(new GridBagLayout());

    public Chess(Dimension chessFieldDimension){
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                chessButtons[i][j] = new JButton();
                chessButtons[i][j].setBackground(Color.WHITE);
                chessButtons[i][j].addActionListener(this);
                chessButtons[i][j].setPreferredSize(chessFieldDimension);

                c.gridy = i;
                c.gridx = j;
                chessPanel.add(chessButtons[i][j], c);
            }
        }
    }

    public JPanel getChessPanel() {
        return chessPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
}
