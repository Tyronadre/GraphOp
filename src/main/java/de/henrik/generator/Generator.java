package de.henrik.generator;

import de.henrik.data.Data;

import java.util.List;

public interface Generator <T extends Data> {
    void generate();

    List<T> getData();

    void reset();
}
