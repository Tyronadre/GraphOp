package de.henrik.algorithm.localSearchAlgorithm;

import de.henrik.algorithm.AbstractAlgorithm;
import de.henrik.data.Data;
import de.henrik.data.DataStructure;

public class LocalSearchAlgorithm<V extends Data, T extends DataStructure<V> > extends AbstractAlgorithm {
    private final NeighbourGenerator<T> neighbourGenerator;
    private final DataStructureEvaluator<T> dataStructureEvaluator;
    T current;
    public static final int NUMBER_OF_LOOPS = 10000;

    public LocalSearchAlgorithm(long seed, T dataStructure, NeighbourGenerator<T> neighbourGenerator, DataStructureEvaluator<T> dataStructureEvaluator) {
        super(seed);
        this.neighbourGenerator = neighbourGenerator;
        this.dataStructureEvaluator = dataStructureEvaluator;
        this.current = dataStructure;
    }

    @Override
    public void runAlgorithm() {
        System.out.println("LocalSearchAlgorithm");
        progress = 0;
        fireStateChanged();
        int i = 0;
        T bestResult = (T) current.copy();
        while (i++ <= NUMBER_OF_LOOPS) {
            fireStateChanged();
            neighbourGenerator.nextNeighbour(current);
            System.out.print("\r eval: " + dataStructureEvaluator.evaluate(current) + " " + i);
            if (dataStructureEvaluator.compare(current, bestResult)) {
                bestResult = (T) current.copy();
            } else {
                neighbourGenerator.revert(current);
            }
            progress = (int) ((float) i / NUMBER_OF_LOOPS * 100);
            checkPause();
        }
        current = bestResult;
        progress = 100;
        fireStateChanged();
    }

    public DataStructure<V> getSolution() {
        return current;
    }
}
