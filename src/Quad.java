import java.awt.*;

public class Quad {
    private boolean active;
    private Quad upLeft;
    private Quad upRight;
    private Quad boRight;
    private Quad boLeft;

    public Quad(Quad upLeft, Quad upRight, Quad boRight, Quad boLeft) {
        this.upLeft = upLeft;
        this.upRight = upRight;
        this.boRight = boRight;
        this.boLeft = boLeft;

        this.active = upLeft.isActive() || upRight.isActive() || boRight.isActive() || boLeft.isActive();
    }

    public Quad(boolean active) {
        this.active = active;

        this.upLeft = null;
        this.upRight = null;
        this.boRight = null;
        this.boLeft = null;
    }

    public Quad(Quad that) {
        if (that.upLeft == null && that.upRight == null && that.boLeft == null && that.boRight == null) {
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

    public boolean isActive() {
        return active;
    }

    public boolean isActive(Direction[] directions) {
        return this.moveTroughQuadTree(directions).isActive();
    }

    public void setActive() {
        active = true;
    }

    public void setActive(Direction[] directions) {
        this.moveTroughQuadTree(directions).active = true;
    }

    public void setInactive() {
        active = false;
    }

    public void setInactive(Direction[] directions) {
        this.moveTroughQuadTree(directions).active = false;
    }

    public void updateQuadActiveState() {
        // updates which Quads are active and which are not
        if (upLeft == null && upRight == null && boLeft == null && boRight == null) {
            return;
        }

        upLeft.updateQuadActiveState();
        upRight.updateQuadActiveState();
        boRight.updateQuadActiveState();
        boLeft.updateQuadActiveState();

        active = upLeft.isActive() || upRight.isActive() || boRight.isActive() || boLeft.isActive();
    }

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
