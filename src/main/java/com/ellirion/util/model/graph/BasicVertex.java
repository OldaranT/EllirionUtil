package com.ellirion.util.model.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class BasicVertex<TData> implements IVertex<TData> {

    private TData data;
    private List<IEdge<TData>> edges;

    /**
     * Construct a BasicVertex around the data {@code data}.
     * @param data The data to wrap around
     */
    public BasicVertex(final TData data) {
        this.data = data;
        this.edges = new ArrayList<>();
    }

    @Override
    public TData getData() {
        return data;
    }

    @Override
    public IEdge<TData> findEdge(IVertex<TData> vert) {
        for (IEdge<TData> edge : edges) {
            if (edge.involves(vert)) {
                return edge;
            }
        }
        return null;
    }

    @Override
    public IEdge<TData> findEdge(TData data) {
        for (IEdge<TData> edge : edges) {
            if (edge.involves(data)) {
                return edge;
            }
        }
        return null;
    }

    @Override
    public void connect(IVertex<TData> vert, double weight) {
        IEdge<TData> edge = findEdge(vert);
        if (edge != null) {
            edge.setWeight(Math.min(edge.getWeight(), weight));
            return;
        }
        edge = new BasicEdge<TData>(this, vert, weight);
        edges.add(edge);
        vert.connect(edge);
    }

    @Override
    public void connect(IEdge<TData> edge) {
        if (edge.involves(this) && !edges.contains(edge)) {
            edges.add(edge);
        }
    }

    @Override
    public void disconnect(IVertex<TData> vert) {
        Iterator<IEdge<TData>> iter = edges.iterator();
        while (iter.hasNext()) {
            IEdge<TData> edge = iter.next();
            if (edge.involves(vert)) {
                iter.remove();
                vert.disconnect(edge);
                return;
            }
        }
    }

    @Override
    public void disconnect(IEdge<TData> edge) {
        edges.remove(edge);
    }

    @Override
    public boolean isConnectedTo(IVertex<TData> vert) {
        for (IEdge<TData> edge : edges) {
            if (edge.involves(vert)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isConnectedTo(TData data) {
        for (IEdge<TData> edge : edges) {
            IVertex<TData> vert = edge.other(this);
            if (vert != null && vert.getData().equals(data)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public double getWeightTo(IVertex<TData> vert) {
        for (IEdge<TData> edge : edges) {
            if (edge.involves(vert)) {
                return edge.getWeight();
            }
        }
        return -1;
    }

    @Override
    public double getWeightTo(TData data) {
        for (IEdge<TData> edge : edges) {
            if (edge.involves(data)) {
                return edge.getWeight();
            }
        }
        return -1;
    }

    @Override
    public Iterable<IEdge<TData>> getEdges() {
        return edges;
    }

    @Override
    public Iterable<IVertex<TData>> getAdjacents() {
        return edges.stream()
                .map(edge -> edge.other(this))
                .collect(Collectors.toList());
    }

    @Override
    public int getEdgeCount() {
        return edges.size();
    }

}
