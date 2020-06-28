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
        // layer 1 depends on layer 2 depends on layer 3
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
