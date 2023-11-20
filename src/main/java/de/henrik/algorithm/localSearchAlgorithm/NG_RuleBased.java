package de.henrik.algorithm.localSearchAlgorithm;

import de.henrik.data.BoxData;
import de.henrik.data.BoxDataList;
import de.henrik.data.RectangleData;

import java.util.List;

public class NG_RuleBased implements NeighbourGenerator<RectangleData, BoxDataList> {
    private List<RectangleData> listBoxData;

    @Override
    public BoxDataList getRandomNeighbor() {
        return null;
    }
}
