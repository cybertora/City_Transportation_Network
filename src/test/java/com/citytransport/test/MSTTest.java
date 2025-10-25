package com.citytransport.test;

import com.citytransport.algorithm.KruskalAlgorithm;
import com.citytransport.algorithm.PrimAlgorithm;
import com.citytransport.algorithm.UnionFind;
import com.citytransport.model.Edge;
import com.citytransport.model.Graph;
import com.citytransport.util.JsonReader;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class MSTTest {
    @Test
    void testMSTCorrectness() throws IOException {
        List<Graph> graphs = JsonReader.readGraphs("assignment_3_input.json");
        PrimAlgorithm prim = new PrimAlgorithm();
        KruskalAlgorithm kruskal = new KruskalAlgorithm();

        for (Graph graph : graphs) {
            PrimAlgorithm.MSTResult primResult = prim.findMST(graph);
            KruskalAlgorithm.MSTResult kruskalResult = kruskal.findMST(graph);

            assertEquals(primResult.totalCost, kruskalResult.totalCost,
                    "Total cost mismatch for graph " + graph.getId());


            int expectedEdges = graph.getNodes().size() - 1;
            assertEquals(expectedEdges, primResult.mstEdges.size(),
                    "Prim edge count mismatch for graph " + graph.getId());
            assertEquals(expectedEdges, kruskalResult.mstEdges.size(),
                    "Kruskal edge count mismatch for graph " + graph.getId());


            assertFalse(hasCycle(primResult.mstEdges, graph.getNodes()),
                    "Prim MST has cycle for graph " + graph.getId());
            assertFalse(hasCycle(kruskalResult.mstEdges, graph.getNodes()),
                    "Kruskal MST has cycle for graph " + graph.getId());


            assertTrue(isConnected(primResult.mstEdges, graph.getNodes()),
                    "Prim MST not connected for graph " + graph.getId());
            assertTrue(isConnected(kruskalResult.mstEdges, graph.getNodes()),
                    "Kruskal MST not connected for graph " + graph.getId());


            assertTrue(primResult.executionTimeMs >= 0, "Prim negative time for graph " + graph.getId());
            assertTrue(kruskalResult.executionTimeMs >= 0, "Kruskal negative time for graph " + graph.getId());
            assertTrue(primResult.operationsCount >= 0, "Prim negative operations for graph " + graph.getId());
            assertTrue(kruskalResult.operationsCount >= 0, "Kruskal negative operations for graph " + graph.getId());
        }
    }

    private boolean hasCycle(List<Edge> edges, List<String> nodes) {
        UnionFind uf = new UnionFind();
        for (String node : nodes) {
            uf.makeSet(node);
        }
        for (Edge edge : edges) {
            String u = edge.getFrom();
            String v = edge.getTo();
            if (uf.find(u).equals(uf.find(v))) {
                return true; // Cycle detected
            }
            uf.union(u, v);
        }
        return false;
    }

    private boolean isConnected(List<Edge> edges, List<String> nodes) {
        Map<String, List<String>> adjList = new HashMap<>();
        for (String node : nodes) {
            adjList.put(node, new ArrayList<>());
        }
        for (Edge edge : edges) {
            adjList.get(edge.getFrom()).add(edge.getTo());
            adjList.get(edge.getTo()).add(edge.getFrom());
        }

        Set<String> visited = new HashSet<>();
        dfs(adjList, nodes.get(0), visited);
        return visited.size() == nodes.size();
    }

    private void dfs(Map<String, List<String>> adjList, String node, Set<String> visited) {
        visited.add(node);
        for (String neighbor : adjList.get(node)) {
            if (!visited.contains(neighbor)) {
                dfs(adjList, neighbor, visited);
            }
        }
    }
}