package com.sabirov.lib;

import com.sabirov.lib.services.AuthService;
import com.sabirov.lib.services.BinsService;
import com.sabirov.lib.services.ItemsService;
import com.sabirov.lib.services.OrdersService;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.grpc.netty.NettyServerBuilder;
import warehouse.Bin;
import warehouse.Item;
import warehouse.Order;
import warehouse.TotalItemsNumber;

import static com.sabirov.lib.Stubs.binsList;
import static com.sabirov.lib.Stubs.itemsList;
import static com.sabirov.lib.Stubs.ordersList;
import static io.grpc.okhttp.internal.Platform.logger;

public class MyServer {
    private void start() throws IOException, InterruptedException {
        int port = 5067;
        SocketAddress socketAddress = new InetSocketAddress("0.0.0.0", port);
        NettyServerBuilder server = NettyServerBuilder.forAddress(socketAddress)
                .addService(new AuthService())
                .addService(new ItemsService())
                .addService(new BinsService())
                .addService(new OrdersService());
        server.build().start().awaitTermination();
        logger.info("Server started, listening on " + port);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        // Making some stubs.
        itemsList=new ArrayList<>();
        binsList=new ArrayList<>();
        ordersList =new ArrayList<>();
        for(int i=1; i<=6; i++){
            String name="name "+i;
            int code1= new Random().nextInt(9999 - 1000) + 1000;
            int code2= new Random().nextInt(999 - 100) + 100;
            String description="Description "+i;
            int binId=-1;
            int orderId=-1;
            int quantity=new Random().nextInt(20)+1;
            itemsList.add(Item.newBuilder().setId(i).setName(name).setCode1(code1).setCode2(code2)
                    .setDescription(description).setBinId(binId).setOrderId(orderId)
                    .setQuantity(quantity).build());
            code1=new Random().nextInt(134566 - 1000) + 1000;
            code2=new Random().nextInt(1424 - 100) + 100;
            name="Bin "+i;
            binsList.add(Bin.newBuilder().setId(i).setName(name).setCode1(code1).setCode2(code2)
                    .addAllItems(new ArrayList<>()).build());
            code1=new Random().nextInt(100);
            code2=new Random().nextInt(500);
            ordersList.add(Order.newBuilder().setId(i).setName(name).setCode1(code1).setCode2(code2)
                   .setPickProblem("").build());

        }


        // Add item to bin example.
        Bin.Builder builder=binsList.get(0).toBuilder();
        builder.addItems(itemsList.get(0));
        binsList.set(0, builder.build());
        itemsList.set(0, itemsList.get(0).toBuilder().setBinId(binsList.get(0).getId()).build());

        // Item to Order
        itemsList.set(0, itemsList.get(0).toBuilder().setOrderId(ordersList.get(0).getId()).build());
        itemsList.set(1, itemsList.get(1).toBuilder().setOrderId(ordersList.get(0).getId()).build());
        itemsList.set(2, itemsList.get(2).toBuilder().setOrderId(ordersList.get(0).getId()).build());
        itemsList.set(3, itemsList.get(3).toBuilder().setOrderId(ordersList.get(0).getId()).build());

        final MyServer server = new MyServer();
        server.start();
    }
}
