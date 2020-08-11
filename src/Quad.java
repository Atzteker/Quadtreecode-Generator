/**
 * This class represents one element of a Quad-tree. It can be a child of other Quad-tree elements and can also contain other Quad-tree elements.
 * The children of this Quad-Tree element are elements in a lower layer and the parent is a element in a higher layer of the Quad-Tree.
 * Corresponding to that, the Quad-Tree element which haven't a parent is the root of the quad tree and elements which haven't children are leaves,
 * respectively elements in the lowest layer of the quad tree.
 */
public class Quad {
    /**
     * The active state is true if the element contains data, respectively is used, otherwise it's false.
     * The active state depends on the children. If the quad element haven't children the state can set as required.
     */
    private boolean active;
    /**
     * This are the children of the Quad element
     */
    private Quad upLeft, upRight, boRight, boLeft;

    /**
     * Creates a new Quad-Tree element which have the given children. This element will be initialized as active, if one of the given children is active.
     *
     * @param upLeft  The child of this element in the North-West direction
     * @param upRight The child of this element in the North-East direction
     * @param boRight The child of this element in the South-West direction
     * @param boLeft  The child of this element in the South-East direction
     */
    public Quad(Quad upLeft, Quad upRight, Quad boRight, Quad boLeft) {
        this.upLeft = upLeft;
        this.upRight = upRight;
        this.boRight = boRight;
        this.boLeft = boLeft;

        this.active = upLeft.isActive() || upRight.isActive() || boRight.isActive() || boLeft.isActive();
    }

    /**
     * Creates a new Quad-Tree element without children.
     *
     * @param active Defines the elements active state. True means the element contains data, respectively is used. False means the opposite.
     */
    public Quad(boolean active) {
        this.active = active;

        this.upLeft = null;
        this.upRight = null;
        this.boRight = null;
        this.boLeft = null;
    }

    /**
     * Copies the given Quad element
     *
     * @param that The element which should be copied.
     */
    public Quad(Quad that) {
        if (that.isModificationAllowed()) {
            //last element
            this.upLeft = null;
            this.upRight = null;
            this.boRight = null;
            this.boLeft = null;
        } else {
            this.upLeft = new Quad(that.upLeft);
            this.upRight = new Quad(that.upRight);
            this.boRight = new Quad(that.boRight);
            this.boLeft = new Quad(that.boLeft);
        }
        this.active = that.isActive();
    }

    /**
     * Determines whether this element is active or not.
     *
     * @return true if active, false if inactive
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Does the same like {@link #isActive()} but for a child element in a given direction.
     *
     * @param directions This are the directions to step trough the Quad-Tree to find the specific child element.
     * @return true if active, false if inactive
     */
    public boolean isActive(Direction[] directions) {
        return this.moveTroughQuadTree(directions).isActive();
    }

    /**
     * Sets the Quad as active. You can only change the active state if this Quad element is one of the last elements in the quad tree (a leave).
     * The active state of the elements in the higher layers are depending on the active state of their children.
     */
    public void setActive() {
        setActiveState(true);
    }

    /**
     * Does the same like {@link #setActive()} but for a child element in a given direction.
     *
     * @param directions This are the directions to step trough the Quad-Tree to find the specific child element.
     */
    public void setActive(Direction[] directions) {
        setActiveState(true, directions);
    }

    /**
     * Does the opposite of {@link #setActive()}.
     */
    public void setInactive() {
        setActiveState(false);
    }

    /**
     * Does the same like {@link #setInactive()} but for a child element in a given direction.
     *
     * @param directions This are the directions to step trough the Quad-Tree to find the specific child element.
     */
    public void setInactive(Direction[] directions) {
        setActiveState(false, directions);
    }

    /**
     * Defines the active state of the element.
     *
     * @param newSate true if the Quad element should be active, false if the Quad element should be inactive
     */
    private void setActiveState(boolean newSate) {
        if (isModificationAllowed()) {
            active = newSate;
        }
    }

    /**
     * Does the same like {@link #setActiveState(boolean)} but for a child element in a given direction.
     *
     * @param newSate    true if the Quad element should be active, false if the Quad element should be inactive
     * @param directions This are the directions to step trough the Quad-Tree to find the specific child element.
     */
    private void setActiveState(boolean newSate, Direction[] directions) {
        Quad tmpQuad = this.moveTroughQuadTree(directions);
        if (tmpQuad.isModificationAllowed()) {
            tmpQuad.active = newSate;
        }
    }

    /**
     * Determines whether the element have no children. If so, its allowed to modify the quad element,
     *
     * @return true if the element could be modified, false if not.
     */
    private boolean isModificationAllowed() {
        return upLeft == null && upRight == null && boLeft == null && boRight == null;
    }

    /**
     * Depending on the elements in the lowest layer, the active state of the elements in the higher layer wil be updated.
     * If you doesn't do this after you modified the lowest layer, the higher layers of the Quad-Tree contains wrong active states.
     * This function won't be called automatically, if you modify one element in the lowest layer.
     * The reason is that normally you modify multiple elements in a short time and than it's better to update once and not after every modification.
     */
    public void updateQuadActiveState() {
        if (isModificationAllowed()) {
            return;
        }

        upLeft.updateQuadActiveState();
        upRight.updateQuadActiveState();
        boRight.updateQuadActiveState();
        boLeft.updateQuadActiveState();

        active = upLeft.isActive() || upRight.isActive() || boRight.isActive() || boLeft.isActive();
    }

    /**
     * Starting from this element this function following a given path through the quad tree layers.
     * It chooses one of the four children corresponding to the first given direction and does the same for this child with the next direction ond so on.
     *
     * @param directions The path trough the Quad-Tree.
     * @return The element at the end of the given path.
     */
    private Quad moveTroughQuadTree(Direction[] directions) {
        Quad tmpQuad = this;
        for (Direction direction : directions) {
            try {
                switch (direction) {
                    case NW:
                        tmpQuad = tmpQuad.upLeft;
                        break;
                    case NE:
                        tmpQuad = tmpQuad.upRight;
                        break;
                    case SE:
                        tmpQuad = tmpQuad.boRight;
                        break;
                    case SW:
                        tmpQuad = tmpQuad.boLeft;
                        break;
                }
            } catch (NullPointerException e) {
                break;
            }
        }

        return tmpQuad;
    }

}
