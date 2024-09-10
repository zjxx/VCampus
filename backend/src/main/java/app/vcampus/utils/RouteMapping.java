package app.vcampus.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class RouteMapping {
    private final Map<String, Function<String, String>> routes = new HashMap<>();
    private final Map<String, BiFunction<String, String, String>> routesWithParams = new HashMap<>();

    public void registerRoute(String type, Function<String, String> handler) {
        routes.put(type, handler);
    }

    public void registerRouteWithParams(String type, BiFunction<String, String, String> handler) {
        routesWithParams.put(type, handler);
    }

    public String handleRequest(String type, String jsonData, String ipAddress) {
        Logger.log("Received request: type=" + type + ", data=" + jsonData, ipAddress);
        Function<String, String> handler = routes.get(type);
        String response;
        if (handler != null) {
            response = handler.apply(jsonData);
        } else {
            JsonObject responseMessage = new JsonObject();
            responseMessage.addProperty("message", "Unsupported request type: " + type);
            response = new Gson().toJson(responseMessage);
        }
        Logger.log("Response: " + response, ipAddress);
        return response;
    }

    public String handleRequestWithParams(String type, String jsonData, String additionalParam, String ipAddress) {
        Logger.log("Received request with params: type=" + type + ", data=" + jsonData + ", additionalParam=" + additionalParam, ipAddress);
        BiFunction<String, String, String> handler = routesWithParams.get(type);
        String response;
        if (handler != null) {
            response = handler.apply(jsonData, additionalParam);
        } else {
            JsonObject responseMessage = new JsonObject();
            responseMessage.addProperty("message", "Unsupported request type: " + type);
            response = new Gson().toJson(responseMessage);
        }
        Logger.log("Response: " + response, ipAddress);
        return response;
    }
}