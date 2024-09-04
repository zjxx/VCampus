package app.vcampus.domain;

import jakarta.persistence.Column;
import java.util.UUID;

public class ShoppingCartItem {
    @Column(length = 13)
    private String userId; // 用户id
    private UUID itemId; // 商品id
    private int quantity; // 商品数量

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public UUID getItemId() {
        return itemId;
    }

    public void setItemId(UUID itemId) {
        this.itemId = itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}