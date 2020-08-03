import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;

public class Numbers extends QuadTreeFormatPanel {
    private JButton[] numbers1LayerButtons = new JButton[4];
    private JButton[] numbers2LayerButtons = new JButton[16];
    private JButton[] numbers3LayerButtons = new JButton[64];
    private JTextArea textArea = new JTextArea(1, 90);

    private static Color normalColor = new Color(100, 100, 100, 50);
    private static Color highlightColor = Color.BLACK;

    public Numbers() {
        JPanel firstRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JPanel secondRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        firstRow.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        secondRow.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        Dimension numberDimension = new Dimension(13, 30);
        for (int i = 0; i < 4; i++) {
            numbers1LayerButtons[i] = new JButton(String.valueOf(i + 1));
            numbers1LayerButtons[i].addActionListener(this);
            numbers1LayerButtons[i].setPreferredSize(numberDimension);
            numbers1LayerButtons[i].setContentAreaFilled(false);
            numbers1LayerButtons[i].setBorderPainted(false);
            numbers1LayerButtons[i].setMargin(new Insets(0, 0, 0, 0));
            numbers1LayerButtons[i].setForeground(normalColor);
            firstRow.add(numbers1LayerButtons[i]);
        }

        firstRow.add(new JLabel("|"));

        for (int i = 0; i < 16; i++) {
            numbers2LayerButtons[i] = new JButton(String.valueOf((i % 4) + 1));
            numbers2LayerButtons[i].addActionListener(this);
            numbers2LayerButtons[i].setPreferredSize(numberDimension);
            numbers2LayerButtons[i].setContentAreaFilled(false);
            numbers2LayerButtons[i].setBorderPainted(false);
            numbers2LayerButtons[i].setMargin(new Insets(0, 0, 0, 0));
            numbers2LayerButtons[i].setForeground(normalColor);
            firstRow.add(numbers2LayerButtons[i]);

            if ((i + 1) % 4 == 0 && i + 1 != 16) {
                firstRow.add(new JLabel("-"));
            }
        }

        firstRow.add(new JLabel("|"));

        for (int i = 0; i < 64; i++) {
            numbers3LayerButtons[i] = new JButton(String.valueOf((i % 4) + 1));
            numbers3LayerButtons[i].addActionListener(this);
            numbers3LayerButtons[i].setPreferredSize(numberDimension);
            numbers3LayerButtons[i].setContentAreaFilled(false);
            numbers3LayerButtons[i].setBorderPainted(false);
            numbers3LayerButtons[i].setMargin(new Insets(0, 0, 0, 0));
            numbers3LayerButtons[i].setForeground(normalColor);
            firstRow.add(numbers3LayerButtons[i]);

            if ((i + 1) % 4 == 0 && i + 1 != 64) {
                firstRow.add(new JLabel("-"));
            }
        }

        textArea.setEditable(false);
        secondRow.add(textArea);

        this.add(firstRow);
        this.add(secondRow);
        this.add(Box.createRigidArea(new Dimension(0, 500)));
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

        numbersButtonsToString();
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

    private void numbersButtonsToString() {
        textArea.setText(" ");
        appendNumbersToTextArea(BUTTONS_LAYER.LAYER_1);
        textArea.append(" | ");
        appendNumbersToTextArea(BUTTONS_LAYER.LAYER_2);
        textArea.append(" | ");
        appendNumbersToTextArea(BUTTONS_LAYER.LAYER_3);
    }

    private void appendNumbersToTextArea(BUTTONS_LAYER layer) {
        int i = 0;
        int myMind = 0;
        String tmpNumbers = "";
        JButton[] numbersButtons;

        switch (layer) {
            case LAYER_1:
                numbersButtons = numbers1LayerButtons;
                break;
            case LAYER_2:
                numbersButtons = numbers2LayerButtons;
                break;
            case LAYER_3:
                numbersButtons = numbers3LayerButtons;
                break;
            default:
                return;
        }

        for (JButton numberButton : numbersButtons) {
            i++;

            if (numberButton.getForeground() == highlightColor) {
                tmpNumbers += numberButton.getText();
                myMind++;
            }

            if (i % 4 == 0 && myMind != 0) {
                String textAreaContent = textArea.getText();
                char lastSign = textAreaContent.toCharArray()[textAreaContent.length() - 1];
                if (i == 4 || lastSign == ' ') {
                    textArea.append(tmpNumbers);
                } else {
                    textArea.append(" - " + tmpNumbers);
                }

                tmpNumbers = "";
                myMind = 0;
            }
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
