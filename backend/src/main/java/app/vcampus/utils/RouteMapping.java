package app.vcampus.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import com.google.gson.Gson;

public class RouteMapping {
    private final Map<String, Function<String, String>> routes = new HashMap<>();

    public void registerRoute(String type, Function<String, String> handler) {
        routes.put(type, handler);
    }

    public String handleRequest(String type, String jsonData) {
        Function<String, String> handler = routes.get(type);
        if (handler != null) {
            return handler.apply(jsonData);
        } else {
            Response response = new Response("Unsupported request type: " + type);
            return new Gson().toJson(response);
        }
    }
}