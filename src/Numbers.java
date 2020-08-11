import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;

/**
 * Represents the Quad-Tree as a Sequence of Numbers. The numbers 1,2,3 and 4 corresponds to the compass directions NW,NE,SE and SW.
 * If one of the numbers are part of the sequence it means that this Quad-Tree element is represented in the Quad-Tree, respectively used/filled.
 * So for a Quad-Tree element, where the NW child and the SW child are filled with something, the numbers would be 14.
 * To distinguish between the different Quad-Tree elements and there children, there is a minus(-) between them in the sequence
 * and the layers in the Quad-Tree are separated with '|'.
 * For example: 12|13-14|1-23-134-2. 13 after the first '|' means that for the NW child of the root element,
 * there children NW and SE are filled/used and so on.
 */
public class Numbers extends QuadTreeFormatPanel {
    /**
     * Organizes the visual components.
     * It divided the visual components into three layer (Sequences between '|').
     * In this case the visual components are numbers from 1 to 4 which the user can activate or deactivate.
     */
    private enum NUMBERS_LAYER {
        LAYER_1(4), LAYER_2(16), LAYER_3(64);
        public JButton[] numbersLayerArray;

        NUMBERS_LAYER(int amount) {
            this.numbersLayerArray = new JButton[amount];
        }
    }

    /**
     * Cause of the user always can modify the whole possible sequence,
     * it's required that in this text area only the pure sequence is printed
     * which are the used/filled Quad-Tree elements/numbers.
     */
    private JTextArea textArea = new JTextArea(1, 90);

    /**
     * Creates a new JPanel with a Quad-Tree represented as a sequence of numbers and a text area to copy the sequence
     *
     * @param numberDimension dimension/size of one number/visual component
     * @param normalColor     {@link #normalColor}
     * @param highlightColor  {@link #highlightColor}
     */
    public Numbers(Dimension numberDimension, Color normalColor, Color highlightColor) {
        super(normalColor, highlightColor);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel firstRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JPanel secondRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        firstRow.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        firstRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        secondRow.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        secondRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        initNumbersLayer(numberDimension, firstRow, NUMBERS_LAYER.LAYER_1);
        firstRow.add(new JLabel("|"));
        initNumbersLayer(numberDimension, firstRow, NUMBERS_LAYER.LAYER_2);
        firstRow.add(new JLabel("|"));
        initNumbersLayer(numberDimension, firstRow, NUMBERS_LAYER.LAYER_3);

        textArea.setEditable(false);
        secondRow.add(textArea);

        this.add(firstRow);
        this.add(secondRow);
        firstRow.setMaximumSize(firstRow.getPreferredSize());
        secondRow.setMaximumSize(secondRow.getPreferredSize());
    }

