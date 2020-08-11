import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Represents the Quad-Tree as a Chessboard
 */
public class Chess extends QuadTreeFormatPanel {
    /**
     * The visual components(buttons) are the 64 fields on the Chessboard.
     */
    private JButton[][] chessButtons = new JButton[8][8];

    /**
     * Creates a new JPanel with a Chessboard
     * were the 64 chess fields represents elements in the lowest layer of a 'root + 3 layer Quad-Tree'.
     *
     * @param chessFieldDimension The dimension of one single chess field
     * @param normalColor         The color if the chess field isn't used, respectively is empty
     * @param highlightColor      The color if the chess field is used, respectively isn't empty
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    protected void updateAppearance() {
        matchQuadAndChess(MATCHING_TYPE.QUAD_IS_BASE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void updateQuad() {
        matchQuadAndChess(MATCHING_TYPE.CHESS_IS_BASE);
    }

    /**
     * {@inheritDoc}
     * <p>
     * It simply switches the color/state of the clicked chess field.
     * So if the chess field was marked as used now mark it as unused and vice versa.
     */
    @Override
    protected void processAppearanceChange(ActionEvent e) {
        JButton tmpButton = (JButton) e.getSource();
        if (tmpButton.getBackground() == normalColor) {
            tmpButton.setBackground(highlightColor);
        } else {
            tmpButton.setBackground(normalColor);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String nameTreeFormat() {
        return "Chess";
    }

    /**
     * It iterates over all Quad-Tree elements in the lowest layer and calculates for each element which chess field is corresponding to it.
     * If the current appearance should be adjust to the current Quad-Tree, the chess field is marked depending on the active state of the Quad-Tree element.
     * If the current Quad-Tree should be adjust to the current appearance, the active state of the Quad-Tree element is set depending on the chess field color(used or unused)
     * and at the end the higher layers from the Quad-Tree will be updated.
     *
     * @param matching_type Defines if the appearance should used to set the Quad-Tree or vice versa.
     */
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
                        chessButtons[tmpY][tmpX].setBackground(getColorDependingOnQuadState(tmpDirection));
                    }
                }
            }
        }

        if (matching_type == MATCHING_TYPE.CHESS_IS_BASE) {
            quad.updateQuadActiveState();
        }
    }

    /**
     * Can be used to define if the appearance should be set like the Quad-Tree or vice versa.
     */
    private enum MATCHING_TYPE {
        /**
         * Set the appearance like the current Quad-Tree
         */
        QUAD_IS_BASE,
        /**
         * Set the Quad-Tree like the current appearance
         */
        CHESS_IS_BASE
    }

    /**
     * Calculates the new boundaries of a square on the chessboard, if you define the chessboard as a Quad-Tree and go down one layer in a specific direction.
     * For example if you are at the root of the 'Chessboard-Quad-Tree' the square goes from A8(array notation 00) to H1(array notation 77).
     * Than you go to North-West. The new boundaries of the square would be A8(array notation 00) to D5(array notation 33).
     *
     * @param direction         The direction in which the boundaries should be scaled down
     * @param currentBoundaries The boundaries of the current Quad-Tree square
     * @return The boundaries of the square in the next lower layer for the given direction.
     */
    private static BoundariesChessArea updateBoundaries(Direction direction, BoundariesChessArea currentBoundaries) {
        int containment = (currentBoundaries.lowerX - currentBoundaries.upperX + 1) / 2;

        switch (direction) {
            case NW:
                return new BoundariesChessArea(currentBoundaries.lowerX - containment, currentBoundaries.lowerY - containment, currentBoundaries.upperX, currentBoundaries.upperY);
            case NE:
                return new BoundariesChessArea(currentBoundaries.lowerX, currentBoundaries.lowerY - containment, currentBoundaries.upperX + containment, currentBoundaries.upperY);
            case SE:
                return new BoundariesChessArea(currentBoundaries.lowerX, currentBoundaries.lowerY, currentBoundaries.upperX + containment, currentBoundaries.upperY + containment);
            case SW:
                return new BoundariesChessArea(currentBoundaries.lowerX - containment, currentBoundaries.lowerY, currentBoundaries.upperX, currentBoundaries.upperY + containment);
        }

        return new BoundariesChessArea(currentBoundaries);
    }

    /**
     * This class represents the boundaries of a square on the chess board if the chess board is defined like a Quad-Tree.
     *
     * @see #updateBoundaries(Direction, BoundariesChessArea)
     */
    private static class BoundariesChessArea {
        /**
         * The bottom right corner of the square
         */
        int lowerX, lowerY;
        /**
         * The top left corner of the square
         */
        int upperX, upperY;

        /**
         * Defines a new square, were the square is at the root of the Chessboard-Quad-Tree.
         * From A8(00) to H1(77).
         */
        BoundariesChessArea() {
            this.upperX = 0;
            this.upperY = 0;
            this.lowerX = 7;
            this.lowerY = 7;
        }

        /**
         * Copies the Boundaries of a square on the Chessboard.
         *
         * @param that The boundaries which will be copied.
         */
        BoundariesChessArea(BoundariesChessArea that) {
            this.upperX = that.upperX;
            this.upperY = that.upperY;
            this.lowerX = that.lowerX;
            this.lowerY = that.lowerY;
        }

        /**
         * Defines a new square, were the square goes from the given top left corner to the given bottom right corner.
         *
         * @param lowerX X-Position of the bottom right corner.
         * @param lowerY Y-Position of the bottom right corner.
         * @param upperX X-Position of the top left corner.
         * @param upperY Y-Position of the top left corner.
         */
        BoundariesChessArea(int lowerX, int lowerY, int upperX, int upperY) {
            this.upperX = upperX;
            this.upperY = upperY;
            this.lowerX = lowerX;
            this.lowerY = lowerY;
        }
    }
}
