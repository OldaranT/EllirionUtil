package com.ellirion.util.model.graph;

public interface IEdge<TData> {

    /**
     * Gets the first vertex of this edge.
     * @return The first vertex
     */
    IVertex<TData> getA();

    /**
     * Gets the second vertex of this edge.
     * @return The second vertex
     */
    IVertex<TData> getB();

    /**
     * Checks whether this edge involves vertex {@code vert}.
     * @param vert The vertex to check
     * @return Whether this vertex is involved in this edge
     */
    boolean involves(IVertex<TData> vert);

    /**
     * Checks whether this edge involves a vertex containing data
     * {@code data}, and returns it if found.
     * @param data The data
     * @return The vertex containing said data, if found.
     */
    boolean involves(TData data);

    /**
     * Gets the other vertex in this edge. Returns null if {@code vert} is not
     * a vertex belonging to this edge.
     * @param vert The vertex which we already have
     * @return The other vertex in this edge
     */
    IVertex<TData> other(IVertex<TData> vert);

    /**
     * Sets the weight of this edge to {@code weight}.
     * @param weight The new weight of this edge
     */
    void setWeight(double weight);

    /**
     * Gets the weight of this edge.
     * @return The weight
     */
    double getWeight();

}
