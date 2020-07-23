import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;

public class Numbers extends QuadTreeFormatPanel {
    private JButton[] numbers1LayerButtons = new JButton[4];
    private JButton[] numbers2LayerButtons = new JButton[16];
    private JButton[] numbers3LayerButtons = new JButton[64];

    private static Color normalColor = new Color(100,100,100, 50);
    private static Color highlightColor = Color.BLACK;
    
    public Numbers(){
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        this.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        Dimension numberDimension = new Dimension(13, 30);
        for (int i = 0; i < 4; i++) {
            numbers1LayerButtons[i] = new JButton(String.valueOf(i + 1));
            numbers1LayerButtons[i].addActionListener(this);
            numbers1LayerButtons[i].setPreferredSize(numberDimension);
            numbers1LayerButtons[i].setContentAreaFilled(false);
            numbers1LayerButtons[i].setBorderPainted(false);
            numbers1LayerButtons[i].setMargin(new Insets(0, 0, 0, 0));
            numbers1LayerButtons[i].setForeground(normalColor);
            this.add(numbers1LayerButtons[i]);
        }

        this.add(new JLabel("|"));

        for (int i = 0; i < 16; i++) {
            numbers2LayerButtons[i] = new JButton(String.valueOf((i % 4) + 1));
            numbers2LayerButtons[i].addActionListener(this);
            numbers2LayerButtons[i].setPreferredSize(numberDimension);
            numbers2LayerButtons[i].setContentAreaFilled(false);
            numbers2LayerButtons[i].setBorderPainted(false);
            numbers2LayerButtons[i].setMargin(new Insets(0, 0, 0, 0));
            numbers2LayerButtons[i].setForeground(normalColor);
            this.add(numbers2LayerButtons[i]);

            if ((i + 1) % 4 == 0) {
                if (i + 1 != 16) {
                    this.add(new JLabel("-"));
                }
            }
        }

        this.add(new JLabel("|"));

        for (int i = 0; i < 64; i++) {
            numbers3LayerButtons[i] = new JButton(String.valueOf((i % 4) + 1));
            numbers3LayerButtons[i].addActionListener(this);
            numbers3LayerButtons[i].setPreferredSize(numberDimension);
            numbers3LayerButtons[i].setContentAreaFilled(false);
            numbers3LayerButtons[i].setBorderPainted(false);
            numbers3LayerButtons[i].setMargin(new Insets(0, 0, 0, 0));
            numbers3LayerButtons[i].setForeground(normalColor);
            this.add(numbers3LayerButtons[i]);

            if ((i + 1) % 4 == 0) {
                if (i + 1 != 64) {
                    this.add(new JLabel("-"));
                }
            }
        }
    }

    @Override
    protected void updateAppearance() {
        System.out.println("numbers update Appearance");
    }

    @Override
    protected void updateQuad() {
        System.out.println("number update quad");
    }

    @Override
    protected void processAppearanceChange(ActionEvent e) {

    }

    @Override
    protected String nameTreeFormat() {
        return "Numbers";
    }
}
