package com.sabirov.warehouse.db.tables;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "PickLater")
public class PickLater {

    @ColumnInfo(name = "OrderID")
    @PrimaryKey()
    private int orderID;

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }
}
