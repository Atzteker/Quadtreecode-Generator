import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class Chess extends QuadTreeFormatPanel {
    private JButton[][] chessButtons = new JButton[8][8];

    public Chess(Dimension chessFieldDimension) {
        this.setLayout(new GridBagLayout());
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
                this.add(chessButtons[i][j], c);
            }
        }
    }

    @Override
    protected void updateAppearance() {
    }

    @Override
    protected void updateQuad() {
    }

    @Override
    protected void processAppearanceChange(ActionEvent e) {
        JButton tmpButton = (JButton)e.getSource();
        if (tmpButton.getBackground() == Color.WHITE){
            tmpButton.setBackground(Color.BLACK);
        } else {
            tmpButton.setBackground(Color.WHITE);
        }
    }

    @Override
    protected String nameTreeFormat() {
        return "Chess";
    }
}
