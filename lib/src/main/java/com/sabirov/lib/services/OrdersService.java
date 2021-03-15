package com.sabirov.lib.services;

import com.sabirov.lib.Stubs;

import javax.rmi.CORBA.Stub;
import javax.swing.text.Style;

import io.grpc.stub.StreamObserver;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import warehouse.Empty;
import warehouse.ID;
import warehouse.Order;
import warehouse.WorkWithOrdersGrpc;

public class OrdersService extends WorkWithOrdersGrpc.WorkWithOrdersImplBase {
    @Override
    public void getAllOrders(Empty request, StreamObserver<Order> responseObserver) {
        Observable<Order> taskObservable=Observable.fromIterable(Stubs.ordersList).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread());
        taskObservable.subscribe(new Observer<Order>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Order order) {
                responseObserver.onNext(order);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
                responseObserver.onCompleted();
            }
        });
    }

    @Override
    public void getOrderById(ID request, StreamObserver<Order> responseObserver) {
        boolean flag=false;
        for(Order order: Stubs.ordersList){
            if (order.getId()==request.getId()){
                responseObserver.onNext(order);
                responseObserver.onCompleted();
                flag=true;
                break;
            }
        }
        if (!flag){
            responseObserver.onError(new Throwable("No matches"));
        }
    }

    @Override
    public void orderAssembled(ID request, StreamObserver<Empty> responseObserver) {
        super.orderAssembled(request, responseObserver);
    }

    @Override
    public void pickOrderLater(ID request, StreamObserver<Empty> responseObserver) {
        super.pickOrderLater(request, responseObserver);
    }

    @Override
    public void setPickProblem(Order request, StreamObserver<Empty> responseObserver) {
        super.setPickProblem(request, responseObserver);
    }
}
