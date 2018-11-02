package com.ellirion.util.model.graph;

public interface IGraph<TData> {

    /**
     * Find a Vertex representing the data {@code data}.
     * @param data The data the Vertex represents
     * @return The Vertex representing the data, if it exists
     */
    IVertex<TData> find(TData data);

    /**
     * Find or create a Vertex representing the data {@code data}.
     * @param data The data the Vertex represents
     * @return The Vertex representing the data
     */
    IVertex<TData> findOrCreate(TData data);

    /**
     * Adds a Vertex using {@code data} as the data.
     * @param data The data
     * @return The Vertex representing the data
     */
    IVertex<TData> add(TData data);

    /**
     * Remove the Vertex belonging to the {@code data}.
     * @param data The data the to-be-deleted Vertex represents
     */
    void remove(TData data);

    /**
     * Checks if the vertex {@code vert} is part of this graph.
     * @param vert The vertex of which to check membership
     * @return Whether this vertex is part of this graph
     */
    boolean contains(IVertex<TData> vert);

    /**
     * Checks if the data {@code data} is contained in this graph.
     * @param data The data to check
     * @return Whether the data is contained in this Graph
     */
    boolean contains(TData data);

    /**
     * Connect two data points with the given {@code weight}. The vertices representing the data are
     * created if they did not already exist.
     * @param a The first data point
     * @param b The second data point
     * @param weight The weight of this connection
     */
    void connect(TData a, TData b, double weight);

    /**
     * Disconnect two data points if they exist and are connected.
     * @param a The first data point
     * @param b The second data point
     */
    void disconnect(TData a, TData b);

    /**
     * Checks whether the Vertices representing the given data points are connected.
     * @param a The first data point
     * @param b the second data point
     * @return Whether the two data points are connected
     */
    boolean areConnected(TData a, TData b);

    /**
     * Gets an iterator over the vertices in this graph.
     * @return The iterator over the vertices
     */
    Iterable<? extends IVertex<TData>> getVertices();

    /**
     * Gets the amount of vertices in this Graph.
     * @return The amount of vertices in this Graph.
     */
    int getVertexCount();

}
