package app.vcampus.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import jakarta.persistence.*;

import java.util.UUID;


@Entity
@Table(name="storeitem")
public class StoreItem {
    @Id
    public UUID uuid= UUID.randomUUID();

    public String itemName;

    public Integer price;

    public String pictureLink;

    public String barcode;

    public Integer stock;

    public Integer salesVolume = 0;

    public String description;

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
