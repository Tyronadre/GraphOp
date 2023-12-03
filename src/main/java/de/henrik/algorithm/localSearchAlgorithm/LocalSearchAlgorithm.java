package de.henrik.algorithm.localSearchAlgorithm;

import de.henrik.algorithm.AbstractAlgorithm;
import de.henrik.data.Data;
import de.henrik.data.DataStructure;

public class LocalSearchAlgorithm<V extends Data, T extends DataStructure<V>> extends AbstractAlgorithm {
    private final NeighbourGenerator<V, T> neighbourGenerator;
    private final DataStructureEvaluator<V, T> dataStructureEvaluator;
    T current;
    public static final int NUMBER_OF_LOOPS = 1000;

    public LocalSearchAlgorithm(long seed, T dataStructure, NeighbourGenerator<V, T> neighbourGenerator, DataStructureEvaluator<V, T> dataStructureEvaluator) {
        super(seed);
        this.neighbourGenerator = neighbourGenerator;
        this.dataStructureEvaluator = dataStructureEvaluator;
        this.current = dataStructure;

    }

    @Override
    protected void fireStateChanged() {

    }

    @Override
    public void runAlgorithm() {
        System.out.println("LocalSearchAlgorithm");
        int i = 0;
        while (i++ <= NUMBER_OF_LOOPS) {
            var neighbor = neighbourGenerator.getNextNeighbour(current);
            System.out.println(dataStructureEvaluator.evaluate(neighbor));
            if (dataStructureEvaluator.evaluate(neighbor) >= dataStructureEvaluator.evaluate(current)) {
                current = neighbor;
                fireStateChanged();
            } else {

            }
        }
    }
}
