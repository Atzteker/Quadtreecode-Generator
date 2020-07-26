public class Quad {
    private boolean active;
    public Quad upLeft;
    public Quad upRight;
    public Quad boRight;
    public Quad boLeft;

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

    public void setActive() {
        active = true;
    }

    public void setInactive() {
        active = false;
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
}
