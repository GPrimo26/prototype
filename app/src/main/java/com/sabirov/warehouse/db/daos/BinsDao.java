package com.sabirov.warehouse.db.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.sabirov.warehouse.db.relations.BinsWithItems;
import com.sabirov.warehouse.db.tables.Bins;

import java.util.List;

@Dao
public interface BinsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void createBin(Bins bin);

    @Delete
    void deleteBin(Bins bin);

    @Update
    void updateBin(Bins bins);

    @Query("select * from Bins")
    List<Bins> getAllBins();

    @Query("select * from Bins where BinID=:binID")
    Bins getBinByID(Integer binID);

    @Transaction
    @Query("select * from Bins where BinID=:binID")
    BinsWithItems getBinWithItemsByID(Integer binID);

    @Transaction
    @Query("select * from Bins")
    List<BinsWithItems> getBinsWithItems();
}
