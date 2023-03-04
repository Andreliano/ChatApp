package com.example.laborator6.friendship;

import com.example.laborator6.domain.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Component<ID, E extends Entity<ID>> {

    private final int numberOfEntities;

    private final Map<ID, E> entities;
    private int time = 0;
    private final Node[] vertices;
    private final Boolean[][] matrix;

    public Component(int numberOfEntities, Boolean[][] matrix, Map<ID, E> entities) {
        this.numberOfEntities = numberOfEntities;
        this.matrix = matrix;
        this.entities = entities;
        this.vertices = new Node[numberOfEntities];
        for (int i = 0; i < numberOfEntities; i++) {
            this.vertices[i] = new Node(0, 0, 0, 0);
        }

    }

    public int Dfs1() {
        int i;
        int numberOfComponents = 0;
        for (i = 0; i < numberOfEntities; i++) {
            vertices[i].setColor(0);
            vertices[i].setPredecessor(-1);
        }

        int timeAux;

        for (i = 0; i < numberOfEntities; i++) {
            timeAux = time + 1;
            if (vertices[i].getColor() == 0) {
                DfsVisit(i);
            }
            if (time != timeAux - 1) {
                numberOfComponents++;
            }
        }

        return numberOfComponents;
    }

    public List<List<E>> Dfs2() {
        int i, j;
        int ind;
        List<List<E>> communities = new ArrayList<>();

        for (i = 0; i < numberOfEntities; i++) {
            vertices[i].setColor(0);
            vertices[i].setPredecessor(-1);
        }

        int timeAux;

        for (i = 0; i < numberOfEntities; i++) {
            timeAux = time + 1;
            if (vertices[i].getColor() == 0) {
                DfsVisit(i);
            }
            if (time != timeAux - 1) {
                List<E> members = new ArrayList<>();
                for (j = 0; j < numberOfEntities; j++) {
                    ind = 0;
                    if (vertices[j].getDiscoveryTime() >= timeAux && vertices[j].getExplorationTime() <= time) {
                        for (Map.Entry<ID, E> entry : entities.entrySet()) {
                            if (ind == j) {
                                //System.out.print(entry.getValue() + " ");
                                members.add(entry.getValue());
                            }
                            ind++;
                        }
                    }
                }
                //System.out.println();
                communities.add(members);
            }
        }

        return communities;

    }

    private void DfsVisit(int source) {
        time++;
        vertices[source].setDiscoveryTime(time);
        vertices[source].setColor(1);
        for (int v = 0; v < numberOfEntities; v++) {
            if (matrix[source][v] && vertices[v].getColor() == 0) {
                vertices[v].setPredecessor(source);
                DfsVisit(v);
            }
        }
    }


}
