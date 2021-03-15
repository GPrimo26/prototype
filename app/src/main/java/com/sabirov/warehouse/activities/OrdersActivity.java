package com.sabirov.warehouse.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sabirov.warehouse.R;
import com.sabirov.warehouse.adapters.OrdersRVA;
import com.sabirov.warehouse.db.DataBase;
import com.sabirov.warehouse.db.relations.BinsWithItems;
import com.sabirov.warehouse.db.relations.OrderWithItems;
import com.sabirov.warehouse.db.tables.Bins;
import com.sabirov.warehouse.db.tables.Items;
import com.sabirov.warehouse.db.tables.Orders;
import com.sabirov.warehouse.db.tables.ItemsToPickRemove;
import com.sabirov.warehouse.db.tables.PickLater;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import warehouse.Empty;
import warehouse.Item;
import warehouse.Order;
import warehouse.WorkWithBinsGrpc;
import warehouse.WorkWithItemsGrpc;
import warehouse.WorkWithOrdersGrpc;

import static com.sabirov.warehouse.Variables.ip;

public class OrdersActivity extends AppCompatActivity {

    private RecyclerView orders_rv;
    private OrdersRVA adapter;
    private List<Orders> ordersList;
    private DataBase dataBase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        dataBase = DataBase.getInstance(this);
        findIDs();
        setInfo();
        setActions();
        doCalls();
    }

    private void findIDs() {
        orders_rv = findViewById(R.id.orders_rv);
    }

    private void setInfo() {
        ordersList = new ArrayList<>();
        adapter = new OrdersRVA(this, ordersList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                RecyclerView.VERTICAL, false);
        orders_rv.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(orders_rv.getContext(),
                        linearLayoutManager.getOrientation());
        orders_rv.addItemDecoration(dividerItemDecoration);
        orders_rv.setAdapter(adapter);
    }

    private void setActions() {
        adapter.setOnItemClickListener(order -> {
            Intent intent=new Intent(OrdersActivity.this, OrderItemsActivity.class);
            intent.putExtra("order", order);
            startActivity(intent);
        });
    }

    private void doCalls() {
        new GetOrdersTask(dataBase, orders_rv, adapter, ordersList).execute();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        List<Orders> tempList=new ArrayList<>(dataBase.orderDao().getAllOrders());
                for (Orders order:ordersList){
                    if (dataBase.orderDao().getPickLaterOrderByID(order.getID())!=null){
                        tempList.remove(order);
                    }
            ordersList=new ArrayList<>(tempList);
            adapter.setOrdersList(ordersList);
        }
    }

    @Override
    public void onBackPressed() {
        ordersList.clear();
        super.onBackPressed();
    }
}

class GetOrdersTask extends AsyncTask<Void, Orders, Void> {

    GetOrdersTask(DataBase dataBase, RecyclerView orders_rv,
                  OrdersRVA adapter, List<Orders> ordersList) {
        this.dataBase = dataBase;
        this.adapter = adapter;
        this.ordersList = ordersList;
        this.orders_rv = orders_rv;
    }

    private OrdersRVA adapter;
    private DataBase dataBase;
    private List<Orders> ordersList;
    private RecyclerView orders_rv;


    @Override
    protected Void doInBackground(Void... voids) {
        try {
            ManagedChannel managedChannel = ManagedChannelBuilder
                    .forAddress(ip, 5067).usePlaintext().build();
            WorkWithOrdersGrpc.WorkWithOrdersBlockingStub stub =
                    WorkWithOrdersGrpc.newBlockingStub(managedChannel);
            Empty emptyRequest = Empty.newBuilder().build();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                stub.getAllOrders(emptyRequest).forEachRemaining(order_ -> {
                    Orders order = new Orders(order_.getId(), order_.getName(),
                            order_.getCode1(), order_.getCode2(), order_.getPickProblem());
                    if (dataBase.orderDao().getOrderByID(order_.getId()) == null) {
                        dataBase.orderDao().createOrder(order);
                    }else {
                        dataBase.orderDao().updateOrder(order);
                    }
                    if (dataBase.orderDao().getPickLaterOrderByID(order.getID())==null) {
                        Orders[] orders = new Orders[]{order};
                        publishProgress(orders);
                    }
                });
            }
            managedChannel.shutdown().awaitTermination(15, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Orders... values) {
        ordersList.add(values[0]);
        adapter.notifyItemInserted(ordersList.size());
        orders_rv.smoothScrollToPosition(ordersList.size());
    }
}

