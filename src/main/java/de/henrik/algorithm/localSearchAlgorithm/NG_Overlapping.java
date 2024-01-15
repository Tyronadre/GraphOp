package de.henrik.algorithm.localSearchAlgorithm;

import de.henrik.data.BoxData;
import de.henrik.data.BoxDataList;
import de.henrik.gui.Panel_InputData;
import de.henrik.gui.Panel_OutputData;

import java.util.ArrayList;
import java.util.Random;

/**
 * Überlappungen teilweise zulassen: Die geometriebasierte Nachbarschaft wird angepasst auf die Situation, dass Rechtecke sich zu einem gewissen Prozentsatz
 * überlappen dürfen. Die Überlappung zweier Rechtecke ist dabei die gemeinsame
 * Fläche geteilt durch das Maximum der beiden Rechteckflächen. Dieser Prozentsatz ist zu Beginn 100 (so dass eine Optimallösung einfach zu finden ist). Im Laufe der Zeit reduziert sich der Prozentsatz, und Verletzungen werden hart in der
 * Zielfunktion bestraft. Am Ende müssen Sie natürlich dafür sorgen, dass schlussendlich eine garantiert überlappungsfreie Lösung entsteht.
 */
public class NG_Overlapping implements NeighbourGenerator<BoxDataList> {
    Random random;
    Panel_InputData panelInputData;
    Panel_OutputData panelOutputData;
    double OVERLAPPING_STEP_DOWN = 0.01;
    double currentOverlap = 1.0;


    public NG_Overlapping(long seed, Panel_InputData panelInputData, Panel_OutputData panelOutputData) {
        this.random = new Random(seed);
        this.panelInputData = panelInputData;
        this.panelOutputData = panelOutputData;
    }

    @Override
    public void nextNeighbour(BoxDataList data, int iteration) {
        this.currentOverlap -= OVERLAPPING_STEP_DOWN;
        if (currentOverlap == 0.0) {
            throw new IllegalStateException("No more neighbours");
        }
        var boxes = new ArrayList<>(data.getBoxes());
        for (int boxIndex = 0; boxIndex < boxes.size(); boxIndex++) {
            var box = boxes.get(boxIndex);
            var recs = new ArrayList<>(box.getRectangles());
            for (int recIndex = 0; recIndex < recs.size(); recIndex++) {
                var rec = recs.get(recIndex);
                System.out.print("\r" + recIndex + "/" + recs.size() + " " + boxIndex + "/" + boxes.size() + " " + currentOverlap + " iteration:" + iteration);
                //while this rectangle intersects with any other rectangle in this box over the given percentage we shift it slightly
                while (box.getMaxIntersectionPercentage(rec) > currentOverlap) {
                    var shift = random.nextInt(3);
                    int moveX = switch (shift) {
                        case 0, 1 -> 1;
                        default -> 0;
                    };
                    int moveY = switch (shift) {
                        case 1, 2 -> 1;
                        default -> 0;
                    };
                    if (!rec.move(moveX, moveY)) {
                        box.remove(rec);
                        rec.setPosition(0, 0);
                        BoxData newBox;
                        if (data.getBoxes().size() == boxIndex + 1) {
                            newBox = new BoxData(box.getLength(), box.getRecMinSize());
                            data.getBoxes().add(newBox);
                        } else {
                            newBox = data.getBoxes().get(boxIndex + 1);
                        }
                        newBox.add(rec);
                        rec.setBoxData(newBox);
                    }
                }
            }
        }

    }

    @Override
    public void revert(BoxDataList data) {

    }
}
