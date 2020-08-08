import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.util.Arrays;

public class Numbers extends QuadTreeFormatPanel {
    private JButton[] numbers1LayerButtons = new JButton[4];
    private JButton[] numbers2LayerButtons = new JButton[16];
    private JButton[] numbers3LayerButtons = new JButton[64];
    private JTextArea textArea = new JTextArea(1, 90);

    private Color normalColor;
    private Color highlightColor;

    public Numbers(Dimension numberDimension, Color normalColor, Color highlightColor) {
        this.normalColor = normalColor;
        this.highlightColor = highlightColor;

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel firstRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JPanel secondRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        firstRow.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        firstRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        secondRow.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        secondRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        initNumbersLayer(numberDimension, firstRow, numbers1LayerButtons);
        firstRow.add(new JLabel("|"));
        initNumbersLayer(numberDimension, firstRow, numbers2LayerButtons);
        firstRow.add(new JLabel("|"));
        initNumbersLayer(numberDimension, firstRow, numbers3LayerButtons);

        textArea.setEditable(false);
        secondRow.add(textArea);

        this.add(firstRow);
        this.add(secondRow);
        firstRow.setMaximumSize(firstRow.getPreferredSize());
        secondRow.setMaximumSize(secondRow.getPreferredSize());
    }

    private void initNumbersLayer(Dimension numberDimension, JPanel row, JButton[] numbersLayerButtons) {
        for (int i = 0; i < numbersLayerButtons.length; i++) {
            numbersLayerButtons[i] = new JButton(String.valueOf((i % 4) + 1));
            numbersLayerButtons[i].addActionListener(this);
            numbersLayerButtons[i].setPreferredSize(numberDimension);
            numbersLayerButtons[i].setContentAreaFilled(false);
            numbersLayerButtons[i].setBorderPainted(false);
            numbersLayerButtons[i].setMargin(new Insets(0, 0, 0, 0));
            numbersLayerButtons[i].setForeground(normalColor);
            row.add(numbersLayerButtons[i]);

            if ((i + 1) % 4 == 0 && i + 1 != numbersLayerButtons.length) {
                row.add(new JLabel("-"));
            }
        }
    }

    @Override
    protected void updateAppearance() {
        Direction[] possibleDirections = new Direction[]{Direction.NW, Direction.NE, Direction.SE, Direction.SW};
        Direction[] tmpDirection = new Direction[3];

        for (int i = 0; i < 4; i++) {
            tmpDirection[0] = possibleDirections[i];
            matchButtonStateAndQuadState(numbers1LayerButtons[i], Arrays.copyOfRange(tmpDirection, 0, 1));

            for (int j = 0; j < 4; j++) {
                tmpDirection[1] = possibleDirections[j];
                matchButtonStateAndQuadState(numbers2LayerButtons[i * 4 + j], Arrays.copyOfRange(tmpDirection, 0, 2));

                for (int k = 0; k < 4; k++) {
                    tmpDirection[2] = possibleDirections[k];
                    matchButtonStateAndQuadState(numbers3LayerButtons[i * 16 + j * 4 + k], tmpDirection);
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
