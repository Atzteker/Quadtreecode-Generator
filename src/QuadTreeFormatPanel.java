import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * This class is the template for a Quad-Tree format you want to integrate in the 'Quad-Tree Converter'.
 * The logic for a Quad-Tree format and there visual components belong to a JPanel.
 */
public abstract class QuadTreeFormatPanel extends JPanel implements ActionListener {
    /**
     * Array with all possible directions of enum Direction.
     * You can use this array to easily iterate over all possible Directions.
     */
    protected final Direction[] POSSIBLE_DIRECTIONS = new Direction[]{Direction.NW, Direction.NE, Direction.SE, Direction.SW};
    /**
     * The number of all possible directions for a Quad-Tree children
     */
    protected final int NUMBER_OF_DIRECTIONS = 4;
    /**
     * The current Quad Tree
     */
    protected Quad quad;
    /**
     * The name of the Quad-Tree format.
     * Is needed to distinguish between the different quadListeners.
     */
    protected String treeFormat = nameTreeFormat();
    protected PropertyChangeListener quadListener = new QuadListener();
    /**
     * The color of a Quad-Tree component/element if they aren't used/active
     */
    protected Color normalColor;
    /**
     * The color of a Quad-Tree component/element if they are used/active
     */
    protected Color highlightColor;

    /**
     * The basic constructor which should be used from the different Quad-Tree formats.
     * It initializes an empty Quad-Tree and defines the to possible colors for the visual Quad-Tree components
     *
     * @param normalColor {@link #normalColor}
     * @param highlightColor {@link #highlightColor}
     */
    public QuadTreeFormatPanel(Color normalColor, Color highlightColor) {
        this.quad = initializeQuadTree();
        this.normalColor = normalColor;
        this.highlightColor = highlightColor;
    }

    /**
     * Creates a Quad-Tree with three layers(+ root element). So in the end there are 64 leave elements in the lowest layer.
     * All elements are initialized as inactive respectively not used.
     *
     * @return A new Quad-Tree(root + 3 layer) where all elements are inactive.
     */
    private Quad initializeQuadTree() {
        Quad[] firstLayer = new Quad[4];
        Quad[] secondLayer = new Quad[4];

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                secondLayer[j] = new Quad(new Quad(false), new Quad(false), new Quad(false), new Quad(false));
            }
            firstLayer[i] = new Quad(secondLayer[0], secondLayer[1], secondLayer[2], secondLayer[3]);
        }
        return new Quad(firstLayer[0], firstLayer[1], firstLayer[2], firstLayer[3]);
    }

    /**
     * Registers a QuadListener from a different Quad-Tree format, so that this Quad-Tree format get informed if the Quad-Tree was changed.
     * also @see #addPropertyChangeListener(String, PropertyChangeListener)
     *
     * @param quadListener The QuadListener from another Quad-Tree format
     */
    public void addQuadListener(PropertyChangeListener quadListener) {
        this.addPropertyChangeListener(treeFormat, quadListener);
    }

    /**
     * @return The QuadListener of this Quad-Tree format
     */
    public PropertyChangeListener getQuadListener() {
        return quadListener;
    }

    /**
     * For a given path trough the Quad-Tree this function checks if the element at the end of this path is active
     * and returns the corresponding color for a visual component.
     *
     * @param directions The Path to the Quad-Tree element which will be checked.
     * @return Returns the highlightColor if the element is active and the normal color if the element was inactive.
     */
    protected Color getColorDependingOnQuadState(Direction[] directions) {
        if (directions == null) {
            if (quad.isActive()) {
                return highlightColor;
            } else {
                return normalColor;
            }
        }

        if (quad.isActive(directions)) {
            return highlightColor;
        } else {
            return normalColor;
        }
    }

    /**
     * For a given path trough the Quad-Tree this function sets the element at the end of this path
     * to active if the given color of the visual component is the highlightColor and
     * to inactive if the given color of the visual component is the normalColor
     *
     * @param buttonsColor The current color of the visual component corresponding to the Quad-Tree element.
     * @param directions   The Path to the Quad-Tree element which will be changed.
     */
    protected void changeQuadStateDependingOnColor(Color buttonsColor, Direction[] directions) {
        if (buttonsColor == highlightColor) {
            quad.setActive(directions);
        } else {
            quad.setInactive(directions);
        }
    }

    /**
     * If the Quad Tree was changed especially from another Quad-Tree format
     * the appearance of this Quad-Tree format must be updated depending on the new Quad-Tree.
     * The logic for this appearance update belongs in this function.
     */
    protected abstract void updateAppearance();

    /**
     * If the user want to change the Quad-Tree by activating or deactivating visual components of this Quad-Tree format
     * the Quad-Tree must to be updated. So that the other Quad-Tree formats can be adjusted to the new Quad-Tree.
     * The logic for this Quad-Tree update belongs in this function.
     */
    protected abstract void updateQuad();

    /**
     * If the user want to change the Quad-Tree by activating or deactivating visual components of this Quad-Tree format
     * the color of the visual components must be changed and sometimes also visual components which are depending on these.
     * The logic for this adjustment belongs in this function.
     *
     * @param e The ActionEvent which was triggered. With the ActionEvent you can access the visual component which was clicked from the user.
     */
    protected abstract void processAppearanceChange(ActionEvent e);

    /**
     * In this function you must return the name of the Quad-Tree format.
     * It's required to set the name of the Quad-Tree format to distinguish between the different QuadListeners.
     *
     * @return The name of the Quad-Tree format.
     */
    protected abstract String nameTreeFormat();

    /**
     * If a visual component was clicked adjust the appearance of the own Quad-Tree format,
     * generate the new Quad-Tree and inform the other Quad-Tree formats that the Quad Tree was changed.
     *
     * @param e The ActionEvent which was triggered. With the ActionEvent you can access the visual component which was clicked from the user.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        processAppearanceChange(e);
        Quad oldQuad = new Quad(quad);
        updateQuad();
        firePropertyChange(treeFormat, oldQuad, quad);
    }

    /**
     * This class listen on changes of the Quad-Tree.
     */
    private class QuadListener implements PropertyChangeListener {
        /**
         * If the other Quad-Tree formats changed the Quad-Tree adjust the appearance of this Quad-Tree format.
         *
         * @param evt Which Quad-Tree format was changed and what is the new Quad-Tree
         */
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt == null)
                return;

            if (!evt.getPropertyName().equals(treeFormat)) {
                quad = new Quad((Quad) evt.getNewValue());
                updateAppearance();
            }
        }
    }

}
