package app.vcampus.domain;

import jakarta.persistence.*;

import java.util.UUID;
/**
 * Represents a store item entity in the database.
 */
@Entity
@Table(name="storeitem")
public class StoreItem {
    @Id
    @Column(unique = true, nullable = false)
    private UUID uuid = UUID.randomUUID();

    @Column(length = 20)
    private String itemName;

    private Integer price;

    @Column(length = 100)
    private String pictureLink;

    @Column(length = 14)
    private String barcode;

    private Integer stock;

    private Integer salesVolume = 0;

    @Column(length = 100)
    private String description;

    @PreUpdate
    public void onPreUpdate() {}

    public UUID getUuid() {
        return uuid;
    }

    public String getItemName() {
        return itemName;
    }

    public Integer getPrice() {
        return price;
    }

    public String getPictureLink() {
        return pictureLink;
    }

    public String getBarcode() {
        return barcode;
    }

    public Integer getStock() {
        return stock;
    }

    public Integer getSalesVolume() {
        return salesVolume;
    }

    public String getDescription() {
        return description;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setPictureLink(String pictureLink) {
        this.pictureLink = pictureLink;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public void setSalesVolume(Integer salesVolume) {
        this.salesVolume = salesVolume;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}