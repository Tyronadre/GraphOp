package de.henrik.algorithm.localSearchAlgorithm;

import de.henrik.data.Data;
import de.henrik.data.DataStructure;

public interface NeighbourGenerator<V extends Data, T extends DataStructure<V>> {
    T getNextNeighbour(T currentData);

}
