package app.vcampus.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="storetransaction")
public class StoreTransaction {
    @Id
    public UUID uuid = UUID.randomUUID();

    @ManyToOne
    @JoinColumn(name = "itemUuid", referencedColumnName = "uuid", insertable = false, updatable = false)
    public StoreItem storeItem;

    public Integer itemPrice;

    public Integer amount;

    @Column(length = 9)
    public String cardNumber;

    public LocalDateTime time;

    public UUID getUuid() {
        return uuid;
    }

    public StoreItem getStoreItem() {
        return storeItem;
    }

    public Integer getItemPrice() {
        return itemPrice;
    }

    public Integer getAmount() {
        return amount;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public LocalDateTime getTransactionTime() {
        return time;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setStoreItem(StoreItem storeItem) {
        this.storeItem = storeItem;
    }

    public void setItemPrice(Integer itemPrice) {
        this.itemPrice = itemPrice;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}