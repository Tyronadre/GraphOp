package de.henrik.algorithm.localSearchAlgorithm;

import de.henrik.data.Data;
import de.henrik.data.DataStructure;

public interface DataStructureEvaluator<T> {

    double evaluate(T dataStructure);

    /**
     * returns true if neighbor is better than current
     */
    boolean compare(T neighbor, T current);
}
