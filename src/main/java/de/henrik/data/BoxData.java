package de.henrik.data;

public class BoxData extends AbstractData{


    private final int length;

    public BoxData(int length) {
        this.length = length;
    }

    public int length() {
        return length;
    }

    @Override
    public int hashCode() {
        return ID;
    }

    @Override
    public String toString() {
        return "BoxData{" +
                "ID=" + ID +
                ", length=" + length +
                '}';
    }
}
