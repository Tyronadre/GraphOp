package de.henrik.data;

public interface DataStructure <T> {

    /**
     * Adds a new data unit to this data structure
     * @param data the new data to add
     */
    void add(T data);

    int evaluate();
}
