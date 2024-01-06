package de.henrik.data;

import java.util.*;
import java.util.Collection;

public interface DataStructure<T extends Data> {
    void shuffel(long seed);

    DataStructure<T> copy();
}
