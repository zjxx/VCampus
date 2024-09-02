package app.vcampus.domain;

import java.util.List;
import java.util.UUID;

public class ShoppingCart {
    private UUID id;//购物车id
    private UUID userId;//用户id
    private List<ShoppingCartItem> items;//购物车商品列表


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public List<ShoppingCartItem> getItems() {
        return items;
    }

    public void setItems(List<ShoppingCartItem> items) {
        this.items = items;
    }
}