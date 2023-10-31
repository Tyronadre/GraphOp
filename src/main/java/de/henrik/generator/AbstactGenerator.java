package de.henrik.generator;

import de.henrik.data.Data;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstactGenerator<T extends Data> implements Generator<T> {
    protected long seed;
    List<T> generatedData;

    AbstactGenerator (long seed) {
        this.seed = seed;
        generatedData = new ArrayList<>();
    }

    @Override
    public List<T> getData() {
        if (generatedData != null)
            return generatedData;
        else
            throw new RuntimeException("No data generated yet");
    }

    @Override
    public void reset() {
        generatedData = new ArrayList<>();
    }
}
