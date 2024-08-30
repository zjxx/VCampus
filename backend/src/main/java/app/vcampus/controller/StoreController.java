package app.vcampus.controller;

import app.vcampus.domain.StoreItem;
import app.vcampus.domain.StoreTransaction;
import app.vcampus.domain.User;
import app.vcampus.interfaces.PurchaseRequest;
import app.vcampus.utils.DataBase;
import app.vcampus.utils.DataBaseManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;


public class StoreController {
    private final Gson gson = new Gson();
    private final StoreItemController storeItemController = new StoreItemController();
    private final StoreTransactionController storeTransactionController = new StoreTransactionController();

    //创建商品的JSON对象
    private JsonObject createItemJsonObject(StoreItem item)
    {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("uuid", item.getUuid().toString());
        jsonObject.addProperty("name", item.getItemName());
        jsonObject.addProperty("price", item.getPrice());
        jsonObject.addProperty("pictureLink", item.getPictureLink());
        jsonObject.addProperty("barcode", item.getBarcode());
        jsonObject.addProperty("stock", item.getStock());
        jsonObject.addProperty("salesVolume", item.getSalesVolume());
        jsonObject.addProperty("description", item.getDescription());
        return jsonObject;
    }

    //创建交易对象
    private StoreTransaction createStoreTransaction(StoreItem storeItem, PurchaseRequest request)
    {
        StoreTransaction transaction = new StoreTransaction();
        transaction.setUuid(UUID.randomUUID());
        transaction.setStoreItem(storeItem);
        transaction.setTime(LocalDateTime.now());
        transaction.setAmount(request.getAmount());
        transaction.setCardNumber(request.getCardNumber());
        return transaction;
    }

    //创建交易的JSON对象
    private JsonObject createTransactionJsonObject(StoreTransaction transaction)
    {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("uuid", transaction.getUuid().toString());
        jsonObject.addProperty("itemName", transaction.getStoreItem().getItemName());
        jsonObject.addProperty("itemPrice", transaction.getStoreItem().getPrice());
        jsonObject.addProperty("amount", transaction.getAmount());
        jsonObject.addProperty("cardNumber", transaction.getCardNumber());
        jsonObject.addProperty("time", transaction.getTransactionTime().toString());
        return jsonObject;
    }


    //处理购买请求
    public String handlePurchase(String jsonData) {
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
            response.addProperty("reason", "insufficient balance");
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
        response.addProperty("balance", user.getBalance());
        return gson.toJson(response);
    }

    //根据关键词搜索商品
    public String searchItems(String jsonData) {
        // 从 JSON 请求中提取关键词
        JsonObject request = gson.fromJson(jsonData, JsonObject.class);
        String keyword = request.get("keyword").getAsString();

        // 查找匹配的商品
        DataBase db = DataBaseManager.getInstance();
        List<StoreItem> items = db.getWhere(StoreItem.class, "name LIKE", "%" + keyword + "%");
        if(items.isEmpty())
        {
            JsonObject response = new JsonObject();
            response.addProperty("status", "failed");
            response.addProperty("reason", "no such item");
            return gson.toJson(response);
        }

        // 构建 JSON 响应
        JsonArray jsonArray = new JsonArray();
        for (StoreItem item : items) {
            jsonArray.add(createItemJsonObject(item));
        }

        JsonObject response = new JsonObject();
        response.addProperty("status", "success");
        response.add("items", jsonArray);
        return gson.toJson(response);
    }

    //获取所有商品
    public String getAllItems(String jsonData) {
        // 获取所有商品
        DataBase db = DataBaseManager.getInstance();
        List<StoreItem> items = db.getAll(StoreItem.class);

        if(items.isEmpty())
        {
            JsonObject response = new JsonObject();
            response.addProperty("status", "failed");
            response.addProperty("reason", "no item in store");
            return gson.toJson(response);
        }
        // 构建 JSON 响应
        JsonArray jsonArray = new JsonArray();
        for (StoreItem item : items) {
            jsonArray.add(createItemJsonObject(item));
        }

        JsonObject response = new JsonObject();
        response.addProperty("status", "success");
        response.add("items", jsonArray);
        return gson.toJson(response);
    }

