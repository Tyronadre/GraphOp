package de.henrik.algorithm.geedyAlgorithm;

import de.henrik.data.BoxData;
import de.henrik.data.RectangleData;
import de.henrik.gui.Panel_InputData;
import de.henrik.gui.Panel_OutputData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * This class is a decision rule for the greedy algorithm.
 * It takes the first rectangle in the list and puts it into the first box that can fit it.
 */
public class DR_PutInFirstSorted implements DecisionRule<RectangleData> {
    private final List<BoxData> boxes;
    private final Panel_OutputData panel_outputData;
    private final Panel_InputData panel_inputData;

    private boolean first = true;

    public DR_PutInFirstSorted(Panel_InputData panel_inputData, Panel_OutputData panel_outputData) {
        this.boxes = new ArrayList<>();
        this.panel_outputData = panel_outputData;
        this.panel_inputData = panel_inputData;
    }


    @Override
    public RectangleData decide(List<RectangleData> data) {

        if (first) {
            first = false;
            data.sort(Comparator.comparingInt(RectangleData::getSize));
            Collections.reverse(data);
        }

        var rectangle = data.get(0);
        panel_inputData.removeRectangle(rectangle);

        for (var box : boxes) {
            var possiblePositions = box.getFirstFreePosition(rectangle.getWidth(), rectangle.getHeight());
            if (possiblePositions != null) {
                box.add(possiblePositions);
                return data.get(0);
            }
        }
        var box = new BoxData(panel_inputData.getBoxWidth(), panel_inputData.getRecMinSize());
        box.add(rectangle);
        boxes.add(box);
        panel_outputData.addBox(box);
        return data.get(0);
    }
}
