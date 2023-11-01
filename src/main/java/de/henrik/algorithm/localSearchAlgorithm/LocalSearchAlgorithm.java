package de.henrik.algorithm.localSearchAlgorithm;

import de.henrik.algorithm.AbstractAlgorithm;

public class LocalSearchAlgorithm extends AbstractAlgorithm {
    public LocalSearchAlgorithm(long seed) {
        super(seed);
    }

    @Override
    protected void fireStateChanged() {

    }

    @Override
    public void runAlgorithm() {
        System.out.println("LocalSearchAlgorithm");
    }
}
