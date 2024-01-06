package de.henrik.algorithm.localSearchAlgorithm;

import de.henrik.data.Data;
import de.henrik.data.DataStructure;

public interface NeighbourGenerator<T> {
    /**
     * Returns the next neighbour of the current data structure, without modifying the current data structure.
     * @param data the current data structure
     * @return the next neighbour of the current data structure
     */
    void nextNeighbour(T data);
    void revert(T data);

}
