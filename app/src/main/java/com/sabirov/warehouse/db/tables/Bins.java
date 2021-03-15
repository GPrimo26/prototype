package com.sabirov.warehouse.db.tables;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "Bins", indices ={@Index(value = {"BinID"}, unique = true)})
public class Bins {

    @ColumnInfo(name = "BinID")
    @PrimaryKey()
    private int binID;

    @ColumnInfo(name = "Name")
    private String name;

    @ColumnInfo(name = "Code1")
    private int code1;

    @ColumnInfo(name = "Code2")
    private int code2;

    public int getBinID() {
        return binID;
    }

    public void setBinID(int binID) {
        this.binID = binID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode1() {
        return code1;
    }

    public void setCode1(int code1) {
        this.code1 = code1;
    }

    public int getCode2() {
        return code2;
    }

    public void setCode2(int code2) {
        this.code2 = code2;
    }
}
