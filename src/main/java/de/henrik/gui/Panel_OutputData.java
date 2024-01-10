package de.henrik.gui;

import de.henrik.data.BoxData;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;

public class Panel_OutputData extends JPanel {

    private final JPanel view;
    private final GridBagConstraints c;
    private final HashMap<Integer, JPanel> boxPanels = new HashMap<>();
    private final HashMap<Integer, Integer> boxPanelIndices = new HashMap<>();

    public Panel_OutputData() {
        view = new JPanel();
        var scrollPane = new JScrollPane(view);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
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
        if (!addBoxHelper(box)) return;
        validate();
    }

    private boolean addBoxHelper(BoxData box) {
        if (box == null) {
            return true;
        }
        if (box.getLength() * (c.gridx + 2) > view.getWidth()) {
            c.gridx = 0;
            c.gridy++;
        } else {
            c.gridx++;
        }
        boxPanels.put(box.getID(), box.getPanel());
        boxPanelIndices.put(box.getID(), boxPanels.size() - 1);
        view.add(box.getPanel(), c);
        return false;
    }

    public void reset() {
        view.removeAll();
        boxPanels.clear();
        boxPanelIndices.clear();
        c.gridx = -1;
        c.gridy = 0;
        validate();
    }


    public void removeBox(int box) {
        view.remove(boxPanels.get(box));
        boxPanels.remove(box);
        boxPanelIndices.remove(box);
    }

    public void swapBox(BoxData oldBox, BoxData newBox) {
        view.remove(boxPanels.get(oldBox.getID()));
        boxPanels.put(newBox.getID(), newBox.getPanel());
        view.add(newBox.getPanel(), boxPanelIndices.get(oldBox.getID()));
        boxPanelIndices.put(newBox.getID(), boxPanelIndices.get(oldBox.getID()));
        boxPanels.remove(oldBox.getID());
        boxPanelIndices.remove(oldBox.getID());
        validate();
    }
}
