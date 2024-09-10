package app.vcampus.controller;

import app.vcampus.domain.ShoppingCartItem;
import app.vcampus.controller.StoreController;
import app.vcampus.domain.StoreItem;
import app.vcampus.utils.DataBase;
import app.vcampus.utils.DataBaseManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.UUID;

public class ShoppingCartController {
    private final Gson gson = new Gson();
    private final StoreController storeController = new StoreController();
    private String createShoppingCartObjectJson(StoreItem item,int quantity)
    {
        String description = "";
        if (item.getDescription() != null) {
            description = item.getDescription();
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("uuid", item.getUuid().toString());
        jsonObject.addProperty("name", item.getItemName());
        jsonObject.addProperty("price",String.valueOf (item.getPrice()));
        jsonObject.addProperty("pictureLink", item.getPictureLink());
        jsonObject.addProperty("barcode", item.getBarcode());
        jsonObject.addProperty("stock",String.valueOf(item.getStock()) );
        jsonObject.addProperty("salesVolume", String.valueOf(item.getSalesVolume()));
        jsonObject.addProperty("description", description);
        jsonObject.addProperty("quantity",String.valueOf(quantity));

        return gson.toJson(jsonObject);
    }

    // 添加商品到购物车，传入 userId, itemId, quantity
    public String addItemToCart(String jsonData) {
       try {
           JsonObject request = gson.fromJson(jsonData, JsonObject.class);
           String userId = request.get("userId").getAsString();
           UUID itemId = UUID.fromString(request.get("itemId").getAsString());
           int quantity = request.get("quantity").getAsInt();
           DataBase db = DataBaseManager.getInstance();
           List<ShoppingCartItem> cartItems = db.getWhere(ShoppingCartItem.class, "userId", userId);
              for (ShoppingCartItem cartItem : cartItems) {
                if (cartItem.getItemId().equals(itemId)) {
                     cartItem.setQuantity(cartItem.getQuantity() + quantity);
                     db.persist(cartItem);
                     JsonObject response = new JsonObject();
                     response.addProperty("status", "success");
                     return gson.toJson(response);
                }
              }

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


    // 从购物车移除商品，传入 userId(用户一卡通号), itemId（商品uuid），quantity（数量）
    public String removeItemFromCart(String jsonData) {
        try {
            JsonObject request = gson.fromJson(jsonData, JsonObject.class);
            String userId = request.get("userId").getAsString();
            UUID itemId = UUID.fromString(request.get("itemId").getAsString());// Get the UUID
            int quantity = request.get("quantity").getAsInt();

            DataBase db = DataBaseManager.getInstance();
            List<ShoppingCartItem> cartItems = db.getWhere(ShoppingCartItem.class, "userId", userId);
            db.disableForeignKeyChecks();
            for (ShoppingCartItem cartItem : cartItems) {
               db.remove(cartItem);
            }
            db.enableForeignKeyChecks();

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
                String shoppingCartObjectJson = createShoppingCartObjectJson(item,cartItem.getQuantity());
                JsonObject itemObject = new JsonObject();
                itemsObject.addProperty("item" + i, shoppingCartObjectJson);
            }

            JsonObject response = new JsonObject();
            response.addProperty("status", "success");
            response.addProperty("length", String.valueOf(cartItems.size()));
            response.addProperty("items", gson.toJson(itemsObject));
            return gson.toJson(response);
        } catch (Exception e) {
            JsonObject response = new JsonObject();
            response.addProperty("status", "failed");
            response.addProperty("reason", e.getMessage());
            return gson.toJson(response);
        }
    }
}