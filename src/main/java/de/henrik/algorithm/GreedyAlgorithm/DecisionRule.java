package de.henrik.algorithm.GreedyAlgorithm;

import de.henrik.data.Data;

import java.util.List;

public interface DecisionRule <T extends Data> {
    T decide(List<T> data);

}
