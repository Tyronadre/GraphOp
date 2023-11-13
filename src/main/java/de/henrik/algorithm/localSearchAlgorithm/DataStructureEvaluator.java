package de.henrik.algorithm.localSearchAlgorithm;

import de.henrik.data.Data;
import de.henrik.data.DataStructure;

public interface DataStructureEvaluator <V extends Data, T extends DataStructure<V>> {

    int evaluate(T dataStructure);
}
