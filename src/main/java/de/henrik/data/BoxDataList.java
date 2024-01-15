package de.henrik.data;

import java.util.*;

/**
 * This class represents a list of boxes.
 */
public class BoxDataList implements DataStructure<RectangleData> {

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

    @Override
    public BoxDataList copy() {
        var copy = new BoxDataList(LENGTH_OF_BOX, REC_MIN_SIZE);
        this.boxDataList.forEach(box -> copy.boxDataList.add(box.copy()));
        return copy;
    }

    public List<BoxData> getBoxes() {
        return boxDataList;
    }

    /**
     * Takes the rectangle from box1 at index1 and puts it into box2 at index2.
     * If box1 is empty after the shift, it will be removed.
     * If box2 cannot fit all rectangles after the shift, subsequent rectangles will be filled in other boxes starting from the first one.
     * <p>
     * <p>
     * the returned map contains the boxes in the following order:
     * <ul>
     * <li>1. the box where the rectangle was removed</li>
     * <li>2. the box where the rectangle was added</li>
     * <li>3 and following: the boxes that have been added because of overfill</li>
     * </ul>
     *
     * @param box1   box from which the rectangle will be taken
     * @param box2   box where the rectangle will be put
     * @param index1 index of rectangle that will be shifted
     * @param index2 index where the rectangle will be shifted to
     * @return the boxes that have been changed as they were before the shift
     */
    public List<HashMap<Integer, BoxData>> shift(int box1, int box2, int index1, int index2) {
        if (box1 == box2 && (index2 == index1 || index2 == index1 + 1 || index2 == index1 - 1))
            return null;
        if (box1 == box2 && index2 >= index1) index2--;
        var changedBoxes = new HashMap<Integer, BoxData>();
        var removedBoxes = new HashMap<Integer, BoxData>();
        var addedBoxes = new HashMap<Integer, BoxData>();

        BoxData boxData1 = boxDataList.get(box1);
        BoxData boxData2 = boxDataList.get(box2);
        changedBoxes.put(boxData1.getID(), boxData1.copy());
        if (box1 != box2) {
            changedBoxes.put(boxData2.getID(), boxData2.copy());
        }
        var rec = boxData1.remove(index1);
        var overfill = boxData1.recalculateLayoutAndReturnOverfill();
        index2 = Math.min(boxData1.getRectangles().size(), index2);
        if (index2 == boxData2.getRectangles().size()) boxData2.getRectangles().add(rec);
        else boxData2.getRectangles().add(index2, rec);
        overfill.addAll(boxData2.recalculateLayoutAndReturnOverfill());
        if (boxData1.getRectangles().isEmpty()) {
            remove(box1);
            removedBoxes.put(boxData1.getID(), changedBoxes.remove(boxData1.getID()));
        }
        var boxDataListSize = boxDataList.size();
        for (RectangleData rec2 : overfill) {
            add(rec2, boxDataListSize);
            addedBoxes.put(rec2.getBoxData().getID(), rec2.getBoxData());
        }

        return List.of(removedBoxes, changedBoxes, addedBoxes);
    }

    /**
     * Removes the box at the specified index.
     *
     * @param index the index of the box
     */
    private void remove(int index) {
        boxDataList.remove(index);
    }

    /**
     * Adds a rectangle to the index at the specified index.
     * If there is no box at the given index, it will create empty boxes until the index is reached.
     * If there is no space at the given index it will try the next
     *
     * @param rectangleData the rectangle to add
     * @param box           the index of the box
     */
    private void add(RectangleData rectangleData, int box) {
        while (boxDataList.size() <= box) {
            boxDataList.add(new BoxData(LENGTH_OF_BOX, REC_MIN_SIZE));
        }
        if (!boxDataList.get(box).addToFirstFreePosition(rectangleData)) {
            add(rectangleData, box + 1);
        }
    }

    @Override
    public void shuffel(long seed) {
        Random r = new Random(seed);
        for (var box : boxDataList) {
            Collections.shuffle(box.getRectangles(), r);
            for (var rec : box.getRectangles())
                if (r.nextBoolean()) rec.flip();
        }
        Collections.shuffle(boxDataList, new Random(seed));
    }

    public int getNumberOfRectangles(int index) {
        if (index >= boxDataList.size()) return 0;
        return boxDataList.get(index).getRectangles().size();
    }

    public int getNumberOfBoxes() {
        return boxDataList.size();
    }

    /**
     * Adds all given rectangles into this data.
     * It will add each to a single box
     *
     * @param data the rectangles to add
     */
    public void addAll(List<RectangleData> data) {
        for (int i = 0; i < data.size(); i++)
            add(data.get(i), i);
    }

    public BoxData getBoxWithID(Integer id) {
        for (var box : boxDataList) {
            if (box.getID() == id) return box;
        }
        return null;
    }

    public void addBox(BoxData value) {
        boxDataList.add(value);
    }

    public void removeBox(Integer key) {
        boxDataList.removeIf(box -> box.getID() == key);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BoxDataList that = (BoxDataList) o;

        if (LENGTH_OF_BOX != that.LENGTH_OF_BOX) return false;
        if (REC_MIN_SIZE != that.REC_MIN_SIZE) return false;
        if (MAX_RECS_PER_BOX != that.MAX_RECS_PER_BOX) return false;
        return boxDataList.equals(that.boxDataList);
    }

    @Override
    public int hashCode() {
        int result = LENGTH_OF_BOX;
        result = 31 * result + REC_MIN_SIZE;
        result = 31 * result + MAX_RECS_PER_BOX;
        result = 31 * result + boxDataList.hashCode();
        return result;
    }

    /**
     * Adds the rectangle to the next box. Returns a box if a new Box was created
     *
     * @param rec     the rectangle to add
     * @param lastBox the last box the rectangle was added to
     * @return the new box if a new box was created, {@code null} otherwise
     */
    public BoxData addToNextBox(RectangleData rec, BoxData lastBox) {
        var index = boxDataList.indexOf(lastBox);
        if (index == boxDataList.size() - 1) {
            var newBox = new BoxData(LENGTH_OF_BOX, REC_MIN_SIZE);
            boxDataList.add(newBox);
            newBox.addForce(rec);
            return newBox;
        }
        var newBox = boxDataList.get(index + 1);
        newBox.addForce(rec);
        return null;
    }

    record ShiftOperation(int boxID, int boxData, ShiftOperationType type) {
        public enum ShiftOperationType {
            REMOVE, ADD, CHANGE
        }
    }
}
