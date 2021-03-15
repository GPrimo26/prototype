package com.sabirov.warehouse.activities;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sabirov.warehouse.OrderItemsViewModel;
import com.sabirov.warehouse.PickItemDialog;
import com.sabirov.warehouse.R;
import com.sabirov.warehouse.adapters.ItemsRVA;
import com.sabirov.warehouse.db.DataBase;
import com.sabirov.warehouse.db.tables.Bins;
import com.sabirov.warehouse.db.tables.Items;
import com.sabirov.warehouse.db.tables.Orders;
import com.sabirov.warehouse.db.tables.PickLater;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import warehouse.Bin;
import warehouse.ID;
import warehouse.WorkWithBinsGrpc;
import warehouse.WorkWithItemsGrpc;

import static com.sabirov.warehouse.Variables.ip;

public class OrderItemsActivity extends AppCompatActivity {

    private Orders order;
    private ItemsRVA adapter;
    private RecyclerView items_rv;
    private List<Items> itemsList;
    private List<Bins> binsList;
    private OnTaskCompleted onTaskCompleted;
    private DataBase dataBase;
    private OrderItemsViewModel orderItemsViewModel;
    private Button complete_btn;


    public interface OnTaskCompleted{
        void onTaskCompleted(List<Items> itemsList, List<Bins> binsList);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_items);
        order=getIntent().getExtras().getParcelable("order");
        dataBase=DataBase.getInstance(OrderItemsActivity.this);
        orderItemsViewModel=new ViewModelProvider(OrderItemsActivity.this)
                .get(OrderItemsViewModel.class);

        findIDs();
        setInfo();
        setActions();

