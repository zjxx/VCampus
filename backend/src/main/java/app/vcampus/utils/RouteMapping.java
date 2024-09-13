package app.vcampus.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * RouteMapping is responsible for managing the routing of requests to their respective handlers.
 * It supports both regular routes and routes with additional parameters.
 */
public class RouteMapping {
    private final Map<String, Function<String, String>> routes = new HashMap<>();
    private final Map<String, BiFunction<String, String, String>> routesWithParams = new HashMap<>();

    /**
     * Registers a route with a handler function.
     *
     * @param type the type of the request
     * @param handler the function to handle the request
     */
    public void registerRoute(String type, Function<String, String> handler) {
        routes.put(type, handler);
    }

    /**
     * Registers a route with a handler function that accepts additional parameters.
     *
     * @param type the type of the request
     * @param handler the function to handle the request with additional parameters
     */
    public void registerRouteWithParams(String type, BiFunction<String, String, String> handler) {
        routesWithParams.put(type, handler);
    }

    /**
     * Handles a request by routing it to the appropriate handler function.
     *
     * @param type the type of the request
     * @param jsonData the JSON data of the request
     * @param ipAddress the IP address of the client
     * @return the response from the handler function
     */
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

    /**
     * Handles a request with additional parameters by routing it to the appropriate handler function.
     *
     * @param type the type of the request
     * @param jsonData the JSON data of the request
     * @param additionalParam additional parameters for the request
     * @param ipAddress the IP address of the client
     * @return the response from the handler function
     */
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