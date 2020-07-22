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

    public Quad(boolean active){
        this.active = active;

        this.upLeft = null;
        this.upRight = null;
        this.boRight = null;
        this.boLeft = null;
    }

    public boolean isActive() {
        return active;
    }
}