        new GetItemsByOrderIdTask(itemsList, onTaskCompleted, dataBase).execute(order);

    }

    private void findIDs() {
        items_rv=findViewById(R.id.items_rv);
        complete_btn=findViewById(R.id.complete_btn);
    }

    private void setInfo() {
        itemsList=new ArrayList<>();
        binsList=new ArrayList<>();
        adapter=new ItemsRVA(OrderItemsActivity.this, itemsList, binsList);
        orderItemsViewModel.setItem(itemsList);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,
                RecyclerView.VERTICAL, false);
        items_rv.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(items_rv.getContext(),
                        linearLayoutManager.getOrientation());
        items_rv.addItemDecoration(dividerItemDecoration);
        items_rv.setAdapter(adapter);

    }

    private void setActions() {
        onTaskCompleted= (itemsList_, binsList_) -> {
            itemsList=itemsList_;
            binsList=binsList_;
            OrderItemsActivity.this.runOnUiThread(() -> {
                adapter.setBinsList(binsList);
                adapter.notifyDataSetChanged();
                orderItemsViewModel.setItem(itemsList);
            });
        };
        adapter.setOnItemClickListener((position, item) -> {
            if (item.getQuantity()!=0) {
                orderItemsViewModel.setPostion(position);
                PickItemDialog pickItemDialog = new PickItemDialog();
                pickItemDialog.show(getSupportFragmentManager(), "piDialog");
            }else {
                Toast.makeText(OrderItemsActivity.this,
                        "Nothing to pick", Toast.LENGTH_SHORT).show();
            }
        });
        orderItemsViewModel.getItem().observe(OrderItemsActivity.this, items -> {
            dataBase.itemsDao().updateItems(items);
            itemsList=new ArrayList<>(items);
            adapter.notifyDataSetChanged();
        });
        complete_btn.setOnClickListener(v->{
            boolean flag=true;
            for (Items item: itemsList){
                if (item.getQuantity()!=0){
                    flag=false;
                    break;
                }
            }
            if (!flag){
                AlertDialog.Builder builder=new AlertDialog.Builder(OrderItemsActivity.this);
                builder.setTitle("Attention!").setMessage("Not all items were collected.\n" +
                        "The order will be placed in \"Pick later\".\n" +
                        "Are you sure you want to continue?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PickLater pickLater=new PickLater();
                        pickLater.setOrderID(order.getID());
                        dataBase.orderDao().addToPickLater(pickLater);
                        dialogInterface.dismiss();
                        finish();
                    }
                }).setNegativeButton("No", (dialogInterface, i)
                        -> dialogInterface.dismiss()).show();
            }else {
                dataBase.orderDao().deleteOrder(order);

                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}

class GetItemsByOrderIdTask extends AsyncTask<Orders, Void, Void>{
    GetItemsByOrderIdTask(List<Items> itemsList,
                          OrderItemsActivity.OnTaskCompleted onTaskCompleted, DataBase dataBase) {
        this.itemsList=itemsList;
        this.onTaskCompleted=onTaskCompleted;
        this.dataBase=dataBase;
    }
    private List<Items> itemsList;
    private OrderItemsActivity.OnTaskCompleted onTaskCompleted;
    private DataBase dataBase;

    @Override
    protected Void doInBackground(Orders... orders) {
        try {
            List<Bins> binsList = new ArrayList<>();
            ManagedChannel managedChannel = ManagedChannelBuilder
                    .forAddress(ip, 5067).usePlaintext().build();
            WorkWithItemsGrpc.WorkWithItemsBlockingStub stub =
                    WorkWithItemsGrpc.newBlockingStub(managedChannel);
            ID request = ID.newBuilder().setId(orders[0].getID()).build();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                stub.getItemsByOrderId(request).forEachRemaining(item_ -> {
                    Items item = new Items(item_.getId(), item_.getName(), item_.getOrderId(),
                            item_.getCode1(), item_.getCode2(), item_.getDescription(),
                            item_.getBinId(), item_.getQuantity());
                    if (dataBase.itemsDao().getItemById(item.getItemID())==null){
                        dataBase.itemsDao().createItem(item);
                    }else {
                        dataBase.itemsDao().updateItemInfo(item);
                    }
                    itemsList.add(item);
                });
                if (itemsList.size()!=0) {
                    WorkWithBinsGrpc.WorkWithBinsStub stub1 =
                            WorkWithBinsGrpc.newStub(managedChannel);
                    AtomicReference<StreamObserver<ID>> requestObserverRef =
                            new AtomicReference<>();
                    CountDownLatch finishedLatch = new CountDownLatch(1);
                    final int[] i = {0};
                    StreamObserver<ID> binStreamObserver = stub1
                            .getBinByItemId(new StreamObserver<Bin>() {
                        @Override
                        public void onNext(Bin value) {
                            Bins bin = new Bins();
                            bin.setBinID(value.getId());
                            bin.setName(value.getName());
                            bin.setCode1(value.getCode1());
                            bin.setCode2(value.getCode2());
                            if (dataBase.binsDao().getBinByID(bin.getBinID())==null){
                                dataBase.binsDao().createBin(bin);
                            }else {
                                dataBase.binsDao().updateBin(bin);
                            }
                            binsList.add(bin);
                            i[0]++;
                            if (i[0] == itemsList.size()) {
                                onCompleted();
                            } else {
                                requestObserverRef.get().onNext(ID.newBuilder()
                                        .setId(itemsList.get(i[0]).getBinID()).build());
                            }
                        }

                        @Override
                        public void onError(Throwable t) {
                            t.printStackTrace();
                        }

                        @Override
                        public void onCompleted() {
                            finishedLatch.countDown();
                        }
                    });
                    requestObserverRef.set(binStreamObserver);
                    binStreamObserver.onNext(ID.newBuilder()
                            .setId(itemsList.get(i[0]).getBinID()).build());
                    finishedLatch.await();
                    binStreamObserver.onCompleted();
                }
            }
            managedChannel.shutdownNow();
            onTaskCompleted.onTaskCompleted(itemsList, binsList);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}