    //获取所有交易记录
    public String getAllTransaction(String jsonData) {
        // 获取所有交易记录
        DataBase db = DataBaseManager.getInstance();
        List<StoreTransaction> transactions = db.getAll(StoreTransaction.class);
        if(transactions.isEmpty())
        {
            JsonObject response = new JsonObject();
            response.addProperty("status", "failed");
            response.addProperty("reason", "no transaction in store");
            return gson.toJson(response);
        }

        // 构建 JSON 响应
        JsonArray jsonArray = new JsonArray();
        for (StoreTransaction transaction : transactions) {
            jsonArray.add(createTransactionJsonObject(transaction));
        }

        JsonObject response = new JsonObject();
        response.addProperty("status", "success");
        response.add("transactions", jsonArray);
        return gson.toJson(response);
    }

    //获取随机商品，用于首页展示
    public String enterStore(String jsonData) {
        DataBase db = DataBaseManager.getInstance();
        List<StoreItem> items = db.getAll(StoreItem.class);
        Collections.shuffle(items); // 随机打乱商品列表

        // 取前10个商品
        List<StoreItem> randomItems = items.subList(0, Math.min(10, items.size()));

        // 构建 JSON 响应
        JsonArray jsonArray = new JsonArray();
        for (StoreItem item : randomItems) {
            jsonArray.add(createItemJsonObject(item));
        }

        JsonObject response = new JsonObject();
        response.addProperty("status", "success");
        response.add("items", jsonArray);
        return gson.toJson(response);
    }

    //获取特定一卡通号的订单
    public String getTransactionsByCardNumber(String jsonData) {
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
            response.addProperty("reason", "no transaction for this card number");
            return gson.toJson(response);
        }

        // 构建 JSON 响应
        JsonArray jsonArray = new JsonArray();
        for (StoreTransaction transaction : transactions) {
            jsonArray.add(createTransactionJsonObject(transaction));
        }

        JsonObject response = new JsonObject();
        response.addProperty("status", "success");
        response.add("transactions", jsonArray);
        return gson.toJson(response);
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
            response.addProperty("reason", "no such item");
            return gson.toJson(response);
        }

        // 构建 JSON 响应
        JsonObject response = new JsonObject();
        response.addProperty("status", "success");
        response.addProperty("salesVolume", item.getSalesVolume());
        return gson.toJson(response);
    }

    //添加商品
    public String addItem(String jsonData) {
        try {
            StoreItem newItem = gson.fromJson(jsonData, StoreItem.class);
            DataBase db = DataBaseManager.getInstance();
            db.persist(newItem);

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

    // 删除商品
    public String removeItem(String jsonData) {
        try {
            JsonObject request = gson.fromJson(jsonData, JsonObject.class);
            UUID uuid = UUID.fromString(request.get("uuid").getAsString());

            DataBase db = DataBaseManager.getInstance();
            StoreItem item = db.getWhere(StoreItem.class, "uuid", uuid).get(0);
            db.remove(item);

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

    // 修改商品
    public String updateItem(String jsonData) {
        try {
            StoreItem updatedItem = gson.fromJson(jsonData, StoreItem.class);

            DataBase db = DataBaseManager.getInstance();
            StoreItem existingItem = db.getWhere(StoreItem.class, "uuid", updatedItem.getUuid()).get(0);

            existingItem.setItemName(updatedItem.getItemName());
            existingItem.setPrice(updatedItem.getPrice());
            existingItem.setPictureLink(updatedItem.getPictureLink());
            existingItem.setBarcode(updatedItem.getBarcode());
            existingItem.setStock(updatedItem.getStock());
            existingItem.setSalesVolume(updatedItem.getSalesVolume());
            existingItem.setDescription(updatedItem.getDescription());

            db.persist(existingItem);

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
