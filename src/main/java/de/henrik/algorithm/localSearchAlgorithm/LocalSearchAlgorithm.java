package de.henrik.algorithm.localSearchAlgorithm;

import de.henrik.algorithm.AbstractAlgorithm;
import de.henrik.data.Data;
import de.henrik.data.DataStructure;

public class LocalSearchAlgorithm<V extends Data, T extends DataStructure<V>> extends AbstractAlgorithm {
    private final NeighbourGenerator<V,T> neighbourGenerator;
    private final DataStructureEvaluator<V, T> dataStructureEvaluator;
    T current;

    public LocalSearchAlgorithm(long seed, T dataStructur) {
        super(seed);
        neighbourGenerator = null;
        dataStructureEvaluator = null;
    }

    @Override
    protected void fireStateChanged() {

    }

    @Override
    public void runAlgorithm() {
        System.out.println("LocalSearchAlgorithm");
        while (true) {
            var neighbor = neighbourGenerator.getRandomNeighbor();
            if (dataStructureEvaluator.evaluate(neighbor) >= dataStructureEvaluator.evaluate(current)) {
                current = neighbor;
            } else {
                break;
            }
        }
    }
}
