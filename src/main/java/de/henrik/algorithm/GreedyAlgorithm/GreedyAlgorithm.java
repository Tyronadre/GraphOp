package de.henrik.algorithm.GreedyAlgorithm;

import de.henrik.algorithm.AbstractAlgorithm;
import de.henrik.data.Data;
import de.henrik.generator.Generator;

import java.util.Comparator;
import java.util.List;

public class GreedyAlgorithm<T extends Data> extends AbstractAlgorithm {
    private final DecisionRule<T> decisionRule;
    private final List<Generator<T>> data;

    protected GreedyAlgorithm(long seed, List<Generator<T>> data, DecisionRule<T> decisionRule) {
        super(seed);
        this.data = data;
        this.decisionRule = decisionRule;
    }

    @Override
    public void runAlgorithm() {

    }
}
