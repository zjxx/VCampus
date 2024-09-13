package app.vcampus.controller;

import app.vcampus.domain.ShoppingCartItem;
import app.vcampus.domain.StoreItem;
import app.vcampus.domain.User;
import app.vcampus.domain.StoreTransaction;
import app.vcampus.interfaces.PurchaseRequest;
import app.vcampus.utils.DataBase;
import app.vcampus.utils.DataBaseManager;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class ShoppingCartController {
    private final Gson gson = new Gson();
    private StoreTransaction createStoreTransaction(StoreItem storeItem, PurchaseRequest request) {
        StoreTransaction transaction = new StoreTransaction();
        transaction.setUuid(UUID.randomUUID());
        transaction.setStoreItem(storeItem);
        transaction.setItemUuid(storeItem.getUuid()); // 设置 itemUuid 字段
        transaction.setTime(LocalDateTime.now());
        transaction.setAmount(request.getAmount());
        transaction.setCardNumber(request.getCardNumber());
        return transaction;
    }

    /**
     * Creates a JSON representation of a shopping cart item.
     *
     * @param item The store item to be added to the shopping cart.
     * @param quantity The quantity of the item to be added.
     * @return A JSON string representing the shopping cart item.
     */
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
    /**
     * Adds an item to the shopping cart.
     *
     * @param jsonData The JSON data containing userId, itemId, and quantity.
     * @return A JSON string containing the status of the add item request.
     */
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
    /**
     * Removes an item from the shopping cart.
     *
     * @param jsonData The JSON data containing userId and itemId.
     * @return A JSON string containing the status of the remove item request.
     */
    public String removeItemFromCart(String jsonData) {
        try {
            JsonObject request = gson.fromJson(jsonData, JsonObject.class);
            String userId = request.get("userId").getAsString();
            UUID itemId = UUID.fromString(request.get("itemId").getAsString());

            DataBase db = DataBaseManager.getInstance();
            List<ShoppingCartItem> cartItems = db.getWhere(ShoppingCartItem.class, "userId", userId);
            for(ShoppingCartItem cartItem : cartItems) {
                if (cartItem.getItemId().equals(itemId)) {
                    db.remove(cartItem);
                    break;
                    }
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
    /**
     * Views the contents of the shopping cart.
     *
     * @param jsonData The JSON data containing the userId.
     * @return A JSON string containing the status and the list of items in the cart.
     */
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


    /**
     * Handles the purchase of items in the shopping cart.
     *
     * @param jsonData The JSON data containing the userId and the list of items to purchase.
     * @return A JSON string containing the status of the purchase request.
     */
    public String handleCartPurchase(String jsonData) {
        try {
            JsonObject request = gson.fromJson(jsonData, JsonObject.class);
            String userId = request.get("userId").getAsString();
            int length = request.get("length").getAsInt();

            // 解析 items 字段为 JsonArray
            String itemsString = request.get("items").getAsString();
            JsonArray itemsArray = gson.fromJson(itemsString, JsonArray.class);

            DataBase db = DataBaseManager.getInstance();
            int totalCost = 0;

            // 检查库存并计算总价格
            for (int i = 0; i < length; i++) {
                JsonObject itemObject = itemsArray.get(i).getAsJsonObject();
                UUID itemId = UUID.fromString(itemObject.get("itemUuid").getAsString());
                int quantity = itemObject.get("quantity").getAsInt();

                StoreItem item = db.getWhere(StoreItem.class, "uuid", itemId).get(0);
                if (item.getStock() < quantity) {
                    JsonObject response = new JsonObject();
                    response.addProperty("status", "failed");
                    response.addProperty("reason", "库存不足: " + item.getItemName());
                    return gson.toJson(response);
                }
                totalCost += item.getPrice() * quantity;
            }

            // 检查用户余额
            User user = db.getWhere(User.class, "userId", userId).get(0);
            if (user.getBalance() < totalCost) {
                JsonObject response = new JsonObject();
                response.addProperty("status", "failed");
                response.addProperty("reason", "余额不足");
                return gson.toJson(response);
            }

            // 更新库存、销售量并扣除用户余额
            for (int i = 0; i < length; i++) {
                JsonObject itemObject = itemsArray.get(i).getAsJsonObject();
                UUID itemId = UUID.fromString(itemObject.get("itemUuid").getAsString());
                int quantity = itemObject.get("quantity").getAsInt();

                StoreItem item = db.getWhere(StoreItem.class, "uuid", itemId).get(0);
                item.setStock(item.getStock() - quantity);
                item.setSalesVolume(item.getSalesVolume() + quantity);
                db.persist(item);

                StoreTransaction transaction = new StoreTransaction();
                transaction.setUuid(UUID.randomUUID());
                transaction.setStoreItem(item);
                transaction.setItemUuid(item.getUuid());
                transaction.setTime(LocalDateTime.now());
                transaction.setAmount(quantity);
                transaction.setCardNumber(userId);
                db.persist(transaction);
                List<ShoppingCartItem> cartItems = db.getWhere(ShoppingCartItem.class, "userId", userId);
                for (ShoppingCartItem cartItem : cartItems) {
                    if (cartItem.getItemId().equals(itemId)) {
                        db.disableForeignKeyChecks();
                        db.remove(cartItem);
                        db.enableForeignKeyChecks();
                        break;
                    }
                }
            }

            // 扣除用户余额
            user.setBalance(user.getBalance() - totalCost);
            db.persist(user);

            JsonObject response = new JsonObject();
            response.addProperty("status", "success");
            return gson.toJson(response);
        } catch (Exception e) {
            JsonObject response = new JsonObject();
            response.addProperty("status", "failed");
            response.addProperty("reason", e.getMessage());
            return gson.toJson(response);
        }
    }
}