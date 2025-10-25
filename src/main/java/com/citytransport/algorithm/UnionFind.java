package com.citytransport.algorithm;

import java.util.HashMap;
import java.util.Map;

public class UnionFind {
    private Map<String, String> parent;
    private Map<String, Integer> rank;
    private int operations;

    public UnionFind() {
        parent = new HashMap<>();
        rank = new HashMap<>();
        operations = 0;
    }

    public void makeSet(String vertex) {
        parent.put(vertex, vertex);
        rank.put(vertex, 0);
        operations++;
    }

    public String find(String vertex) {
        operations++; // Count find operation
        if (!parent.get(vertex).equals(vertex)) {
            parent.put(vertex, find(parent.get(vertex))); // Path compression
        }
        return parent.get(vertex);
    }

    public void union(String u, String v) {
        String rootU = find(u);
        String rootV = find(v);
        operations++; // Count union operation
        if (!rootU.equals(rootV)) {
            int rankU = rank.get(rootU);
            int rankV = rank.get(rootV);
            operations++; // Rank comparison
            if (rankU < rankV) {
                parent.put(rootU, rootV);
            } else if (rankU > rankV) {
                parent.put(rootV, rootU);
            } else {
                parent.put(rootV, rootU);
                rank.put(rootU, rankU + 1);
            }
        }
    }

    public int getOperations() { return operations; }
}