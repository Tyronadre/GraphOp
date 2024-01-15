package de.henrik.algorithm.localSearchAlgorithm;

public interface DataStructureEvaluator<T> {

    double evaluate(T dataStructure);

    /**
     * returns true if neighbor is better than current
     */
    boolean compare(T neighbor, T current);
}
