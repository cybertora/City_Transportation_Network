package com.citytransport.util;

import com.citytransport.algorithm.KruskalAlgorithm;
import com.citytransport.algorithm.PrimAlgorithm;
import com.citytransport.model.Edge;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonWriter {
    public static class Result {
        public int graph_id;
        public InputStats input_stats;
        public AlgorithmResult prim;
        public AlgorithmResult kruskal;

        public Result(int graph_id, InputStats input_stats, AlgorithmResult prim, AlgorithmResult kruskal) {
            this.graph_id = graph_id;
            this.input_stats = input_stats;
            this.prim = prim;
            this.kruskal = kruskal;
        }
    }

    public static class InputStats {
        public int vertices;
        public int edges;

        public InputStats(int vertices, int edges) {
            this.vertices = vertices;
            this.edges = edges;
        }
    }

    public static class AlgorithmResult {
        public List<Edge> mst_edges;
        public int total_cost;
        public int operations_count;
        public double execution_time_ms;

        public AlgorithmResult(List<Edge> mst_edges, int total_cost, int operations_count, double execution_time_ms) {
            this.mst_edges = mst_edges;
            this.total_cost = total_cost;
            this.operations_count = operations_count;
            this.execution_time_ms = execution_time_ms;
        }
    }

    public static void writeResults(String filePath, List<Result> results) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(new OutputWrapper(results), writer);
        }
    }

    private static class OutputWrapper {
        List<Result> results;

        OutputWrapper(List<Result> results) {
            this.results = results;
        }
    }
}