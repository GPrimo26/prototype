package com.sabirov.warehouse.db.relations;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Relation;

import com.sabirov.warehouse.db.tables.Items;
import com.sabirov.warehouse.db.tables.Orders;
import com.sabirov.warehouse.db.tables.ItemsToPickRemove;

import java.util.List;


public class OrderWithItems {

    @Embedded
    private
    Orders order;

    @Relation(parentColumn = "OrderID", entityColumn = "OrderID")
    private
    List<Items> items;

    public OrderWithItems(Orders order, List<Items> items) {
        this.order = order;
        this.items = items;
    }

    public Orders getOrder() {
        return order;
    }

    public void setOrder(Orders order) {
        this.order = order;
    }

    public List<Items> getItems() {
        return items;
    }

    public void setItems(List<Items> items) {
        this.items = items;
    }
}
