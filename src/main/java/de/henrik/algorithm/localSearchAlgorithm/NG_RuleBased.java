package de.henrik.algorithm.localSearchAlgorithm;

import de.henrik.data.BoxData;
import de.henrik.data.BoxDataList;
import de.henrik.data.Pair;
import de.henrik.gui.Panel_InputData;
import de.henrik.gui.Panel_OutputData;

import java.util.*;

/**
 * Regelbasiert: Anstelle von zulässigen Lösungen, arbeitet die lokale Suche hier auf
 * Permutationen von Rechtecken. Analog zum Greedy-Algorithmus werden die
 * Rechtecke in der Reihenfolge der Permutation in den Boxen platziert. Als Regel
 * Die Nachbarschaft definieren Sie durch kleine Modifikationsschritte auf der Permutation. Auch hier könnte es sinnvoll sein, Rechtecke in relativ leeren Boxen
 * anderswo in der Permutation zu platzieren.
 * <p>
 * <p>
 * Aktuell machen wir permutationen für jede unterschiedliche BoxDataList. es wäre bessere eine queue zu benutzen und dabei nur neue permutationen für boxen zu machen die auch verändert wurden.
 */
public class NG_RuleBased implements NeighbourGenerator<BoxDataList> {
    private static final int NUMBER_OF_PERMUTATIONS = 1;
    private static final boolean PAINT_EVERY_STEP = true;
    private final Panel_OutputData panel_outputData;
    private final RandomPermutation randomPermutation;
    List<HashMap<Integer, BoxData>> lastChanges = null;

    public NG_RuleBased(long seed, Panel_InputData panel_inputData, Panel_OutputData panel_outputData) {
        this.panel_outputData = panel_outputData;
        this.randomPermutation = new RandomPermutation(seed);
    }

    @Override
    public void nextNeighbour(BoxDataList data, int iteration) {
        for (int i = 0; i < NUMBER_OF_PERMUTATIONS; i++) {
            var permutation = randomPermutation.next(data);
            if (permutation == null) throw new IllegalStateException("No more permutations");
            int box1 = permutation[0];
            int box2 = permutation[1];
            int index1 = permutation[2];
            int index2 = permutation[3];

            lastChanges = data.shift(box1, box2, index1, index2);

            if (PAINT_EVERY_STEP) {
                if (lastChanges == null) continue;
                if (!lastChanges.get(0).keySet().isEmpty()) {
                    for (var entry : lastChanges.get(0).entrySet()) {
                        panel_outputData.removeBox(entry.getKey());
                    }
                }
                if (!lastChanges.get(2).keySet().isEmpty()) {
                    for (var entry : lastChanges.get(2).entrySet()) {
                        panel_outputData.addBox(entry.getValue());
                    }
                }
                panel_outputData.validate();
                panel_outputData.repaint();
            }
        }
    }

    @Override
    public void revert(BoxDataList data) {
        if (lastChanges == null) return;
        for (var removedBoxEntry : lastChanges.get(0).entrySet()) {
            data.addBox(removedBoxEntry.getValue());
            if (PAINT_EVERY_STEP) panel_outputData.addBox(removedBoxEntry.getValue());
        }
        for (var changedBoxEntry : lastChanges.get(1).entrySet()) {
            data.getBoxWithID(changedBoxEntry.getKey()).loadData(changedBoxEntry.getValue(), PAINT_EVERY_STEP);
        }
        for (var addedBoxEntry : lastChanges.get(2).entrySet()) {
            data.removeBox(addedBoxEntry.getKey());
            if (PAINT_EVERY_STEP) panel_outputData.removeBox(addedBoxEntry.getKey());
        }
        lastChanges = null;
    }

    private static class RandomPermutation {
        private static HashMap<Integer, RandomBoxPermutations> dataHashToBox;
        private static HashMap<Integer, Pair<Integer, Integer>> dataHashToBoxIndices;
        private static HashMap<Pair<Integer, Pair<Integer, Integer>>, RandomRecPermutation> dataHashAndBoxDataToRec;
        private final Random random;

