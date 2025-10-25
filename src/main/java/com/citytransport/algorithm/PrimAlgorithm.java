package com.citytransport.algorithm;

import com.citytransport.model.Edge;
import com.citytransport.util.PerformanceTracker;
import com.citytransport.model.Graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class KruskalAlgorithm {
    private final PerformanceTracker tracker;

    public KruskalAlgorithm() {
        this.tracker = new PerformanceTracker();
    }

    public static class MSTResult {
        public List<Edge> mstEdges;
        public int totalCost;
        public int operationsCount;
        public double executionTimeMs;

        public MSTResult(List<Edge> mstEdges, int totalCost, int operationsCount, double executionTimeMs) {
            this.mstEdges = mstEdges;
            this.totalCost = totalCost;
            this.operationsCount = operationsCount;
            this.executionTimeMs = executionTimeMs;
        }
    }

    public MSTResult findMST(Graph graph) {
        long startTime = System.nanoTime();
        tracker.reset();

        // Sort edges by weight
        List<Edge> edges = new ArrayList<>(graph.getEdges());
        edges.sort(Comparator.comparingInt(Edge::getWeight));
        tracker.incrementOperations(edges.size() * (int) Math.log(edges.size())); // Approximate sorting ops

        UnionFind uf = new UnionFind();
        for (String node : graph.getNodes()) {
            uf.makeSet(node);
            tracker.incrementOperations(1); // Make set
        }

        List<Edge> mstEdges = new ArrayList<>();
        int totalCost = 0;

        for (Edge edge : edges) {
            String u = edge.getFrom();
            String v = edge.getTo();
            if (!uf.find(u).equals(uf.find(v))) {
                uf.union(u, v);
                mstEdges.add(edge);
                totalCost += edge.getWeight();
                tracker.incrementOperations(3); // Find, union, add to MST
            }
        }

        // Verify MST connects all vertices
        if (mstEdges.size() != graph.getNodes().size() - 1) {
            return new MSTResult(new ArrayList<>(), 0, tracker.getOperations() + uf.getOperations(),
                    (System.nanoTime() - startTime) / 1_000_000.0);
        }

        double executionTimeMs = (System.nanoTime() - startTime) / 1_000_000.0;
        return new MSTResult(mstEdges, totalCost, tracker.getOperations() + uf.getOperations(), executionTimeMs);
    }
}