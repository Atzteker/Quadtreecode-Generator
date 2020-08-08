import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;

public class Numbers extends QuadTreeFormatPanel {
    private enum BUTTONS_LAYER {
        LAYER_1(4), LAYER_2(16), LAYER_3(64);
        public JButton[] buttonsLayerArray;

        BUTTONS_LAYER(int amount) {
            this.buttonsLayerArray = new JButton[amount];
        }
    }
    private JTextArea textArea = new JTextArea(1, 90);

    public Numbers(Dimension numberDimension, Color normalColor, Color highlightColor) {
        super(normalColor,highlightColor);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel firstRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JPanel secondRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        firstRow.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        firstRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        secondRow.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        secondRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        initNumbersLayer(numberDimension, firstRow, BUTTONS_LAYER.LAYER_1);
        firstRow.add(new JLabel("|"));
        initNumbersLayer(numberDimension, firstRow, BUTTONS_LAYER.LAYER_2);
        firstRow.add(new JLabel("|"));
        initNumbersLayer(numberDimension, firstRow, BUTTONS_LAYER.LAYER_3);

        textArea.setEditable(false);
        secondRow.add(textArea);

        this.add(firstRow);
        this.add(secondRow);
        firstRow.setMaximumSize(firstRow.getPreferredSize());
        secondRow.setMaximumSize(secondRow.getPreferredSize());
    }

    private void initNumbersLayer(Dimension numberDimension, JPanel row, BUTTONS_LAYER buttonsLayer) {
        for (int i = 0; i < buttonsLayer.buttonsLayerArray.length; i++) {
            buttonsLayer.buttonsLayerArray[i] = new JButton(String.valueOf((i % 4) + 1));
            buttonsLayer.buttonsLayerArray[i].addActionListener(this);
            buttonsLayer.buttonsLayerArray[i].setPreferredSize(numberDimension);
            buttonsLayer.buttonsLayerArray[i].setContentAreaFilled(false);
            buttonsLayer.buttonsLayerArray[i].setBorderPainted(false);
            buttonsLayer.buttonsLayerArray[i].setMargin(new Insets(0, 0, 0, 0));
            buttonsLayer.buttonsLayerArray[i].setForeground(normalColor);
            row.add(buttonsLayer.buttonsLayerArray[i]);

            if ((i + 1) % 4 == 0 && i + 1 != buttonsLayer.buttonsLayerArray.length) {
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
            BUTTONS_LAYER.LAYER_1.buttonsLayerArray[i].setForeground(colorDependingOnQuadState(Arrays.copyOfRange(tmpDirection, 0, 1)));

            for (int j = 0; j < 4; j++) {
                tmpDirection[1] = possibleDirections[j];
                BUTTONS_LAYER.LAYER_2.buttonsLayerArray[i * 4 + j].setForeground(colorDependingOnQuadState(Arrays.copyOfRange(tmpDirection, 0, 2)));

                for (int k = 0; k < 4; k++) {
                    tmpDirection[2] = possibleDirections[k];
                    BUTTONS_LAYER.LAYER_3.buttonsLayerArray[i * 16 + j * 4 + k].setForeground(colorDependingOnQuadState(tmpDirection));
                }
            }
        }

        numbersButtonsToString();
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
                    if (BUTTONS_LAYER.LAYER_3.buttonsLayerArray[i * 16 + j * 4 + k].getForeground() == highlightColor) {
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
        for (JButton numbersButtonLayer1 : BUTTONS_LAYER.LAYER_1.buttonsLayerArray) {
            if (actionButton == numbersButtonLayer1) {
                colorChangeForButtonsLayer(BUTTONS_LAYER.LAYER_2, changeState, i * 4, (i + 1) * 4 - 1);
                colorChangeForButtonsLayer(BUTTONS_LAYER.LAYER_3, changeState, i * 16, (i + 1) * 16 - 1);
                return;
            }
            i++;
        }

        int j = 0;
        for (JButton numbersButtonLayer2 : BUTTONS_LAYER.LAYER_2.buttonsLayerArray) {
            if (actionButton == numbersButtonLayer2) {
                colorChangeForButtonsLayer(BUTTONS_LAYER.LAYER_3, changeState, j * 4, (j + 1) * 4 - 1);
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

        for (JButton numberButton : layer.buttonsLayerArray) {
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

    private void colorChangeForButtonsLayer(BUTTONS_LAYER buttonsLayer, BUTTONS_STATE newState, int fromIdx, int toIdx) {
        int maxIdx = buttonsLayer.buttonsLayerArray.length - 1;
        if (fromIdx < 0 || fromIdx > maxIdx || toIdx < 0 || toIdx > maxIdx) {
            return;
        }

        for (int i = fromIdx; i <= toIdx; i++) {
            if (newState == BUTTONS_STATE.MARKED) {
                buttonsLayer.buttonsLayerArray[i].setForeground(highlightColor);
            } else {
                buttonsLayer.buttonsLayerArray[i].setForeground(normalColor);
            }

        }
    }

    private enum BUTTONS_STATE {
        MARKED, UNMARKED
    }
}
