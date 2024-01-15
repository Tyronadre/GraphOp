package de.henrik.algorithm.geedyAlgorithm;

import java.util.List;

public interface DecisionRule <T> {
    T decide(List<T> data);

}
