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
        Direction[] possibleDirections = new Direction[]{Direction.NW, Direction.NE, Direction.SE, Direction.SW};
        Direction[] tmpDirection = new Direction[3];
        BoundariesChessArea boundariesChessArea;

        for (int i = 0; i < 4; i++) {
            tmpDirection[0] = possibleDirections[i];
            for (int j = 0; j < 4; j++) {
                tmpDirection[1] = possibleDirections[j];
                for (int k = 0; k < 4; k++) {
                    tmpDirection[2] = possibleDirections[k];
                    boundariesChessArea = new BoundariesChessArea();
                    boundariesChessArea.updateBoundaries(tmpDirection);

                    if (chessButtons[boundariesChessArea.upperY][boundariesChessArea.upperX].getBackground() == Color.BLACK) {
                        quad.setActive(tmpDirection);
                    } else {
                        quad.setInactive(tmpDirection);
                    }
                }
            }
        }

        quad.updateQuadActiveState();
    }

    @Override
    protected void processAppearanceChange(ActionEvent e) {
        JButton tmpButton = (JButton) e.getSource();
        if (tmpButton.getBackground() == Color.WHITE) {
            tmpButton.setBackground(Color.BLACK);
        } else {
            tmpButton.setBackground(Color.WHITE);
        }
    }

    @Override
    protected String nameTreeFormat() {
        return "Chess";
    }

    private static class BoundariesChessArea {
        int lowerX = 0, upperX = 7;
        int lowerY = 0, upperY = 7;

        public void updateBoundaries(Direction direction) {
            int containment = (upperX - lowerX + 1) / 2;

            switch (direction) {
                case NW:
                    this.upperX -= containment;
                    this.upperY -= containment;
                    break;
                case NE:
                    this.lowerX += containment;
                    this.upperY -= containment;
                    break;
                case SE:
                    this.lowerX += containment;
                    this.lowerY += containment;
                    break;
                case SW:
                    this.upperX -= containment;
                    this.lowerY += containment;
                    break;
            }
        }

        public void updateBoundaries(Direction[] directions) {
            for (Direction direction : directions) {
                updateBoundaries(direction);
            }
        }
    }
}
