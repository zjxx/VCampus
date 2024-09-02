package app.vcampus.controller;

import app.vcampus.domain.ShoppingCart;
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
        UUID userId = UUID.fromString(request.get("userId").getAsString());
        UUID itemId = UUID.fromString(request.get("itemId").getAsString());
        int quantity = request.get("quantity").getAsInt();

        DataBase db = DataBaseManager.getInstance();
        List<ShoppingCart> carts = db.getWhere(ShoppingCart.class, "userId", userId);
        ShoppingCart cart;
        if (carts.isEmpty()) {
            cart = new ShoppingCart();
            cart.setId(UUID.randomUUID());
            cart.setUserId(userId);
            db.persist(cart);
        } else {
            cart = carts.get(0);
        }

        ShoppingCartItem cartItem = new ShoppingCartItem();
        cartItem.setId(UUID.randomUUID());
        cartItem.setCartId(cart.getId());
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
        UUID cartItemId = UUID.fromString(request.get("cartItemId").getAsString());

        DataBase db = DataBaseManager.getInstance();
        ShoppingCartItem cartItem = db.getWhere(ShoppingCartItem.class, "id", cartItemId).get(0);
        db.remove(cartItem);

        JsonObject response = new JsonObject();
        response.addProperty("status", "success");
        return gson.toJson(response);
    }

    // 查看购物车内容
    public String viewCart(String jsonData) {
        JsonObject request = gson.fromJson(jsonData, JsonObject.class);
        UUID userId = UUID.fromString(request.get("userId").getAsString());

        DataBase db = DataBaseManager.getInstance();
        List<ShoppingCart> carts = db.getWhere(ShoppingCart.class, "userId", userId);
        JsonObject response = new JsonObject();
        if (!carts.isEmpty()) {
            ShoppingCart cart = carts.get(0);
            List<ShoppingCartItem> cartItems = db.getWhere(ShoppingCartItem.class, "cartId", cart.getId());
            JsonObject itemsObject = new JsonObject();
            for (ShoppingCartItem cartItem : cartItems) {
                StoreItem item = db.getWhere(StoreItem.class, "id", cartItem.getItemId()).get(0);
                JsonObject itemObject = new JsonObject();
                itemObject.addProperty("name", item.getItemName());
                itemObject.addProperty("price", item.getPrice());
                itemObject.addProperty("quantity", cartItem.getQuantity());
                itemsObject.add(cartItem.getItemId().toString(), itemObject);
            }
            response.add("items", itemsObject);
        } else {
            response.add("items", new JsonObject());
        }

        response.addProperty("status", "success");
        return gson.toJson(response);
    }
}