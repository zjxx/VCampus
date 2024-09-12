package app.vcampus.controller;

import app.vcampus.domain.StoreItem;
import app.vcampus.domain.StoreTransaction;
import app.vcampus.domain.User;
import app.vcampus.domain.ShoppingCartItem;
import app.vcampus.interfaces.PurchaseRequest;
import app.vcampus.utils.DataBase;
import app.vcampus.utils.DataBaseManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;


import java.io.FileOutputStream;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;


public class StoreController {
    private final Gson gson = new Gson();
    private final StoreItemController storeItemController = new StoreItemController();


    //创建商品的JSON对象
    private String createItemJsonObject(StoreItem item)
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

        return gson.toJson(jsonObject);
    }

    //创建交易对象
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

    //创建交易的JSON对象
    private JsonObject createTransactionJsonObject(StoreTransaction transaction) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("uuid", transaction.getUuid().toString());
        jsonObject.addProperty("itemUuid", transaction.getItemUuid().toString());
        jsonObject.addProperty("itemName", transaction.getStoreItem().getItemName());
        jsonObject.addProperty("itemPrice", String.valueOf(transaction.getStoreItem().getPrice()));
        jsonObject.addProperty("amount", String.valueOf(transaction.getAmount()));
        jsonObject.addProperty("cardNumber", transaction.getCardNumber());
        jsonObject.addProperty("time", transaction.getTransactionTime().toString());
        return jsonObject;
    }



    //处理购买请求,传入itemUuid（商品的UUID）,amount(购买数量),cardNumber（一卡通号）,itemName（商品名称）
    public String handlePurchase(String jsonData) {
       try {
           // 将 JSON 转换为 PurchaseRequest 对象
           PurchaseRequest request = gson.fromJson(jsonData, PurchaseRequest.class);

           // 查找 StoreItem 对象
           DataBase db = DataBaseManager.getInstance();
           StoreItem storeItem = db.getWhere(StoreItem.class, "uuid", UUID.fromString(request.getItemUuid())).get(0);

           // 查找 User 对象
           User user = db.getWhere(User.class, "userId", request.getCardNumber()).get(0);

           // 计算总价并扣款
           int totalPrice = storeItem.getPrice() * request.getAmount();
           if(user.getBalance() < totalPrice) {
               JsonObject response = new JsonObject();
               response.addProperty("status", "failed");
               response.addProperty("reason", "余额不足");
               return gson.toJson(response);
           }
           user.setBalance(user.getBalance() - totalPrice);
           db.persist(user);

           // 更新商品的销量数据
           storeItem.setSalesVolume(storeItem.getSalesVolume() + request.getAmount());
           db.persist(storeItem);

           // 创建 StoreTransaction 对象
           StoreTransaction transaction = createStoreTransaction(storeItem, request);

           // 处理转换后的 StoreTransaction 对象
           StoreTransactionController storeTransactionController = new StoreTransactionController();
           storeTransactionController.addTransaction(transaction);

           // 返回成功消息和用户余额
           JsonObject response = new JsonObject();
           response.addProperty("status", "success");
           response.addProperty("balance", String.valueOf(user.getBalance()));
           return gson.toJson(response);
       }
        catch (Exception e)
        {
            JsonObject response = new JsonObject();
            response.addProperty("status", "failed");
            response.addProperty("reason", e.getMessage());
            return gson.toJson(response);
        }
    }

    //根据关键词搜索商品，传入itemname（商品名称）
    public String searchItems(String jsonData) {
        try {
            JsonObject request = gson.fromJson(jsonData, JsonObject.class);
            String keyword = request.get("itemname").getAsString();

            DataBase db = DataBaseManager.getInstance();
            List<StoreItem> items = db.getLike(StoreItem.class, "itemName", keyword);
            if (items.isEmpty()) {
                JsonObject response = new JsonObject();
                response.addProperty("status", "failed");
                response.addProperty("reason", "没有找到该商品");
                return gson.toJson(response);
            }

            JsonObject itemsObject = new JsonObject();
            for (int i = 0; i < items.size(); i++) {
                itemsObject.addProperty("item" + i, createItemJsonObject(items.get(i)));
            }

            JsonObject response = new JsonObject();
            response.addProperty("status", "success");
            response.addProperty("length", String.valueOf(items.size()));
            response.addProperty("items", gson.toJson(itemsObject));
            return gson.toJson(response);
        } catch (Exception e) {
            JsonObject response = new JsonObject();
            response.addProperty("status", "failed");
            response.addProperty("reason", e.getMessage());
            return gson.toJson(response);
        }
    }

    //获取所有商品
    public String getAllItems(String jsonData) {
        try {
            DataBase db = DataBaseManager.getInstance();
            List<StoreItem> items = db.getAll(StoreItem.class);

            if (items.isEmpty()) {
                JsonObject response = new JsonObject();
                response.addProperty("status", "failed");
                response.addProperty("reason", "商店中没有商品");
                return gson.toJson(response);
            }

            JsonObject itemsObject = new JsonObject();
            for (int i = 0; i < items.size(); i++) {
                itemsObject.addProperty("item" + i, createItemJsonObject(items.get(i)));
            }

            JsonObject response = new JsonObject();
            response.addProperty("status", "success");
            response.addProperty("length",String.valueOf(items.size()));
            response.addProperty("items", gson.toJson(itemsObject));
            return gson.toJson(response);
        } catch (Exception e) {
            JsonObject response = new JsonObject();
            response.addProperty("status", "failed");
            response.addProperty("reason", e.getMessage());
            return gson.toJson(response);
        }
    }

    //获取所有交易记录
    public String getAllTransaction(String jsonData) {
        try {
            DataBase db = DataBaseManager.getInstance();
            List<StoreTransaction> transactions = db.getAll(StoreTransaction.class);

            if (transactions.isEmpty()) {
                JsonObject response = new JsonObject();
                response.addProperty("status", "failed");
                response.addProperty("reason", "没有交易记录");
                return gson.toJson(response);
            }

            JsonObject transactionsObject = new JsonObject();
            for (int i = 0; i < transactions.size(); i++) {
                transactionsObject.add("transaction" + i, createTransactionJsonObject(transactions.get(i)));
            }

            JsonObject response = new JsonObject();
            response.addProperty("status", "success");
            response.addProperty("length", String.valueOf(transactions.size()));
            response.addProperty("transactions", gson.toJson(transactionsObject));
            return gson.toJson(response);
        } catch (Exception e) {
            JsonObject response = new JsonObject();
            response.addProperty("status", "failed");
            response.addProperty("reason", e.getMessage());
            return gson.toJson(response);
        }
    }

    //获取随机商品，用于首页展示,返回随机商品列表
    public String enterStore(String jsonData) {
        try {
            DataBase db = DataBaseManager.getInstance();
            List<StoreItem> items = db.getAll(StoreItem.class);
            Collections.shuffle(items);

            List<StoreItem> randomItems = items.subList(0, Math.min(10, items.size()));

            JsonObject itemsObject = new JsonObject();
            for (int i = 0; i < randomItems.size(); i++) {
                itemsObject.addProperty("item" + i, createItemJsonObject(randomItems.get(i)));
            }

            JsonObject response = new JsonObject();
            response.addProperty("status", "success");
            response.addProperty("length", String.valueOf(randomItems.size()));
            response.addProperty("items", gson.toJson(itemsObject));
            return gson.toJson(response);
        } catch (Exception e) {
            JsonObject response = new JsonObject();
            response.addProperty("status", "failed");
            response.addProperty("reason", e.getMessage());
            return gson.toJson(response);
        }
    }

    //获取特定一卡通号的订单，传入cardNumber（一卡通号）
    public String getTransactionsByCardNumber(String jsonData) {
        try {
            // 从 JSON 请求中提取一卡通号
            JsonObject request = gson.fromJson(jsonData, JsonObject.class);
            String cardNumber = request.get("cardNumber").getAsString();

            // 查找匹配的交易记录
            DataBase db = DataBaseManager.getInstance();
            List<StoreTransaction> transactions = db.getWhere(StoreTransaction.class, "cardNumber", cardNumber);
            if(transactions.isEmpty())
            {
                JsonObject response = new JsonObject();
                response.addProperty("status", "failed");
                response.addProperty("reason", "该一卡通号没有交易记录");
                return gson.toJson(response);
            }

            // 构建 JSON 响应
            JsonObject transactionsObject = new JsonObject();
            for (int i = 0; i < transactions.size(); i++) {
                transactionsObject.add("transaction" + i, createTransactionJsonObject(transactions.get(i)));
            }

            JsonObject response = new JsonObject();
            response.addProperty("status", "success");
            response.addProperty("length", String.valueOf(transactions.size()));
            response.addProperty("transactions", gson.toJson(transactionsObject));
            return gson.toJson(response);
        }
        catch (Exception e) {
            JsonObject response = new JsonObject();
            response.addProperty("status", "failed");
            response.addProperty("reason", e.getMessage());
            return gson.toJson(response);
        }

    }

    //获取特定商品的销量
    public String getSalesVolume(String jsonData) {
        // 从 JSON 请求中提取商品 UUID
        JsonObject request = gson.fromJson(jsonData, JsonObject.class);
        String itemUuid = request.get("itemUuid").getAsString();

        // 查找匹配的商品
        DataBase db = DataBaseManager.getInstance();
        StoreItem item = db.getWhere(StoreItem.class, "uuid", UUID.fromString(itemUuid)).get(0);
        if(item == null)
        {
            JsonObject response = new JsonObject();
            response.addProperty("status", "failed");
            response.addProperty("reason", "没有找到该商品");
            return gson.toJson(response);
        }

        // 构建 JSON 响应
        JsonObject response = new JsonObject();
        response.addProperty("status", "success");
        response.addProperty("salesVolume", item.getSalesVolume());
        return gson.toJson(response);
    }

    //添加商品，传入商品的JSON对象
    public String addItem(String jsonData, String additionalParam) {
        JsonObject data = new JsonObject();
        JsonObject request = gson.fromJson(jsonData, JsonObject.class);
        try {
            DataBase db = DataBaseManager.getInstance();
            List<StoreItem> storeItems = db.getWhere(StoreItem.class, "itemName", request.get("itemName").getAsString());
            if (!storeItems.isEmpty()) {
                data.addProperty("status", "failed");
                data.addProperty("reason", "该商品已经存在");
                return gson.toJson(data);
            }
            StoreItem newItem = new StoreItem();
            newItem.setUuid(UUID.randomUUID());
            newItem.setItemName(request.get("itemName").getAsString());
            newItem.setPrice(request.get("price").getAsInt());
            newItem.setBarcode(request.get("barcode").getAsString());
            newItem.setStock(request.get("stock").getAsInt());
            newItem.setSalesVolume(request.get("salesVolumn").getAsInt());
            newItem.setDescription(request.get("description").getAsString());
            String filepath = "C:\\Users\\Administrator\\Desktop\\server\\img\\" + newItem.getUuid() + ".jpg";
            FileOutputStream fileOutputStream = new FileOutputStream(filepath);
            byte[] bytes = java.util.Base64.getDecoder().decode(additionalParam);
            fileOutputStream.write(bytes);
            fileOutputStream.close();
            newItem.setPictureLink(filepath);


            db.persist(newItem);
            data.addProperty("status", "success");
            return gson.toJson(data);
        } catch (Exception e) {
            JsonObject response = new JsonObject();
            response.addProperty("status", "failed");
            response.addProperty("reason", e.getMessage());
            return gson.toJson(response);
        }
    }

    // 删除商品，传入uuid（商品的UUID）
    public String removeItem(String jsonData) {
        JsonObject response = new JsonObject();
        try {
            JsonObject request = gson.fromJson(jsonData, JsonObject.class);
            UUID uuid = UUID.fromString(request.get("uuid").getAsString());

            DataBase db = DataBaseManager.getInstance();
            StoreItem item = db.getWhere(StoreItem.class, "uuid", uuid).get(0);

            // 删除商品图片
            String pictureLink = item.getPictureLink();
            java.io.File file = new java.io.File(pictureLink);
            if (file.exists()) {
                if (!file.delete()) {
                    response.addProperty("status", "failed");
                    response.addProperty("reason", "删除商品图片失败");
                    return gson.toJson(response);
                }
            }

            db.disableForeignKeyChecks();
            db.remove(item);
            List<StoreTransaction> transactions = db.getWhere(StoreTransaction.class, "itemUuid", uuid);
            for (StoreTransaction transaction : transactions) {
                db.remove(transaction);
            }
            //从购物车中删除商品
            List<ShoppingCartItem> cartItems = db.getWhere(ShoppingCartItem.class, "itemId", uuid);
            for (ShoppingCartItem cartItem : cartItems) {
                db.remove(cartItem);
            }
            db.remove(item);
            db.enableForeignKeyChecks();

            response.addProperty("status", "success");
        } catch (Exception e) {
            response.addProperty("status", "failed");
            response.addProperty("reason", e.getMessage());
        }
        return gson.toJson(response);
    }

    // 修改商品
    public String updateItem(String jsonData, String additionalParam) {
        try {
            JsonObject request = gson.fromJson(jsonData, JsonObject.class);
            String filepath = "C:\\Users\\Administrator\\Desktop\\server\\img\\" + request.get("uuid") + ".jpg";
            FileOutputStream fileOutputStream = new FileOutputStream(filepath);
            byte[] bytes = java.util.Base64.getDecoder().decode(additionalParam);
            fileOutputStream.write(bytes);
            fileOutputStream.close();

            DataBase db = DataBaseManager.getInstance();
            String itemname = request.get("itemName").getAsString();
            StoreItem existingItem = db.getWhere(StoreItem.class, "itemName", itemname).get(0);
            existingItem.setItemName(request.get("itemName").getAsString());
            existingItem.setPrice(request.get("price").getAsInt());
            existingItem.setPictureLink(filepath);
            existingItem.setBarcode(request.get("barcode").getAsString());
            existingItem.setStock(request.get("stock").getAsInt());
            existingItem.setSalesVolume(request.get("salesVolumn").getAsInt());
            existingItem.setDescription(request.get("description").getAsString());

            db.disableForeignKeyChecks();
            db.persist(existingItem);
            db.enableForeignKeyChecks();

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

    // 通过二维码购买商品
    public String QRbuy(String jsonData) {
        try {
            JsonObject request = gson.fromJson(jsonData, JsonObject.class);
            String cardNumber = request.get("cardNumber").getAsString();
            String itemUuid = request.get("itemUuid").getAsString();
            int amount = request.get("amount").getAsInt();
            String itemName = request.get("itemName").getAsString();

            DataBase db = DataBaseManager.getInstance();
            StoreItem storeItem = db.getWhere(StoreItem.class, "itemName", itemName).get(0);
            User user = db.getWhere(User.class, "userId", cardNumber).get(0);



            storeItem.setSalesVolume(storeItem.getSalesVolume() + amount);
            db.persist(storeItem);

            StoreTransaction transaction = new StoreTransaction();
            transaction.setUuid(UUID.randomUUID());
            transaction.setStoreItem(storeItem);
            transaction.setItemUuid(storeItem.getUuid());
            transaction.setTime(LocalDateTime.now());
            transaction.setAmount(amount);
            transaction.setCardNumber(cardNumber);
            db.persist(transaction);

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
