package de.henrik.algorithm.localSearchAlgorithm;

public interface NeighbourGenerator<T> {
    /**
     * Returns the next neighbour of the current data structure, without modifying the current data structure.
     * @param data the current data structure
     * @return the next neighbour of the current data structure
     */
    void nextNeighbour(T data, int Iteration);
    void revert(T data);

}
