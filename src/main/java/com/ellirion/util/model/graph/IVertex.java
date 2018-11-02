package com.ellirion.util.model.graph;

public interface IVertex<TData>  {

    /**
     * Get the data represented by this vertex.
     * @return The data
     */
    TData getData();

    /**
     * Find the edge connecting this vertex to vertex {@code vert}.
     * @param vert The other vertex the edge connects to
     * @return The edge connecting this vertex to the other vertex, if any.
     */
    IEdge<TData> findEdge(IVertex<TData> vert);

    /**
     * Find the edge connecting this vertex to a vertex representing data {@code data}.
     * @param data The data the other vertex represents that the edge connects to
     * @return The edge connecting this vertex to the other vertex, if any.
     */
    IEdge<TData> findEdge(TData data);

    /**
     * Connects this vertex to the given other vertex.
     * @param vert The vertex to connect to
     * @param weight The weight of the connection
     */
    void connect(IVertex<TData> vert, double weight);

    /**
     * Connects the other half of the edge.
     * @param edge The edge to attach ourselves to
     */
    void connect(IEdge<TData> edge);

    /**
     * Disconnects this vertex from the vertex {@code v}, if they are connected.
     * @param vert The vertex to disconnect from
     */
    void disconnect(IVertex<TData> vert);

    /**
     * Disconnects this vertex from the edge {@code edge}.
     * @param edge The edge to disconnect from
     */
    void disconnect(IEdge<TData> edge);

    /**
     * Return whether this vertex is connected to vertex {@code v}.
     * @param vert The vertex we may be connected to
     * @return Whether this connection exists
     */
    boolean isConnectedTo(IVertex<TData> vert);

    /**
     * Return whether this vertex is connected to a vertex representing data {@code data}.
     * @param data The data represented by a vertex
     * @return Whether this connection exists
     */
    boolean isConnectedTo(TData data);

    /**
     * Returns the weight of a connection between this and vertex {@code v}.
     * @param vert The other vertex this vertex may be connected to
     * @return The weight, or -1 if no connection exists
     */
    double getWeightTo(IVertex<TData> vert);

    /**
     * Returns the weight of a connection between this and vertex {@code v}.
     * @param data The data which this vertex may be connected to
     * @return The weight, or -1 if no connection exists
     */
    double getWeightTo(TData data);

    /**
     * Gets all edges of this vertex.
     * @return An Itererable for iterating the edges of this vertex
     */
    Iterable<? extends IEdge<TData>> getEdges();

    /**
     * Gets all connected vertices.
     * @return An Iterable for iterating the vertices connected to this vertex
     */
    Iterable<? extends IVertex<TData>> getAdjacents();

    /**
     * Gets the amount of edges.
     * @return The amount of edges this vertex has
     */
    int getEdgeCount();

}
