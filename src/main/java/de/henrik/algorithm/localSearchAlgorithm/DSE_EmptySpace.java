package de.henrik.algorithm.localSearchAlgorithm;

import de.henrik.data.BoxDataList;

import java.util.function.Function;

public class DSE_EmptySpace implements DataStructureEvaluator<BoxDataList> {
    private final int boxLength;
    private final int minRecLength;
    private final Function<Double, Double> evaluationFunction;

    public DSE_EmptySpace(int boxLength, int minRecLength) {
        this.boxLength = boxLength;
        this.minRecLength = minRecLength;
//        this.evaluationFunction = (x) -> 1 / (1 + 4.5 * Math.exp(-9.5 * x + 4));
        this.evaluationFunction = (x) -> Math.pow(x, 2);
    }


    @Override
    public double evaluate(BoxDataList dataStructure) {
        return dataStructure.getBoxes().stream().mapToDouble(b -> evaluationFunction.apply(b.getPercentageFilled())).sum() / dataStructure.getNumberOfBoxes();
    }

    @Override
    public boolean compare(BoxDataList neighbor, BoxDataList current) {
        if (neighbor.getNumberOfBoxes() < current.getNumberOfBoxes()) {
            return true;
        } else if (neighbor.getNumberOfBoxes() > current.getNumberOfBoxes()) {
            return false;
        } else {
            return evaluate(neighbor) > evaluate(current);
        }
    }
}
