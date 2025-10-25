package com.citytransport.algorithm;

import com.citytransport.model.Edge;
import com.citytransport.util.PerformanceTracker;
import com.citytransport.model.Graph;

import java.util.*;

public class PrimAlgorithm {
    private final PerformanceTracker tracker;

    public PrimAlgorithm() {
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

        // Build adjacency list
        Map<String, List<Edge>> adjList = new HashMap<>();
        for (String node : graph.getNodes()) {
            adjList.put(node, new ArrayList<>());
        }
        for (Edge edge : graph.getEdges()) {
            adjList.get(edge.getFrom()).add(edge);
            adjList.get(edge.getTo()).add(new Edge(edge.getTo(), edge.getFrom(), edge.getWeight()));
            tracker.incrementOperations(2); // Adding to adjacency list
        }

        List<Edge> mstEdges = new ArrayList<>();
        int totalCost = 0;
        Set<String> visited = new HashSet<>();
        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingInt(Edge::getWeight));

        // Start with the first node
        String startNode = graph.getNodes().get(0);
        visited.add(startNode);
        for (Edge edge : adjList.get(startNode)) {
            pq.offer(edge);
            tracker.incrementOperations(1); // Adding to PQ
        }

        while (!pq.isEmpty() && visited.size() < graph.getNodes().size()) {
            Edge edge = pq.poll();
            tracker.incrementOperations(1); // Poll operation
            String toNode = edge.getTo();

            if (visited.contains(toNode)) continue;

            visited.add(toNode);
            mstEdges.add(edge);
            totalCost += edge.getWeight();
            tracker.incrementOperations(3); // Check, add to MST, add weight

            for (Edge nextEdge : adjList.get(toNode)) {
                if (!visited.contains(nextEdge.getTo())) {
                    pq.offer(nextEdge);
                    tracker.incrementOperations(2); // Check and add to PQ
                }
            }
        }

        // Check if MST connects all vertices
        if (visited.size() != graph.getNodes().size()) {
            return new MSTResult(new ArrayList<>(), 0, tracker.getOperations(),
                    (System.nanoTime() - startTime) / 1_000_000.0);
        }

        double executionTimeMs = (System.nanoTime() - startTime) / 1_000_000.0;
        return new MSTResult(mstEdges, totalCost, tracker.getOperations(), executionTimeMs);
    }
}