package de.henrik.algorithm.localSearchAlgorithm;

import de.henrik.data.BoxDataList;

/**
 * Geometriebasiert: Ein Nachbar lässt sich erzeugen, indem Rechtecke direkt verschoben werden, sowohl innerhalb einer Box als auch von einer Box zur anderen.
 * Im Prinzip sind Sie frei darin, wie Sie das machen. Sie sind auch frei darin, die
 * Zielfunktion so abzuändern, dass Nachbarn auch dann besser sind, wenn die eigentlich zu minimierende Zielfunktion nicht besser ist, der Nachbar aber nach
 * heuristischen Überlegungen näher an einer Verbesserung dran ist. Zum Beispiel
 * könnte es sinnvoll sein, einen Schritt zu belohnen, bei dem die Anzahl Rechtecke
 * in einer Box, in der ohnehin nur wenige Rechtecke in dieser Box sind, weiter verringert wird, auch wenn die Box damit (noch) nicht leer ist.
 *
 */
public class NG_GeometryBased implements NeighbourGenerator<BoxDataList> {


    @Override
    public void nextNeighbour(BoxDataList data, int iteration) {

    }

    @Override
    public void revert(BoxDataList data) {

    }
}
