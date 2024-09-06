package app.vcampus.controller;

import app.vcampus.domain.ShoppingCartItem;
import app.vcampus.domain.StoreItem;
import app.vcampus.utils.DataBase;
import app.vcampus.utils.DataBaseManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.UUID;

public class ShoppingCartController {
    private final Gson gson = new Gson();

    // 添加商品到购物车，传入 userId, itemId, quantity
    public String addItemToCart(String jsonData) {
       try {
           JsonObject request = gson.fromJson(jsonData, JsonObject.class);
           String userId = request.get("userId").getAsString();
           UUID itemId = UUID.fromString(request.get("itemId").getAsString());
           int quantity = request.get("quantity").getAsInt();

           DataBase db = DataBaseManager.getInstance();
           ShoppingCartItem cartItem = new ShoppingCartItem();
           cartItem.setUuid(UUID.randomUUID()); // Set the UUID
           cartItem.setUserId(userId);
           cartItem.setItemId(itemId);
           cartItem.setQuantity(quantity);
           db.persist(cartItem);

           JsonObject response = new JsonObject();
           response.addProperty("status", "success");
           return gson.toJson(response);
       }
         catch (Exception e) {
              JsonObject response = new JsonObject();
              response.addProperty("status", "failed");
              response.addProperty("reason", e.getMessage());
              return gson.toJson(response);
         }
    }


    // 从购物车移除商品，传入 userId(用户一卡通号), itemId（商品名称）, uuid（每个购物车的uuid）
    public String removeItemFromCart(String jsonData) {
        try {
            JsonObject request = gson.fromJson(jsonData, JsonObject.class);
            String userId = request.get("userId").getAsString();
            UUID itemId = UUID.fromString(request.get("itemId").getAsString());
            UUID uuid = UUID.fromString(request.get("uuid").getAsString()); // Get the UUID

            DataBase db = DataBaseManager.getInstance();
            List<ShoppingCartItem> cartItems = db.getWhere(ShoppingCartItem.class, "userId", userId);
            for (ShoppingCartItem cartItem : cartItems) {
                if (cartItem.getItemId().equals(itemId) && cartItem.getUuid().equals(uuid)) {
                    db.remove(cartItem);
                    break;
                }
            }

            JsonObject response = new JsonObject();
            response.addProperty("status", "success");
            return gson.toJson(response);
        }
        catch (Exception e) {
            JsonObject response = new JsonObject();
            response.addProperty("status", "failed");
            response.addProperty("reason", e.getMessage());
            return gson.toJson(response);
        }
    }

    // 查看购物车内容，传入 userId
    public String viewCart(String jsonData) {
        try {
            JsonObject request = gson.fromJson(jsonData, JsonObject.class);
            String userId = request.get("userId").getAsString();

            DataBase db = DataBaseManager.getInstance();
            List<ShoppingCartItem> cartItems = db.getWhere(ShoppingCartItem.class, "userId", userId);

            if (cartItems.isEmpty()) {
                JsonObject response = new JsonObject();
                response.addProperty("status", "failed");
                response.addProperty("reason", "购物车中无商品");
                return gson.toJson(response);
            }

            JsonObject itemsObject = new JsonObject();
            for (int i = 0; i < cartItems.size(); i++) {
                ShoppingCartItem cartItem = cartItems.get(i);
                StoreItem item = db.getWhere(StoreItem.class, "uuid", cartItem.getItemId()).get(0);
                JsonObject itemObject = gson.toJsonTree(item).getAsJsonObject();
                itemObject.addProperty("quantity", cartItem.getQuantity());
                itemsObject.add("item" + i, itemObject);
            }

            JsonObject response = new JsonObject();
            response.addProperty("status", "success");
            response.addProperty("length", cartItems.size());
            response.add("items", itemsObject);
            return gson.toJson(response);
        } catch (Exception e) {
            JsonObject response = new JsonObject();
            response.addProperty("status", "failed");
            response.addProperty("reason", e.getMessage());
            return gson.toJson(response);
        }
    }
}