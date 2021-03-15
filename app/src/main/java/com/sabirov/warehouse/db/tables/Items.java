package com.sabirov.warehouse.db.tables;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import warehouse.Item;

@Entity(tableName = "Items", indices ={@Index(value = {"ItemID"}, unique = true)})
public class Items implements Parcelable {

    @ColumnInfo(name="ItemID")
    @PrimaryKey()
    private int itemID;

    @ColumnInfo(name = "Name")
    private String name;

    @ColumnInfo(name = "OrderID")
    private int orderID;

    @ColumnInfo(name = "Code1")
    private int code1;

    @ColumnInfo(name = "Code2")
    private int code2;

    @ColumnInfo(name = "Description")
    private String description;

    @ColumnInfo(name = "BinID")
    private int binID;

    @ColumnInfo(name = "Quantity")
    private int quantity;

    public Items(int itemID, String name, int orderID, int code1, int code2, String description, int binID, int quantity) {
        this.itemID = itemID;
        this.name = name;
        this.orderID = orderID;
        this.code1 = code1;
        this.code2 = code2;
        this.description = description;
        this.binID = binID;
        this.quantity = quantity;
    }

    protected Items(Parcel in) {
        itemID = in.readInt();
        name = in.readString();
        orderID = in.readInt();
        code1 = in.readInt();
        code2 = in.readInt();
        description = in.readString();
        binID = in.readInt();
        quantity = in.readInt();
    }

    public static final Creator<Items> CREATOR = new Creator<Items>() {
        @Override
        public Items createFromParcel(Parcel in) {
            return new Items(in);
        }

        @Override
        public Items[] newArray(int size) {
            return new Items[size];
        }
    };

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getBinID() {
        return binID;
    }

    public void setBinID(int binID) {
        this.binID = binID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(itemID);
        parcel.writeString(name);
        parcel.writeInt(orderID);
        parcel.writeInt(code1);
        parcel.writeInt(code2);
        parcel.writeString(description);
        parcel.writeInt(binID);
        parcel.writeInt(quantity);
    }
}