        RandomPermutation(long seed) {
            this.random = new Random(seed);
            dataHashToBox = new HashMap<>();
            dataHashAndBoxDataToRec = new HashMap<>();
            dataHashToBoxIndices = new HashMap<>();
        }

        /**
         * @return the next permutation or null if there is no next permutation
         */
        public int[] next(BoxDataList data) {
            //first we check if we have box indices
            Pair<Integer, Integer> boxIndices = dataHashToBoxIndices.get(data.hashCode());
            if (boxIndices != null) {
                //we have old box indices so now we check if we have any rec permutations for those
                // if we have old box indices we also have to have a recPermutation
                Pair<Integer, Integer> recIndices = dataHashAndBoxDataToRec.get(new Pair<>(data.hashCode(), boxIndices)).next();
                if (recIndices != null) {
                    return new int[]{boxIndices.first(), boxIndices.second(), recIndices.first(), recIndices.second()};
                }
                //recIndices is null so we need new Box indices
            }
            //we need new box indices
            dataHashToBox.computeIfAbsent(data.hashCode(), k -> new RandomBoxPermutations(data));
            var boxPermutations = dataHashToBox.get(data.hashCode());
            Pair<Integer, Integer> boxIndicesNew = boxPermutations.next();
            //if we have no new box indices we return null, that means we went through all permutations
            if (boxIndicesNew == null) return null;
            //we have new box indices so we need new rec indices
            dataHashToBoxIndices.put(data.hashCode(), boxIndicesNew);
            dataHashAndBoxDataToRec.computeIfAbsent(new Pair<>(data.hashCode(), boxIndicesNew), k -> new RandomRecPermutation(data, boxIndicesNew));
            var recPermutations = dataHashAndBoxDataToRec.get(new Pair<>(data.hashCode(), boxIndicesNew));
            var recIndices = recPermutations.next();
            if (recIndices == null) {
                throw new IllegalStateException("recPermutations is null");
            }
            return new int[]{boxIndicesNew.first(), boxIndicesNew.second(), recIndices.first(), recIndices.second()};

        }


        private class RandomBoxPermutations {
            int index;
            List<Pair<Integer, Integer>> permutations;

            public RandomBoxPermutations(BoxDataList data) {
                this.index = 0;
                this.permutations = createPermutations(data);
            }

            private List<Pair<Integer, Integer>> createPermutations(BoxDataList data) {
                var permutations = new ArrayList<Pair<Integer, Integer>>();
                for (int box1 = 0; box1 < data.getNumberOfBoxes(); box1++)
                    for (int box2 = 0; box2 < data.getNumberOfBoxes(); box2++)
                        if (box1 != box2)
                            permutations.add(new Pair<>(box1, box2));
                Collections.shuffle(permutations, random);
                return permutations;
            }

            /**
             * @return the next permutation or null if there is no next permutation
             */
            public Pair<Integer, Integer> next() {
                if (index >= permutations.size()) return null;
                return permutations.get(index++);
            }
        }

        private class RandomRecPermutation {
            int index;
            List<Pair<Integer, Integer>> permutations;

            public RandomRecPermutation(BoxDataList data, Pair<Integer, Integer> boxIndices) {
                this.index = 0;
                this.permutations = createPermutations(data, boxIndices);
            }

            private List<Pair<Integer, Integer>> createPermutations(BoxDataList data, Pair<Integer, Integer> boxIndices) {
                var permutations = new ArrayList<Pair<Integer, Integer>>();
                for (int index1 = 0; index1 < data.getNumberOfRectangles(boxIndices.first()); index1++)
                    for (int index2 = 0; index2 <= data.getNumberOfRectangles(boxIndices.second()); index2++)
                        permutations.add(new Pair<>(index1, index2));
                Collections.shuffle(permutations, random);
                return permutations;
            }


            /**
             * @return the next permutation or null if there is no next permutation
             */
            public Pair<Integer, Integer> next() {
                if (index >= permutations.size()) return null;
                return permutations.get(index++);
            }
        }
    }
}