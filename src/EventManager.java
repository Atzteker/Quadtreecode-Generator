import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EventManager implements ActionListener {
    private JButton[][] chessButtons = new JButton[8][8];

    public EventManager(JButton[][] chessButtons){
        this.chessButtons = chessButtons;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        /*if (e.getSource() == chessButtons[0][0]){
            chessButtons[0][0].setBackground(Color.BLACK);
        }*/

        if (((JButton)e.getSource()).getBackground() == Color.WHITE){
            ((JButton)e.getSource()).setBackground(Color.BLACK);
        } else {
            ((JButton)e.getSource()).setBackground(Color.WHITE);
        }
    }
}
