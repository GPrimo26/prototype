package com.sabirov.warehouse.db.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.sabirov.warehouse.db.tables.Items;

import java.util.List;

@Dao
public interface ItemsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void createItem(Items item);

    @Delete
    void deleteItem(Items item);

    @Query("select * from Items where ItemID=:id")
    Items getItemById(Integer id);

    @Update
    void updateItemInfo(Items item);

    @Update
    void updateItems(List<Items> itemsList);
}
