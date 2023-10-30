package de.henrik.gui;

import javax.swing.*;
import java.awt.*;

public class DefaultValueTextField extends JTextField {
    private final String hint;

    public DefaultValueTextField(String hint, int columns) {
        this.hint = hint;
        setColumns(columns);
        addPropertyChangeListener(evt -> {
            System.out.println(evt);
        });
    }

    public String getTextOrDefault() {
        var written = super.getText();
        if (written.isEmpty()) return hint;
        return written;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (getText().isEmpty()) {
            int h = getHeight();
            ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            Insets ins = getInsets();
            FontMetrics fm = g.getFontMetrics();
            int c0 = getBackground().getRGB();
            int c1 = getForeground().getRGB();
            int m = 0xfefefefe;
            int c2 = ((c0 & m) >>> 1) + ((c1 & m) >>> 1);
            g.setColor(new Color(c2, true));
            g.drawString(hint, ins.left, h / 2 + fm.getAscent() / 2 - 2);
        }
    }

    @Override
    public void revalidate() {
        super.revalidate();
    }

    @Override
    public void validate() {
        super.validate();
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
    }

    @Override
    public void setSize(Dimension d) {
        super.setSize(d);
    }
}
