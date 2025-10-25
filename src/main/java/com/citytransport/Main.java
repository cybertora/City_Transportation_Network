package com.citytransport;

import com.citytransport.algorithm.KruskalAlgorithm;
import com.citytransport.algorithm.PrimAlgorithm;
import com.citytransport.model.Graph;
import com.citytransport.util.JsonReader;
import com.citytransport.util.JsonWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            // Read input
            List<Graph> graphs = JsonReader.readGraphs("assignment_3_input.json");
            List<JsonWriter.Result> results = new ArrayList<>();

            PrimAlgorithm prim = new PrimAlgorithm();
            KruskalAlgorithm kruskal = new KruskalAlgorithm();

            for (Graph graph : graphs) {
                // Run Prim
                PrimAlgorithm.MSTResult primResult = prim.findMST(graph);

                // Run Kruskal
                KruskalAlgorithm.MSTResult kruskalResult = kruskal.findMST(graph);

                // Collect results
                JsonWriter.InputStats stats = new JsonWriter.InputStats(
                        graph.getNodes().size(), graph.getEdges().size());
                JsonWriter.Result result = new JsonWriter.Result(
                        graph.getId(),
                        stats,
                        new JsonWriter.AlgorithmResult(
                                primResult.mstEdges, primResult.totalCost,
                                primResult.operationsCount, primResult.executionTimeMs),
                        new JsonWriter.AlgorithmResult(
                                kruskalResult.mstEdges, kruskalResult.totalCost,
                                kruskalResult.operationsCount, kruskalResult.executionTimeMs)
                );
                results.add(result);
            }

            // Write output
            JsonWriter.writeResults("assignment_3_output.json", results);
            System.out.println("Processing complete. Output written to assignment_3_output.json");

            // Generate CSV for analysis
            generateCSV(results);

        } catch (IOException e) {
            System.err.println("Error processing JSON: " + e.getMessage());
        }
    }

    private static void generateCSV(List<JsonWriter.Result> results) throws IOException {
        try (FileWriter writer = new FileWriter("analysis.csv")) {
            writer.write("graph_id,vertices,edges,prim_cost,kruskal_cost,prim_time_ms,kruskal_time_ms,prim_ops,kruskal_ops\n");
            for (JsonWriter.Result result : results) {
                writer.write(String.format("%d,%d,%d,%d,%d,%.2f,%.2f,%d,%d\n",
                        result.graph_id,
                        result.input_stats.vertices,
                        result.input_stats.edges,
                        result.prim.total_cost,
                        result.kruskal.total_cost,
                        result.prim.execution_time_ms,
                        result.kruskal.execution_time_ms,
                        result.prim.operations_count,
                        result.kruskal.operations_count));
            }
        }
    }
}