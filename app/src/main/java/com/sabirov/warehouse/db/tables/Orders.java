package com.sabirov.warehouse.db.tables;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "Orders", indices ={@Index(value = {"OrderID"}, unique = true)})
public class Orders implements Parcelable {

    @ColumnInfo(name="OrderID")
    @PrimaryKey()
    private int ID;

    @ColumnInfo(name = "Name")
    private String name;

    @ColumnInfo(name = "Code1")
    private int code1;

    @ColumnInfo(name = "Code2")
    private int code2;

    @ColumnInfo(name = "PickProblem")
    private String pickProblem;

    public Orders(int ID, String name, int code1, int code2, String pickProblem) {
        this.ID = ID;
        this.name = name;
        this.code1 = code1;
        this.code2 = code2;
        this.pickProblem = pickProblem;
    }

    protected Orders(Parcel in) {
        ID = in.readInt();
        name = in.readString();
        code1 = in.readInt();
        code2 = in.readInt();
        pickProblem = in.readString();
    }

    public static final Creator<Orders> CREATOR = new Creator<Orders>() {
        @Override
        public Orders createFromParcel(Parcel in) {
            return new Orders(in);
        }

        @Override
        public Orders[] newArray(int size) {
            return new Orders[size];
        }
    };

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
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

    public String getPickProblem() {
        return pickProblem;
    }

    public void setPickProblem(String pickProblem) {
        this.pickProblem = pickProblem;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(ID);
        parcel.writeString(name);
        parcel.writeInt(code1);
        parcel.writeInt(code2);
        parcel.writeString(pickProblem);
    }
}
