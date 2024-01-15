package de.henrik.data;

import java.util.*;

public interface DataStructure<T extends Data> {
    void shuffel(long seed);

    DataStructure<T> copy();

    void addAll(List<T> data);

    List<BoxData> getBoxes();
}
