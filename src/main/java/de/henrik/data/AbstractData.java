package de.henrik.data;

public abstract class AbstractData implements Data {
    protected static int IDCounter = 0;
    protected final int ID = IDCounter++;
}
