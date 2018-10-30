package com.ellirion.util.model.graph;

import java.util.HashMap;
import java.util.Map;

public class BasicGraph<TData> implements IGraph<TData> {

    private Map<TData, IVertex<TData>> vertices;

    /**
     * Construct a BasicGraph.
     */
    public BasicGraph() {
        this.vertices = new HashMap<>();
    }

    @Override
    public IVertex<TData> find(TData data) {
        return vertices.get(data);
    }

    @Override
    public IVertex<TData> findOrCreate(TData data) {
        IVertex<TData> vert = find(data);
        if (vert == null) {
            vert = new BasicVertex<>(data);
            vertices.put(data, vert);
        }
        return vert;
    }

    @Override
    public IVertex<TData> add(TData data) {
        return findOrCreate(data);
    }

    @Override
    public void remove(TData data) {
        IVertex<TData> vert = find(data);
        if (vert != null) {
            vertices.remove(data);

            // Iterate over all edges in this vertex
            for (IEdge<TData> edge : vert.getEdges()) {

                // Delete the edge from the other vertex
                IVertex<TData> other = edge.getA().equals(vert) ? edge.getB() : edge.getA();
                other.disconnect(edge);
            }
        }
    }

    @Override
    public boolean contains(IVertex<TData> vert) {
        return vertices.containsValue(vert);
    }

    @Override
    public boolean contains(TData data) {
        return vertices.containsKey(data);
    }

    @Override
    public void connect(TData dataA, TData dataB, double weight) {
        IVertex<TData> vertA = findOrCreate(dataA);
        IVertex<TData> vertB = findOrCreate(dataB);
        vertA.connect(vertB, weight);
    }

    @Override
    public void disconnect(TData dataA, TData dataB) {
        IVertex<TData> vertA = find(dataA);
        IVertex<TData> vertB = find(dataB);
        if (vertA != null && vertB != null) {
            vertA.disconnect(vertB);
        }
    }

    @Override
    public boolean areConnected(TData a, TData b) {
        IVertex<TData> vertA = find(a);
        IVertex<TData> vertB = find(b);
        if (vertA != null && vertB != null) {
            return vertA.isConnectedTo(vertB);
        }
        return false;
    }

    @Override
    public Iterable<IVertex<TData>> getVertices() {
        return vertices.values();
    }

    @Override
    public int getVertexCount() {
        return vertices.size();
    }

}
