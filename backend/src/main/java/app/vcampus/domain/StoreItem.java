package app.vcampus.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

import java.util.UUID;

@Entity
@Table(name="storeitem")
public class StoreItem {
    @Id
    @Column(unique = true, nullable = false)
    private UUID uuid = UUID.randomUUID();

    private String itemName;

    private Integer price;

    private String pictureLink;

    private String barcode;

    private Integer stock;

    private Integer salesVolume = 0;

    private String description;

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