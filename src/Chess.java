import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Chess extends QuadTreeFormatPanel {
    private JButton[][] chessButtons = new JButton[8][8];

    public Chess(Dimension chessFieldDimension, Color normalColor, Color highlightColor) {
        super(normalColor, highlightColor);
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                chessButtons[i][j] = new JButton();
                chessButtons[i][j].setBackground(normalColor);
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
        matchQuadAndChess(MATCHING_TYPE.QUAD_IS_BASE);
    }

    @Override
    protected void updateQuad() {
        matchQuadAndChess(MATCHING_TYPE.CHESS_IS_BASE);
    }

    @Override
    protected void processAppearanceChange(ActionEvent e) {
        JButton tmpButton = (JButton) e.getSource();
        if (tmpButton.getBackground() == normalColor) {
            tmpButton.setBackground(highlightColor);
        } else {
            tmpButton.setBackground(normalColor);
        }
    }

    @Override
    protected String nameTreeFormat() {
        return "Chess";
    }

    private void matchQuadAndChess(MATCHING_TYPE matching_type) {
        Direction[] possibleDirections = new Direction[]{Direction.NW, Direction.NE, Direction.SE, Direction.SW};
        Direction[] tmpDirection = new Direction[3];
        BoundariesChessArea boundariesChessArea;
        int tmpX, tmpY;

        for (int i = 0; i < 4; i++) {
            tmpDirection[0] = possibleDirections[i];
            for (int j = 0; j < 4; j++) {
                tmpDirection[1] = possibleDirections[j];
                for (int k = 0; k < 4; k++) {
                    tmpDirection[2] = possibleDirections[k];

                    boundariesChessArea = new BoundariesChessArea();
                    boundariesChessArea.updateBoundaries(tmpDirection);
                    tmpX = boundariesChessArea.upperX;
                    tmpY = boundariesChessArea.upperY;

                    if (matching_type == MATCHING_TYPE.CHESS_IS_BASE) {
                        if (chessButtons[tmpY][tmpX].getBackground() == highlightColor) {
                            quad.setActive(tmpDirection);
                        } else {
                            quad.setInactive(tmpDirection);
                        }
                    } else if (matching_type == MATCHING_TYPE.QUAD_IS_BASE) {
                        if (quad.isActive(tmpDirection)) {
                            chessButtons[tmpY][tmpX].setBackground(highlightColor);
                        } else {
                            chessButtons[tmpY][tmpX].setBackground(normalColor);
                        }
                    }
                }
            }
        }

        if (matching_type == MATCHING_TYPE.CHESS_IS_BASE) {
            quad.updateQuadActiveState();
        }
    }

    private enum MATCHING_TYPE {
        QUAD_IS_BASE, CHESS_IS_BASE
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
