import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EventManager implements ActionListener {
    private JButton[][] chessButtons = new JButton[8][8];

    private enum ChessFieldState {ACTIVE, INACTIVE}

    private JButton[] zahlencode1LayerButtons = new JButton[4];
    private JButton[] zahlencode2LayerButtons = new JButton[16];
    private JButton[] zahlencode3LayerButtons = new JButton[64];

    public EventManager(JButton[][] chessButtons, JButton[] zahlencode1LayerButtons, JButton[] zahlencode2LayerButtons, JButton[] zahlencode3LayerButtons) {
        this.chessButtons = chessButtons;
        this.zahlencode1LayerButtons = zahlencode1LayerButtons;
        this.zahlencode2LayerButtons = zahlencode2LayerButtons;
        this.zahlencode3LayerButtons = zahlencode3LayerButtons;
    }

    private void updateZahlencode(Point changedPosition, ChessFieldState chessFieldState) {
        //TODO
        // clean this shit

        // layer 1 depends on layer 2 depends on layer 3
        int x = changedPosition.x;
        int y = changedPosition.y;
        int[] help = {0, 1, 4, 5, 3, 2, 7, 6, 12, 13, 8, 9, 15, 14, 11, 10};
        int[] help2 = {0, 0, 1, 1, 0, 0, 1, 1, 3, 3, 2, 2, 3, 3, 2, 2};
        int index = 70; // poor out of bound exception
        int index2 = 18;
        int index3 = 5;

        // first quadrant
        if (x % 2 == 0 && y % 2 == 0) {
            index = help[((y / 2) * 4 + x / 2)] * 4;
            index2 = help[((y / 2) * 4 + x / 2)];
            index3 = help2[((y / 2) * 4 + x / 2)];

        }
        // second quadrant
        if ((x - 1) % 2 == 0 && y % 2 == 0) {
            index = help[(y / 2) * 4 + (x - 1) / 2] * 4 + 1;
            index2 = help[(y / 2) * 4 + (x - 1) / 2];
            index3 = help2[(y / 2) * 4 + (x - 1) / 2];
        }
        // fourth quadrant
        if (x % 2 == 0 && (y - 1) % 2 == 0) {
            index = help[((y - 1) / 2) * 4 + x / 2] * 4 + 3;
            index2 = help[((y - 1) / 2) * 4 + x / 2];
            index3 = help2[((y - 1) / 2) * 4 + x / 2];
        }
        // third quadrant
        if ((x - 1) % 2 == 0 && (y - 1) % 2 == 0) {
            index = help[((y - 1) / 2) * 4 + (x - 1) / 2] * 4 + 2;
            index2 = help[((y - 1) / 2) * 4 + (x - 1) / 2];
            index3 = help2[((y - 1) / 2) * 4 + (x - 1) / 2];
        }

        if (chessFieldState == ChessFieldState.ACTIVE) {
            zahlencode3LayerButtons[index].setForeground(Color.BLACK);
            zahlencode2LayerButtons[index2].setForeground(Color.BLACK);
            zahlencode1LayerButtons[index3].setForeground(Color.BLACK);
        } else {
            zahlencode3LayerButtons[index].setForeground(Color.GRAY);
            if (zahlencode3LayerButtons[index2 * 4].getForeground() == Color.GRAY && zahlencode3LayerButtons[index2 * 4 + 1].getForeground() == Color.GRAY && zahlencode3LayerButtons[index2 * 4 + 2].getForeground() == Color.GRAY && zahlencode3LayerButtons[index2 * 4 + 3].getForeground() == Color.GRAY) {
                zahlencode2LayerButtons[index2].setForeground(Color.GRAY);
            }
            if (zahlencode2LayerButtons[index3 * 4].getForeground() == Color.GRAY && zahlencode2LayerButtons[index3 * 4 + 1].getForeground() == Color.GRAY && zahlencode2LayerButtons[index3 * 4 + 2].getForeground() == Color.GRAY && zahlencode2LayerButtons[index3 * 4 + 3].getForeground() == Color.GRAY) {
                zahlencode1LayerButtons[index3].setForeground(Color.GRAY);
            }
        }


    }

    private Point getChessButtonPosition(JButton chessButton) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (chessButton == chessButtons[i][j]) {
                    return new Point(j, i);
                }
            }
        }
        return null;
    }

    private ChessFieldState getChessFieldState(JButton chessButton) {
        if (chessButton.getBackground() == Color.BLACK) {
            return ChessFieldState.ACTIVE;
        }
        return ChessFieldState.INACTIVE;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (((JButton) e.getSource()).getBackground() == Color.WHITE) {
            ((JButton) e.getSource()).setBackground(Color.BLACK);
        } else {
            ((JButton) e.getSource()).setBackground(Color.WHITE);
        }

        updateZahlencode(getChessButtonPosition((JButton) e.getSource()), getChessFieldState((JButton) e.getSource()));
    }
}
