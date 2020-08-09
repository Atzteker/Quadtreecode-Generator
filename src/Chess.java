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
        Direction[] tmpDirection = new Direction[3];
        BoundariesChessArea root = new BoundariesChessArea();
        BoundariesChessArea tmpFirstLayer;
        BoundariesChessArea tmpSecondLayer;
        BoundariesChessArea tmpThirdLayer;
        int tmpX, tmpY;

        for (int i = 0; i < NUMBER_OF_DIRECTIONS; i++) {
            tmpDirection[0] = POSSIBLE_DIRECTIONS[i];
            tmpFirstLayer = updateBoundaries(POSSIBLE_DIRECTIONS[i], root);

            for (int j = 0; j < NUMBER_OF_DIRECTIONS; j++) {
                tmpDirection[1] = POSSIBLE_DIRECTIONS[j];
                tmpSecondLayer = updateBoundaries(POSSIBLE_DIRECTIONS[j], tmpFirstLayer);

                for (int k = 0; k < NUMBER_OF_DIRECTIONS; k++) {
                    tmpDirection[2] = POSSIBLE_DIRECTIONS[k];
                    tmpThirdLayer = updateBoundaries(POSSIBLE_DIRECTIONS[k], tmpSecondLayer);
                    tmpX = tmpThirdLayer.upperX;
                    tmpY = tmpThirdLayer.upperY;

                    if (matching_type == MATCHING_TYPE.CHESS_IS_BASE) {
                        changeQuadStateDependingOnColor(chessButtons[tmpY][tmpX].getBackground(), tmpDirection);
                    } else if (matching_type == MATCHING_TYPE.QUAD_IS_BASE) {
                        chessButtons[tmpY][tmpX].setBackground(colorDependingOnQuadState(tmpDirection));
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

    private static BoundariesChessArea updateBoundaries(Direction direction, BoundariesChessArea currentBoundaries) {
        int containment = (currentBoundaries.upperX - currentBoundaries.lowerX + 1) / 2;

        switch (direction) {
            case NW:
                return new BoundariesChessArea(currentBoundaries.lowerX, currentBoundaries.lowerY, currentBoundaries.upperX - containment, currentBoundaries.upperY - containment);
            case NE:
                return new BoundariesChessArea(currentBoundaries.lowerX + containment, currentBoundaries.lowerY, currentBoundaries.upperX, currentBoundaries.upperY - containment);
            case SE:
                return new BoundariesChessArea(currentBoundaries.lowerX + containment, currentBoundaries.lowerY + containment, currentBoundaries.upperX, currentBoundaries.upperY);
            case SW:
                return new BoundariesChessArea(currentBoundaries.lowerX, currentBoundaries.lowerY + containment, currentBoundaries.upperX - containment, currentBoundaries.upperY);
        }

        return new BoundariesChessArea(currentBoundaries);
    }

    private static class BoundariesChessArea {
        int lowerX, upperX;
        int lowerY, upperY;

        BoundariesChessArea() {
            this.upperX = 7;
            this.upperY = 7;
            this.lowerX = 0;
            this.lowerY = 0;
        }

        BoundariesChessArea(BoundariesChessArea that) {
            this.upperX = that.upperX;
            this.upperY = that.upperY;
            this.lowerX = that.lowerX;
            this.lowerY = that.lowerY;
        }

        BoundariesChessArea(int lowerX, int lowerY, int upperX, int upperY) {
            this.upperX = upperX;
            this.upperY = upperY;
            this.lowerX = lowerX;
            this.lowerY = lowerY;
        }
    }
}
