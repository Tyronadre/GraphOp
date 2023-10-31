package de.henrik.algorithm.GreedyAlgorithm;

import de.henrik.data.BoxData;
import de.henrik.data.RectangleData;
import de.henrik.gui.Panel_InputData;
import de.henrik.gui.Panel_OutputData;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is a decision rule for the greedy algorithm.
 * It takes the first rectangle in the list and puts it into the first box that can fit it.
 */
public class DR_PutInFirst implements DecisionRule<RectangleData> {
    List<BoxData> boxes;
    public final int BOX_WIDTH;
    private final Panel_OutputData panel_outputData;
    private final Panel_InputData panel_inputData;

    public DR_PutInFirst(int boxWidth, Panel_InputData panel_inputData, Panel_OutputData panel_outputData) {
        this.boxes = new ArrayList<>();
        this.BOX_WIDTH = boxWidth;
        this.panel_outputData = panel_outputData;
        this.panel_inputData = panel_inputData;
    }


    @Override
    public RectangleData decide(List<RectangleData> data) {

//        if (first) {
//            first = false;
//            data.sort(Comparator.comparingInt(RectangleData::getSize));
//            Collections.reverse(data);
//        }

        var rectangle = data.get(0);
        panel_inputData.removeRectangle(rectangle);

        for (var box : boxes) {
            var possiblePositions = box.getFirstFreePosition(rectangle.getWidth(), rectangle.getHeight());
            if (possiblePositions != null) {
                box.add(possiblePositions);
                return data.get(0);
            }
        }
        var box = new BoxData(BOX_WIDTH);
        box.add(rectangle);
        boxes.add(box);
        panel_outputData.addBox(box);
        return data.get(0);
    }
}
