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

    // 添加商品到购物车
    public String addItemToCart(String jsonData) {
        JsonObject request = gson.fromJson(jsonData, JsonObject.class);
        String userId = request.get("userId").getAsString();
        UUID itemId = UUID.fromString(request.get("itemId").getAsString());
        int quantity = request.get("quantity").getAsInt();

        DataBase db = DataBaseManager.getInstance();
        ShoppingCartItem cartItem = new ShoppingCartItem();
        cartItem.setUserId(userId);
        cartItem.setItemId(itemId);
        cartItem.setQuantity(quantity);
        db.persist(cartItem);

        JsonObject response = new JsonObject();
        response.addProperty("status", "success");
        return gson.toJson(response);
    }

    // 从购物车移除商品
    public String removeItemFromCart(String jsonData) {
        JsonObject request = gson.fromJson(jsonData, JsonObject.class);
        String userId = request.get("userId").getAsString();
        UUID itemId = UUID.fromString(request.get("itemId").getAsString());

        DataBase db = DataBaseManager.getInstance();
        List<ShoppingCartItem> cartItems = db.getWhere(ShoppingCartItem.class, "userId", userId);
        for (ShoppingCartItem cartItem : cartItems) {
            if (cartItem.getItemId().equals(itemId)) {
                db.remove(cartItem);
                break;
            }
        }

        JsonObject response = new JsonObject();
        response.addProperty("status", "success");
        return gson.toJson(response);
    }

    // 查看购物车内容
    public String viewCart(String jsonData) {
        JsonObject request = gson.fromJson(jsonData, JsonObject.class);
        String userId = request.get("userId").getAsString();

        DataBase db = DataBaseManager.getInstance();
        List<ShoppingCartItem> cartItems = db.getWhere(ShoppingCartItem.class, "userId", userId);
        JsonObject response = new JsonObject();
        JsonObject itemsObject = new JsonObject();
        for (ShoppingCartItem cartItem : cartItems) {
            StoreItem item = db.getWhere(StoreItem.class, "id", cartItem.getItemId()).get(0);
            JsonObject itemObject = gson.toJsonTree(item).getAsJsonObject();
            itemObject.addProperty("quantity", cartItem.getQuantity());
            itemsObject.add(cartItem.getItemId().toString(), itemObject);
        }
        response.add("items", itemsObject);
        response.addProperty("status", "success");
        return gson.toJson(response);
    }
}