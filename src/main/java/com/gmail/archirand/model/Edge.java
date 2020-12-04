package com.gmail.archirand.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Edge {

    @EqualsAndHashCode.Include
    private Integer weight;
    private boolean processed;
    private List<Node> nodes = new ArrayList<>();

    public Edge(Integer weight) {
        this.weight = weight;
    }

}
