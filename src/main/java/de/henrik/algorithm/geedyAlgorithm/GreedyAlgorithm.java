package de.henrik.algorithm.geedyAlgorithm;

import de.henrik.algorithm.AbstractAlgorithm;
import de.henrik.algorithm.ProgressEvent;
import de.henrik.data.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GreedyAlgorithm<T extends Data> extends AbstractAlgorithm {
    private final DecisionRule<T> decisionRule;
    private final List<T> data;

    private final List<T> solution;
    public GreedyAlgorithm(long seed, List<T> data, DecisionRule<T> decisionRule) {
        super(seed);
        this.data = data;
        this.decisionRule = decisionRule;
        this.solution = new ArrayList<>();
    }

    @Override
    public void runAlgorithm() {
        int i = 0;
        int max = data.size();
        Collections.shuffle(data, new Random(seed));
        while (!data.isEmpty()) {
            progress = (float) i++ / max * 100;
            var t = decisionRule.decide(data);
            solution.add(t);
            data.remove(t);
            fireStateChanged();
            checkPause();
        }
        progress = 100;
        fireStateChanged();
    }

    public List<T> getSolution() {
        return solution;
    }
}
