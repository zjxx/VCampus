package app.vcampus.controller;

import app.vcampus.domain.StoreItem;
import app.vcampus.utils.DataBase;
import app.vcampus.utils.DataBaseManager;

import java.util.List;
import java.util.UUID;

public class StoreItemController {

    public void addItem(StoreItem item) {
        DataBase db = DataBaseManager.getInstance();
        db.persist(item);
    }

    public void deleteItem(UUID itemId) {
        DataBase db = DataBaseManager.getInstance();
        StoreItem item = db.getWhere(StoreItem.class, "uuid", itemId).get(0);
        db.remove(item);
    }

    public void updateItem(StoreItem updatedItem) {
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
    }

    public List<StoreItem> getItems() {
        DataBase db = DataBaseManager.getInstance();
        return db.getAll(StoreItem.class);
    }
}