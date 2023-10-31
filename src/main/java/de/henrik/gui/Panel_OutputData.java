package de.henrik.gui;

import de.henrik.data.BoxData;

import javax.swing.*;
import java.awt.*;

public class Panel_OutputData extends JPanel {

    private final JPanel view;
    private final GridBagConstraints c;

    public Panel_OutputData() {
        view = new JPanel();
        var scrollPane = new JScrollPane(view);
//        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        view.setLayout(new GridBagLayout());
        c = new GridBagConstraints();
        c.insets = new Insets(3, 3, 3, 3);
        c.fill = GridBagConstraints.BOTH;
        c.gridx = -1;
        c.weightx = 1;
        c.weighty = 1;
        setLayout(new BorderLayout());
        add(scrollPane);
    }

    public void addBox(BoxData box) {
        if (box == null) {
            return;
        }
        if (box.getLength() * (c.gridx + 2) > view.getWidth()) {
            c.gridx = 0;
            c.gridy++;
        } else {
            c.gridx++;
        }
        view.add(box.getPanel(), c);
        validate();
    }

    public void reset() {
        view.removeAll();
        c.gridx = -1;
        c.gridy = 0;
        validate();
    }
}
