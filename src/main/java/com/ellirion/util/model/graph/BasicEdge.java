package com.ellirion.util.model.graph;

public class BasicEdge<TData> implements IEdge<TData> {

    private IVertex<TData> vertA;
    private IVertex<TData> vertB;
    private double weight;

    /**
     * Constructs a BasicEdge between the two given vertices with the given weight.
     * @param vertA The first vertex
     * @param vertB The second vertex
     * @param weight The weight
     */
    public BasicEdge(final IVertex<TData> vertA, final IVertex<TData> vertB, final double weight) {
        this.vertA = vertA;
        this.vertB = vertB;
        this.weight = weight;
    }

    @Override
    public IVertex<TData> getA() {
        return vertA;
    }

    @Override
    public IVertex<TData> getB() {
        return vertB;
    }

    @Override
    public boolean involves(IVertex<TData> vert) {
        return vertA.equals(vert) || vertB.equals(vert);
    }

    @Override
    public boolean involves(TData data) {
        return vertA.getData().equals(data) || vertB.getData().equals(data);
    }

    @Override
    public IVertex<TData> other(IVertex<TData> vert) {
        if (vertA.equals(vert)) {
            return vertB;
        } else if (vertB.equals(vert)) {
            return vertA;
        }
        return null;
    }

    @Override
    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public double getWeight() {
        return weight;
    }

}
