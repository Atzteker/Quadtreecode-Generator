import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EventManager implements ActionListener {
    private JButton[][] chessButtons = new JButton[8][8];
    private JButton[] zahlencode1LayerButtons = new JButton[4];
    private JButton[] zahlencode2LayerButtons = new JButton[16];
    private JButton[] zahlencode3LayerButtons = new JButton[64];

    public EventManager(JButton[][] chessButtons, JButton[] zahlencode1LayerButtons, JButton[] zahlencode2LayerButtons, JButton[] zahlencode3LayerButtons) {
        this.chessButtons = chessButtons;
        this.zahlencode1LayerButtons = zahlencode1LayerButtons;
        this.zahlencode2LayerButtons = zahlencode2LayerButtons;
        this.zahlencode3LayerButtons = zahlencode3LayerButtons;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        /*if (e.getSource() == chessButtons[0][0]){
            chessButtons[0][0].setBackground(Color.BLACK);
        }*/

        if (((JButton) e.getSource()).getBackground() == Color.WHITE) {
            ((JButton) e.getSource()).setBackground(Color.BLACK);
        } else {
            ((JButton) e.getSource()).setBackground(Color.WHITE);
        }
    }
}
