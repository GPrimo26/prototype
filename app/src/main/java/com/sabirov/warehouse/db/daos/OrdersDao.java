package com.sabirov.warehouse.db.daos;

import android.content.Intent;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.sabirov.warehouse.db.relations.OrderWithItems;
import com.sabirov.warehouse.db.tables.Orders;
import com.sabirov.warehouse.db.tables.PickLater;

import java.util.List;

import warehouse.Order;

@Dao
public interface OrdersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void createOrder(Orders order);

    @Delete
    void deleteOrder(Orders order);

    @Update
    void updateOrder(Orders order);

    @Query("select * from Orders")
    List<Orders> getAllOrders();

    @Query("select * from Orders where OrderID=:orderID")
    Orders getOrderByID(Integer orderID);

    @Transaction
    @Query("select * from Orders where OrderID=:orderID")
    OrderWithItems getOrderWithItemsByID(Integer orderID);

    @Transaction
    @Query("select * from Orders")
    List<OrderWithItems> getOrdersWithItems();

    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    void addToPickLater(PickLater pickLater);

    @Query("select * from PickLater")
    List<PickLater> getPickLaterOrders();

    @Query("select * from PickLater where OrderID=:orderId")
    PickLater getPickLaterOrderByID(Integer orderId);

}
