package de.henrik.gui.extraComponents;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

public class ProgressButton extends JButton {
    private float progress;
    private final String buttonText;
    private final DecimalFormat df = new DecimalFormat("0.00");


    public ProgressButton(String text) {
        super(text);
        buttonText = text;
    }

    public void reset() {
        setEnabled(true);
        setText(buttonText);
        progress = 0;
        repaint();
    }

    public void setProgress(float progress) {
        if (progress < 0 || progress > 100)
            throw new IllegalArgumentException("Progress must be between 0 and 100");
        if (this.progress == progress || progress == 0)
            return;
        if (this.isEnabled())
            this.setEnabled(false);
        this.progress = progress;
        setText(df.format(progress) + "%");
        repaint();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(20, 20, 255, 100));
        int width = (int) (getWidth() * progress / 100.0);
        g.fillRect(0, 0, width, getHeight());
    }
}
