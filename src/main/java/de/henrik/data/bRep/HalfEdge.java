package de.henrik.data.bRep;

public class HalfEdge {
    Edge parentEdge;
    Vertex startVertex;
    HalfEdge nextEdge;
    HalfEdge prevEdge;
    Loop parentLoop;
}
