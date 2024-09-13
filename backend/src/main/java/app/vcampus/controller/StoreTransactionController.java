package app.vcampus.controller;

import app.vcampus.interfaces.PurchaseRequest;
import app.vcampus.domain.StoreItem;
import app.vcampus.domain.StoreTransaction;
import app.vcampus.domain.User;
import app.vcampus.utils.DataBase;
import app.vcampus.utils.DataBaseManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;

public class StoreTransactionController
{
    private final Gson gson = new Gson();

    /**
     * Adds a new store transaction to the database.
     *
     * @param transaction The StoreTransaction object to be added.
     */
    public void addTransaction(StoreTransaction transaction) {
        DataBase db = DataBaseManager.getInstance();
        db.persist(transaction);
    }

    public void deleteTransaction(UUID transactionId) {
        DataBase db = DataBaseManager.getInstance();
        StoreTransaction transaction = db.getWhere(StoreTransaction.class, "uuid", transactionId).get(0);
        db.remove(transaction);
    }

    public void updateTransaction(StoreTransaction updatedTransaction) {
        DataBase db = DataBaseManager.getInstance();
        StoreTransaction existingTransaction = db.getWhere(StoreTransaction.class, "uuid", updatedTransaction.getUuid()).get(0);
        existingTransaction.setStoreItem(updatedTransaction.getStoreItem());
        existingTransaction.setItemPrice(updatedTransaction.getItemPrice());
        existingTransaction.setAmount(updatedTransaction.getAmount());
        existingTransaction.setCardNumber(updatedTransaction.getCardNumber());
        existingTransaction.setTime(updatedTransaction.getTransactionTime());
        db.persist(existingTransaction);
    }

    public List<StoreTransaction> getTransactions() {
        DataBase db = DataBaseManager.getInstance();
        return db.getAll(StoreTransaction.class);
    }

//    public String handlePurchase(String jsonData) //处理购买请求
//    {
//        // 将 JSON 转换为 PurchaseRequest 对象
//        PurchaseRequest request = gson.fromJson(jsonData, PurchaseRequest.class);
//
//        // 查找 StoreItem 对象
//        DataBase db = DataBaseManager.getInstance();
//        StoreItem storeItem = db.getWhere(StoreItem.class, "uuid", UUID.fromString(request.getItemUuid())).get(0);
//
//        // 查找 User 对象
//        User user = db.getWhere(User.class, "userId", request.getCardNumber()).get(0);
//
//        // 计算总价并扣款
//        int totalPrice = storeItem.getPrice() * request.getAmount();
//        user.setBalance(user.getBalance() - totalPrice);
//        db.persist(user);
//
//        // 创建 StoreTransaction 对象
//        StoreTransaction transaction = new StoreTransaction();
//        transaction.setUuid(UUID.randomUUID());
//        transaction.setStoreItem(storeItem);
//        transaction.setTime(LocalDateTime.now());
//        transaction.setAmount(request.getAmount());
//        transaction.setCardNumber(request.getCardNumber());
//
//        // 处理转换后的 StoreTransaction 对象
//        addTransaction(transaction);
//
//        // 返回成功消息和用户余额
//        JsonObject response = new JsonObject();
//        response.addProperty("status", "success");
//        response.addProperty("balance", user.getBalance());
//        return gson.toJson(response);
//    }
}