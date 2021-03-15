package com.sabirov.warehouse.db.tables;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ItemsToPickRemove")
public class ItemsToPickRemove {

    @ColumnInfo(name = "ItemID")
    @PrimaryKey()
    private Integer itemId;

    @ColumnInfo(name = "Quantity")
    private Integer quantity;

    public ItemsToPickRemove(Integer itemId, Integer quantity) {
        this.itemId = itemId;
        this.quantity = quantity;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
