package de.henrik.algorithm.geedyAlgorithm;

import de.henrik.data.BoxData;
import de.henrik.data.RectangleData;
import de.henrik.gui.Panel_InputData;
import de.henrik.gui.Panel_OutputData;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is a decision rule for the greedy algorithm.
 * It takes the biggest rectangle in the list that can still fit into the box. If there are none, it will start filling the next box.
 */
public class DR_TakeBiggest implements DecisionRule<RectangleData> {
    private final List<BoxData> boxes;

    private final Panel_OutputData panel_outputData;
    private final Panel_InputData panel_inputData;
    private BoxData lastBox;


    public DR_TakeBiggest(Panel_InputData panel_inputData, Panel_OutputData panel_outputData) {
        this.boxes = new ArrayList<>();
        this.panel_outputData = panel_outputData;
        this.panel_inputData = panel_inputData;
    }

    @Override
    public RectangleData decide(List<RectangleData> data) {

        if (lastBox != null) {
            for (var rec : data) {
                var firstPos = lastBox.getFirstFreePosition(rec.getWidth(), rec.getHeight());
                if (firstPos != null) {
                    lastBox.add(firstPos);
                    panel_inputData.removeRectangle(rec);
                    return rec;
                }
            }
        }


        var rec = data.get(0);
        panel_inputData.removeRectangle(rec);
        lastBox = new BoxData(panel_inputData.getBoxWidth(), panel_inputData.getRecMinSize());
        lastBox.add(rec);
        boxes.add(lastBox);
        panel_outputData.addBox(lastBox);
        return data.get(0);
    }
}
