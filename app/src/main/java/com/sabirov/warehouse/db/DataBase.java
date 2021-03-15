package com.sabirov.warehouse.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.sabirov.warehouse.db.daos.BinsDao;
import com.sabirov.warehouse.db.daos.ItemsDao;
import com.sabirov.warehouse.db.daos.OrdersDao;
import com.sabirov.warehouse.db.tables.Bins;
import com.sabirov.warehouse.db.tables.Items;
import com.sabirov.warehouse.db.tables.Orders;
import com.sabirov.warehouse.db.tables.PickLater;

import warehouse.Bin;


@Database(entities = {Items.class, Orders.class, Bins.class, PickLater.class}, version = 3, exportSchema = false)
public abstract class DataBase extends RoomDatabase {
    private static DataBase dataBase;

    public synchronized static DataBase getInstance(Context context){
        if (dataBase==null){
            String DATABASE_NAME = "database";
            dataBase= Room.databaseBuilder(context.getApplicationContext(), DataBase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration().build();
        }
        return dataBase;
    }

    public abstract OrdersDao orderDao();
    public abstract ItemsDao itemsDao();
    public abstract BinsDao binsDao();
}
