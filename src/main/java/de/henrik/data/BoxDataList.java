package de.henrik.data;

import javax.swing.text.Position;
import java.util.*;

public class BoxDataList extends ArrayList<RectangleData> implements DataStructure<RectangleData> {

    private final int LENGTH_OF_BOX;
    private final int REC_MIN_SIZE;
    private final int MAX_RECS_PER_BOX;

    private final ArrayList<BoxData> boxDataList;

    public BoxDataList(int lengthOfBox, int recMinSize) {
        this.LENGTH_OF_BOX = lengthOfBox;
        this.REC_MIN_SIZE = recMinSize;
        this.MAX_RECS_PER_BOX = (int) Math.pow((double) LENGTH_OF_BOX / REC_MIN_SIZE, 2);
        this.boxDataList = new ArrayList<>();
    }

    public void calculateBoxes() {
        this.boxDataList.clear();
        int boxPointer = 0;
        RectangleData firstFreePosition;
        for (var rec : this) {
            if (boxDataList.isEmpty()) {
                boxDataList.add(new BoxData(LENGTH_OF_BOX, REC_MIN_SIZE));
            }
            if ((firstFreePosition = boxDataList.get(boxPointer).getFirstFreePosition(rec.getWidth(), rec.getHeight())) != null) {
                boxDataList.get(boxPointer).add(firstFreePosition);
            } else {
                boxDataList.add(new BoxData(LENGTH_OF_BOX, REC_MIN_SIZE));
                boxDataList.get(++boxPointer).add(rec);
            }
        }
    }


    @Override
    public BoxDataList clone() {
        var newBoxDataList = new BoxDataList(LENGTH_OF_BOX, REC_MIN_SIZE);
        newBoxDataList.addAll(this);
        return newBoxDataList;
    }


    @Override
    public boolean add(RectangleData rectangleData) {
        var t = super.add(rectangleData);
        if (t) calculateBoxes();
        return t;
    }


    @Override
    public boolean remove(Object o) {
        var t = super.remove(o);
        if (t) calculateBoxes();
        return t;
    }

    @Override
    public boolean addAll(Collection<? extends RectangleData> c) {
        var t = super.addAll(c);
        if (t) calculateBoxes();
        return t;
    }

    @Override
    public boolean addAll(int index, Collection<? extends RectangleData> c) {
        var t = super.addAll(index, c);
        if (t) calculateBoxes();
        return t;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        var t = super.removeAll(c);
        if (t) calculateBoxes();
        return t;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        var t = super.retainAll(c);
        if (t) calculateBoxes();
        return t;
    }

    @Override
    public void clear() {
        super.clear();
        boxDataList.clear();
        calculateBoxes();
    }


    @Override
    public RectangleData set(int index, RectangleData element) {
        var t = super.set(index, element);
        if (t != null) calculateBoxes();
        return t;
    }

    @Override
    public void add(int index, RectangleData element) {
        super.add(index, element);
        calculateBoxes();
    }

    @Override
    public RectangleData remove(int index) {
        var t = super.remove(index);
        if (t != null) calculateBoxes();
        return t;
    }

    public List<BoxData> getBoxes() {
        return boxDataList;
    }

    public void swap(int i, int i1) {
        var temp = this.get(i);
        this.set(i, this.get(i1));
        this.set(i1, temp);
    }
}
