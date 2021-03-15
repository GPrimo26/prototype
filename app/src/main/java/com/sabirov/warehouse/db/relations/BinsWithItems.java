package com.sabirov.warehouse.db.relations;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Relation;

import com.sabirov.warehouse.db.tables.Bins;
import com.sabirov.warehouse.db.tables.Items;

import java.util.List;

public class BinsWithItems {
    @Embedded
    private
    Bins bin;

    @Relation(parentColumn = "BinID", entityColumn = "BinID")
    private
    List<Items> items;

    public BinsWithItems(Bins bin, List<Items> items) {
        this.bin = bin;
        this.items = items;
    }

    public Bins getBin() {
        return bin;
    }

    public void setBin(Bins bin) {
        this.bin = bin;
    }

    public List<Items> getItems() {
        return items;
    }

    public void setItems(List<Items> items) {
        this.items = items;
    }
}
