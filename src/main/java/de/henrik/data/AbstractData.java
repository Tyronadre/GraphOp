package de.henrik.data;

public abstract class AbstractData implements Data {
    protected static int IDCounter = 1;
    protected final int ID = IDCounter++;

    public int getID() {
        return ID;
    }
}
