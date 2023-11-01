package de.henrik.algorithm.geedyAlgorithm;

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
    public final int BOX_WIDTH;
    private final Panel_OutputData panel_outputData;
    private final Panel_InputData panel_inputData;
    private final List<BoxData> boxes;

    public DR_PutInFirst(int boxWidth, Panel_InputData panel_inputData, Panel_OutputData panel_outputData) {
        this.boxes = new ArrayList<>();
        this.BOX_WIDTH = boxWidth;
        this.panel_outputData = panel_outputData;
        this.panel_inputData = panel_inputData;
    }


    @Override
    public RectangleData decide(List<RectangleData> data) {
        var rectangle = data.get(0);
        panel_inputData.removeRectangle(rectangle);

        for (var box : boxes) {
            var possiblePositions = box.getFirstFreePosition(rectangle.getWidth(), rectangle.getHeight());
            if (possiblePositions != null) {
                rectangle.setPosition(possiblePositions.getPosition());
                rectangle.setDimension(possiblePositions.getDimension());
                box.add(rectangle);
                return rectangle;
            }
        }
        var box = new BoxData(BOX_WIDTH);
        box.add(rectangle);
        boxes.add(box);
        panel_outputData.addBox(box);
        return data.get(0);
    }
}
