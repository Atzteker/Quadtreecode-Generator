import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;

public class Numbers extends QuadTreeFormatPanel {
    private JButton[] numbers1LayerButtons = new JButton[4];
    private JButton[] numbers2LayerButtons = new JButton[16];
    private JButton[] numbers3LayerButtons = new JButton[64];

    private static Color normalColor = new Color(100, 100, 100, 50);
    private static Color highlightColor = Color.BLACK;

    public Numbers() {
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        this.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        Dimension numberDimension = new Dimension(13, 30);
        for (int i = 0; i < 4; i++) {
            numbers1LayerButtons[i] = new JButton(String.valueOf(i + 1));
            numbers1LayerButtons[i].addActionListener(this);
            numbers1LayerButtons[i].setPreferredSize(numberDimension);
            numbers1LayerButtons[i].setContentAreaFilled(false);
            numbers1LayerButtons[i].setBorderPainted(false);
            numbers1LayerButtons[i].setMargin(new Insets(0, 0, 0, 0));
            numbers1LayerButtons[i].setForeground(normalColor);
            this.add(numbers1LayerButtons[i]);
        }

        this.add(new JLabel("|"));

        for (int i = 0; i < 16; i++) {
            numbers2LayerButtons[i] = new JButton(String.valueOf((i % 4) + 1));
            numbers2LayerButtons[i].addActionListener(this);
            numbers2LayerButtons[i].setPreferredSize(numberDimension);
            numbers2LayerButtons[i].setContentAreaFilled(false);
            numbers2LayerButtons[i].setBorderPainted(false);
            numbers2LayerButtons[i].setMargin(new Insets(0, 0, 0, 0));
            numbers2LayerButtons[i].setForeground(normalColor);
            this.add(numbers2LayerButtons[i]);

            if ((i + 1) % 4 == 0) {
                if (i + 1 != 16) {
                    this.add(new JLabel("-"));
                }
            }
        }

        this.add(new JLabel("|"));

        for (int i = 0; i < 64; i++) {
            numbers3LayerButtons[i] = new JButton(String.valueOf((i % 4) + 1));
            numbers3LayerButtons[i].addActionListener(this);
            numbers3LayerButtons[i].setPreferredSize(numberDimension);
            numbers3LayerButtons[i].setContentAreaFilled(false);
            numbers3LayerButtons[i].setBorderPainted(false);
            numbers3LayerButtons[i].setMargin(new Insets(0, 0, 0, 0));
            numbers3LayerButtons[i].setForeground(normalColor);
            this.add(numbers3LayerButtons[i]);

            if ((i + 1) % 4 == 0) {
                if (i + 1 != 64) {
                    this.add(new JLabel("-"));
                }
            }
        }
    }

    @Override
    protected void updateAppearance() {
        Direction[] possibleDirections = new Direction[]{Direction.NW, Direction.NE, Direction.SE, Direction.SW};
        Direction[] tmpDirectionLayer1 = new Direction[1];
        Direction[] tmpDirectionLayer2 = new Direction[2];
        Direction[] tmpDirectionLayer3 = new Direction[3];

        for (int i = 0; i < 4; i++) {
            tmpDirectionLayer1[0] = possibleDirections[i];
            tmpDirectionLayer2[0] = possibleDirections[i];
            tmpDirectionLayer3[0] = possibleDirections[i];
            matchButtonStateAndQuadState(numbers1LayerButtons[i], tmpDirectionLayer1);

            for (int j = 0; j < 4; j++) {
                tmpDirectionLayer2[1] = possibleDirections[j];
                tmpDirectionLayer3[1] = possibleDirections[j];
                matchButtonStateAndQuadState(numbers2LayerButtons[i * 4 + j], tmpDirectionLayer2);

                for (int k = 0; k < 4; k++) {
                    tmpDirectionLayer3[2] = possibleDirections[k];
                    matchButtonStateAndQuadState(numbers3LayerButtons[i * 16 + j * 4 + k], tmpDirectionLayer3);
                }
            }
        }
    }

