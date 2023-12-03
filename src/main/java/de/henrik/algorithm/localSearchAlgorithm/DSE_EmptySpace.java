package de.henrik.algorithm.localSearchAlgorithm;

import de.henrik.data.BoxData;
import de.henrik.data.BoxDataList;
import de.henrik.data.RectangleData;

import java.util.ArrayList;

public class DSE_EmptySpace implements DataStructureEvaluator<RectangleData, BoxDataList>{
    private final int boxLength;
    private final int minRecLength;

    public DSE_EmptySpace(int boxLength, int minRecLength) {
        this.boxLength = boxLength;
        this.minRecLength = minRecLength;
    }

    @Override
    public float evaluate(BoxDataList dataStructure) {
        float score = 0;
        for (BoxData box: dataStructure.getBoxes()){
            double filledSpace = box.getPercentageFilled();
            if (filledSpace > 0.9) {
                score += 1;
            } else if (filledSpace > 0.8) {
                score += 0.8;
            } else if (filledSpace > 0.7) {
                score += 0.6;
            } else if (filledSpace > 0.5) {
                score += 0.25;
            }
        }
        return score / dataStructure.size();
    }
}
