package de.henrik.algorithm.localSearchAlgorithm;

import de.henrik.data.BoxDataList;
import de.henrik.data.DataStructure;
import de.henrik.data.RectangleData;

/**
 * Überlappungen teilweise zulassen: Die geometriebasierte Nachbarschaft wird angepasst auf die Situation, dass Rechtecke sich zu einem gewissen Prozentsatz
 * überlappen dürfen. Die Überlappung zweier Rechtecke ist dabei die gemeinsame
 * Fläche geteilt durch das Maximum der beiden Rechteckflächen. Dieser Prozentsatz ist zu Beginn 100 (so dass eine Optimallösung einfach zu finden ist). Im Laufe der Zeit reduziert sich der Prozentsatz, und Verletzungen werden hart in der
 * Zielfunktion bestraft. Am Ende müssen Sie natürlich dafür sorgen, dass schlussendlich eine garantiert überlappungsfreie Lösung entsteht.
 *
 *
 */
public class NG_Overlapping implements NeighbourGenerator<BoxDataList>{


    @Override
    public void nextNeighbour(BoxDataList data) {

    }

    @Override
    public void revert(BoxDataList data) {

    }
}
