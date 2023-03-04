package com.example.laborator6.friendship;

public class Node {
    private int discoveryTime;
    private int color;
    private int predecessor;
    private final int explorationTime;

    public Node(int discoveryTime, int color, int predecessor, int explorationTime) {
        this.discoveryTime = discoveryTime;
        this.color = color;
        this.predecessor = predecessor;
        this.explorationTime = explorationTime;
    }

    public void setPredecessor(int predecessor) {
        this.predecessor = predecessor;
    }

    public int getExplorationTime() {
        return explorationTime;
    }

    public int getDiscoveryTime() {
        return discoveryTime;
    }

    public void setDiscoveryTime(int discoveryTime) {
        this.discoveryTime = discoveryTime;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
