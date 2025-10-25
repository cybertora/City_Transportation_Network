package com.citytransport.util;

import com.citytransport.model.Graph;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class JsonReader {
    public static List<Graph> readGraphs(String filePath) throws IOException {
        Gson gson = new Gson();
        JsonObject json = gson.fromJson(new FileReader(filePath), JsonObject.class);
        return gson.fromJson(json.get("graphs"), new TypeToken<List<Graph>>(){}.getType());
    }

    private static class JsonObject {
        List<Graph> graphs;
    }
}