import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.prefs.PreferenceChangeListener;

public abstract class QuadTreeFormatPanel extends JPanel implements ActionListener {
    protected Quad quad;
    protected String treeFormat = nameTreeFormat();
    protected PropertyChangeListener quadListener = new QuadListener();
    protected Color normalColor;
    protected Color highlightColor;

    public QuadTreeFormatPanel(Color normalColor, Color highlightColor) {
        this.quad = initializeQuadTree();
        this.normalColor = normalColor;
        this.highlightColor = highlightColor;
    }

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

    public void addQuadListener(PropertyChangeListener quadListener, String name) {
        this.addPropertyChangeListener(name, quadListener);
    }

    public Quad getQuad() {
        return quad;
    }

    public String getTreeFormat() {
        return treeFormat;
    }

    public PropertyChangeListener getQuadListener() {
        return quadListener;
    }

    protected abstract void updateAppearance();

    protected abstract void updateQuad();

    protected abstract void processAppearanceChange(ActionEvent e);

    protected abstract String nameTreeFormat();

    @Override
    public void actionPerformed(ActionEvent e) {
        processAppearanceChange(e);
        Quad oldQuad = new Quad(quad);
        updateQuad();
        firePropertyChange(treeFormat, oldQuad, quad);
    }

    private class QuadListener implements PropertyChangeListener {
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