    /**
     * For a given Quad-Tree layer the corresponding visual components(numbers) will be added to the Numbers panel on the given row.
     * The Numbers will be initialised as inactive/unused.
     *
     * @param numberDimension dimension/size of one number/visual component
     * @param row             The row in which the given visual components will be added.
     *                        Normally there are the first row and the second row and all numbers should be on the first row.
     * @param numbersLayer    The Quad-Tree layer with the corresponding visual components(numbers)
     */
    private void initNumbersLayer(Dimension numberDimension, JPanel row, NUMBERS_LAYER numbersLayer) {
        for (int i = 0; i < numbersLayer.numbersLayerArray.length; i++) {
            numbersLayer.numbersLayerArray[i] = new JButton(String.valueOf((i % NUMBER_OF_DIRECTIONS) + 1));
            numbersLayer.numbersLayerArray[i].addActionListener(this);
            numbersLayer.numbersLayerArray[i].setPreferredSize(numberDimension);
            numbersLayer.numbersLayerArray[i].setContentAreaFilled(false);
            numbersLayer.numbersLayerArray[i].setBorderPainted(false);
            numbersLayer.numbersLayerArray[i].setMargin(new Insets(0, 0, 0, 0));
            numbersLayer.numbersLayerArray[i].setForeground(normalColor);
            row.add(numbersLayer.numbersLayerArray[i]);

            if ((i + 1) % NUMBER_OF_DIRECTIONS == 0 && i + 1 != numbersLayer.numbersLayerArray.length) {
                row.add(new JLabel("-"));
            }
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * For every layer in the Quad-Tree it transmits the active state of the elements to the equivalent numbers.
     */
    @Override
    protected void updateAppearance() {
        Direction[] possibleDirections = new Direction[]{Direction.NW, Direction.NE, Direction.SE, Direction.SW};
        Direction[] tmpDirection = new Direction[3];

        for (int i = 0; i < NUMBER_OF_DIRECTIONS; i++) {
            tmpDirection[0] = possibleDirections[i];
            NUMBERS_LAYER.LAYER_1.numbersLayerArray[i].setForeground(getColorDependingOnQuadState(Arrays.copyOfRange(tmpDirection, 0, 1)));

            for (int j = 0; j < NUMBER_OF_DIRECTIONS; j++) {
                tmpDirection[1] = possibleDirections[j];
                NUMBERS_LAYER.LAYER_2.numbersLayerArray[i * 4 + j].setForeground(getColorDependingOnQuadState(Arrays.copyOfRange(tmpDirection, 0, 2)));

                for (int k = 0; k < NUMBER_OF_DIRECTIONS; k++) {
                    tmpDirection[2] = possibleDirections[k];
                    NUMBERS_LAYER.LAYER_3.numbersLayerArray[i * 16 + j * 4 + k].setForeground(getColorDependingOnQuadState(tmpDirection));
                }
            }
        }

        numbersButtonsToString();
    }

    /**
     * {@inheritDoc}
     * <p>
     * The quad will be updated based on the third layer of the visual components, which is the lowest layer in the Quad-Tree.
     * At the end the whole Quad-Tree can be updated depending on the lowest layer.
     * Also the appearance of this Quad-Tree format will be updated based on the updated Quad-Tree,
     * because it's easier to change the whole appearance based on the new quad tree,
     * instead of do this hardcoded in {@link #processAppearanceChange(ActionEvent)},
     */
    @Override
    protected void updateQuad() {
        Direction[] tmpDirection = new Direction[3];

        for (int i = 0; i < NUMBER_OF_DIRECTIONS; i++) {
            tmpDirection[0] = POSSIBLE_DIRECTIONS[i];
            for (int j = 0; j < NUMBER_OF_DIRECTIONS; j++) {
                tmpDirection[1] = POSSIBLE_DIRECTIONS[j];
                for (int k = 0; k < NUMBER_OF_DIRECTIONS; k++) {
                    tmpDirection[2] = POSSIBLE_DIRECTIONS[k];
                    changeQuadStateDependingOnColor(NUMBERS_LAYER.LAYER_3.numbersLayerArray[i * 16 + j * 4 + k].getForeground(), tmpDirection);
                }
            }
        }

        quad.updateQuadActiveState();
        updateAppearance(); // in addition to processAppearanceChange
    }

    /**
     * {@inheritDoc}
     * <p>
     * If the user clicked one Number their state switches from active to inactive or vice versa.
     * Later the Quad-Tree will be updated based on the third numbers layer.
     * So if there are changes in the upper layer it's required to adjust the third layer and transmit the changes downwards.
     * Changes in the third layer must not be transmit upwards, because the upper layer aren't needed to update the Quad-Tree
     * and their appearance can be easily changed with the updated Quad-Tree.
     * To guarantee that, after the user had activated or deactivated a number in the upper layers, the number sequence remains correct
     * also the 'child numbers' of these activated or deactivated number needs to be set accordingly.
     */
    @Override
    protected void processAppearanceChange(ActionEvent e) {
        JButton actionButton = (JButton) e.getSource();
        NUMBER_STATE changeState;
        if (actionButton.getForeground() == normalColor) {
            actionButton.setForeground(highlightColor);
            changeState = NUMBER_STATE.MARKED;
        } else {
            actionButton.setForeground(normalColor);
            changeState = NUMBER_STATE.UNMARKED;
        }

        int i = 0;
        for (JButton numbersButtonLayer1 : NUMBERS_LAYER.LAYER_1.numbersLayerArray) {
            if (actionButton == numbersButtonLayer1) {
                colorChangeForButtonsLayer(NUMBERS_LAYER.LAYER_2, changeState, i * 4, (i + 1) * 4 - 1);
                colorChangeForButtonsLayer(NUMBERS_LAYER.LAYER_3, changeState, i * 16, (i + 1) * 16 - 1);
                return;
            }
            i++;
        }

        int j = 0;
        for (JButton numbersButtonLayer2 : NUMBERS_LAYER.LAYER_2.numbersLayerArray) {
            if (actionButton == numbersButtonLayer2) {
                colorChangeForButtonsLayer(NUMBERS_LAYER.LAYER_3, changeState, j * 4, (j + 1) * 4 - 1);
                return;
            }
            j++;
        }

        // If there are state changes in Layer 2 or 3, their parent layer 1 and 2 must getting corrected.
        // It's easier to do this with the function 'updateAppearance', after the quad was updated depending on layer 3
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String nameTreeFormat() {
        return "Numbers";
    }

    /**
     * Converts the visual components/numbers in a string and write the string in the textArea.
     */
    private void numbersButtonsToString() {
        textArea.setText(" ");
        appendNumbersToTextArea(NUMBERS_LAYER.LAYER_1);
        textArea.append(" | ");
        appendNumbersToTextArea(NUMBERS_LAYER.LAYER_2);
        textArea.append(" | ");
        appendNumbersToTextArea(NUMBERS_LAYER.LAYER_3);
    }

    /**
     * The activated numbers from the given layer are converted to a string and appended at the textArea.
     *
     * @param layer One of the three layers with visual components/numbers.
     */
    private void appendNumbersToTextArea(NUMBERS_LAYER layer) {
        int i = 0;
        int myMind = 0;
        String tmpNumbers = "";

        for (JButton numberButton : layer.numbersLayerArray) {
            i++;

            if (numberButton.getForeground() == highlightColor) {
                tmpNumbers += numberButton.getText();
                myMind++;
            }

            if (i % NUMBER_OF_DIRECTIONS == 0 && myMind != 0) {
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

    /**
     * The function changes the color of the visual components/numbers corresponding to the given state.
     * Only the numbers in the given Index range will be changed.
     *
     * @param numbersLayer One of the three layers with visual components/numbers. The color of this numbers will be changed.
     * @param newState     The state on which the numbers should set
     * @param fromIdx      The first number which should set to the given state
     * @param toIdx        The last number which should set to the given state
     */
    private void colorChangeForButtonsLayer(NUMBERS_LAYER numbersLayer, NUMBER_STATE newState, int fromIdx, int toIdx) {
        int maxIdx = numbersLayer.numbersLayerArray.length - 1;
        if (fromIdx < 0 || fromIdx > maxIdx || toIdx < 0 || toIdx > maxIdx) {
            return;
        }

        for (int i = fromIdx; i <= toIdx; i++) {
            if (newState == NUMBER_STATE.MARKED) {
                numbersLayer.numbersLayerArray[i].setForeground(highlightColor);
            } else {
                numbersLayer.numbersLayerArray[i].setForeground(normalColor);
            }

        }
    }

    /**
     * Can be used to describe if a number is active or inactive
     */
    private enum NUMBER_STATE {
        /**
         * The number is active/used
         */
        MARKED,
        /**
         * The number is inactive/unused
         */
        UNMARKED
    }
}
