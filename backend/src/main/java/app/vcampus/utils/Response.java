package app.vcampus.utils;

import com.google.gson.JsonObject;

public class Response {
    private JsonObject responseMessage;


    public Response(JsonObject responseMessage) {
        this.responseMessage = responseMessage;
    }

    public JsonObject getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(JsonObject responseMessage) {
        this.responseMessage = responseMessage;
    }

}