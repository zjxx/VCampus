package app.vcampus.controller;

import app.vcampus.domain.StoreItem;
import app.vcampus.utils.DataBase;

import java.util.List;
import java.util.UUID;

public class StoreItemController {

    public void addItem(StoreItem item) {
        DataBase db = new DataBase();
        db.init();
        db.persist(item);
        db.close();
    }

    public void deleteItem(UUID itemId) {
        DataBase db = new DataBase();
        db.init();
        StoreItem item = db.getWhere(StoreItem.class, "uuid", itemId).get(0);
        db.remove(item);
        db.close();
    }

    public void updateItem(StoreItem updatedItem) {
        DataBase db = new DataBase();
        db.init();
        StoreItem existingItem = db.getWhere(StoreItem.class, "uuid", updatedItem.getUuid()).get(0);
        existingItem.setItemName(updatedItem.getItemName());
        existingItem.setPrice(updatedItem.getPrice());
        existingItem.setPictureLink(updatedItem.getPictureLink());
        existingItem.setBarcode(updatedItem.getBarcode());
        existingItem.setStock(updatedItem.getStock());
        existingItem.setSalesVolume(updatedItem.getSalesVolume());
        existingItem.setDescription(updatedItem.getDescription());
        db.persist(existingItem);
        db.close();
    }

    public List<StoreItem> getItems() {
        DataBase db = new DataBase();
        db.init();
        List<StoreItem> items = db.getAll(StoreItem.class);
        db.close();
        return items;
    }
}