    private void matchButtonStateAndQuadState(JButton button, Direction[] quadPath) {
        if (quad.isActive(quadPath)) {
            button.setForeground(highlightColor);
        } else {
            button.setForeground(normalColor);
        }
    }

    @Override
    protected void updateQuad() {
        Direction[] possibleDirections = new Direction[]{Direction.NW, Direction.NE, Direction.SE, Direction.SW};
        Direction[] tmpDirection = new Direction[3];

        for (int i = 0; i < 4; i++) {
            tmpDirection[0] = possibleDirections[i];
            for (int j = 0; j < 4; j++) {
                tmpDirection[1] = possibleDirections[j];
                for (int k = 0; k < 4; k++) {
                    tmpDirection[2] = possibleDirections[k];
                    if (numbers3LayerButtons[i * 16 + j * 4 + k].getForeground() == highlightColor) {
                        quad.setActive(tmpDirection);
                    } else {
                        quad.setInactive(tmpDirection);
                    }
                }
            }
        }

        quad.updateQuadActiveState();
        updateAppearance(); // in addition to processAppearanceChange
    }

    @Override
    protected void processAppearanceChange(ActionEvent e) {
        JButton actionButton = (JButton) e.getSource();
        BUTTONS_STATE changeState;
        if (actionButton.getForeground() == normalColor) {
            actionButton.setForeground(highlightColor);
            changeState = BUTTONS_STATE.MARKED;
        } else {
            actionButton.setForeground(normalColor);
            changeState = BUTTONS_STATE.UNMARKED;
        }

        int i = 0;
        for (JButton numbersButtonLayer1 : numbers1LayerButtons) {
            if (actionButton == numbersButtonLayer1) {
                changeStateButtons(BUTTONS_LAYER.LAYER_2, i * 4, (i + 1) * 4 - 1, changeState);
                changeStateButtons(BUTTONS_LAYER.LAYER_3, i * 16, (i + 1) * 16 - 1, changeState);
                return;
            }
            i++;
        }

        int j = 0;
        for (JButton numbersButtonLayer2 : numbers2LayerButtons) {
            if (actionButton == numbersButtonLayer2) {
                changeStateButtons(BUTTONS_LAYER.LAYER_3, j * 4, (j + 1) * 4 - 1, changeState);
                return;
            }
            j++;
        }

        // If there are state changes in Layer 2 or 3, their parent layer 1 and 2 must getting corrected.
        // It's easier to do this with the function 'updateAppearance', after the quad was updated depending on layer 3
    }

    @Override
    protected String nameTreeFormat() {
        return "Numbers";
    }

    private void changeStateButtons(BUTTONS_LAYER layer, int fromIdx, int toIdx, BUTTONS_STATE newState) {
        Color tmpColor;
        if (newState == BUTTONS_STATE.MARKED) {
            tmpColor = highlightColor;
        } else {
            tmpColor = normalColor;
        }

        switch (layer) {
            case LAYER_1:
                colorChangeForButtons(numbers1LayerButtons, tmpColor, 3, fromIdx, toIdx);
                break;
            case LAYER_2:
                colorChangeForButtons(numbers2LayerButtons, tmpColor, 15, fromIdx, toIdx);
                break;
            case LAYER_3:
                colorChangeForButtons(numbers3LayerButtons, tmpColor, 63, fromIdx, toIdx);
                break;
        }
    }

    private void colorChangeForButtons(JButton[] buttons, Color color, int maxIdx, int fromIdx, int toIdx) {
        if (fromIdx < 0 || fromIdx > maxIdx || toIdx < 0 || toIdx > maxIdx) {
            return;
        }

        for (int i = fromIdx; i <= toIdx; i++) {
            buttons[i].setForeground(color);
        }
    }

    private enum BUTTONS_STATE {
        MARKED, UNMARKED
    }

    private enum BUTTONS_LAYER {
        LAYER_1, LAYER_2, LAYER_3
    }
}
