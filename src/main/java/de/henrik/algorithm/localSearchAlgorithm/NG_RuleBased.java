package de.henrik.algorithm.localSearchAlgorithm;

import de.henrik.data.BoxData;
import de.henrik.data.BoxDataList;
import de.henrik.data.RectangleData;
import de.henrik.gui.Panel_InputData;
import de.henrik.gui.Panel_OutputData;

import java.util.Random;

public class NG_RuleBased implements NeighbourGenerator<RectangleData, BoxDataList> {
    private static final int NUMBER_OF_PERMUTATIONS = 1;
    private final Random random;
    private final Panel_InputData panel_inputData;
    private final Panel_OutputData panel_outputData;

    public NG_RuleBased(long seed, Panel_InputData panel_inputData, Panel_OutputData panel_outputData) {
        this.random = new Random(seed);
        this.panel_inputData = panel_inputData;
        this.panel_outputData = panel_outputData;
    }


    @Override

    public BoxDataList getNextNeighbour(BoxDataList currentData) {
        BoxDataList newBoxDataList = currentData.clone();
        for (int i = 0; i < NUMBER_OF_PERMUTATIONS; i++) {
            newBoxDataList.swap(random.nextInt(newBoxDataList.size()), random.nextInt(newBoxDataList.size()));
        }
        newBoxDataList.calculateBoxes();
        panel_outputData.reset();
        panel_outputData.addBoxes(newBoxDataList.getBoxes());
        return newBoxDataList;
    }


}
