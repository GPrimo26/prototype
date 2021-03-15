package com.sabirov.lib.services;

import com.sabirov.lib.Stubs;

import io.grpc.stub.StreamObserver;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import warehouse.Empty;
import warehouse.ID;
import warehouse.Item;
import warehouse.TotalItemsNumber;
import warehouse.WorkWithItemsGrpc;

public class ItemsService extends WorkWithItemsGrpc.WorkWithItemsImplBase {
    @Override
    public void getAllItems(Empty request, StreamObserver<Item> responseObserver) {
        Observable<Item> taskObservable=Observable.fromIterable(Stubs.itemsList).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread());
        taskObservable.subscribe(new Observer<Item>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Item item) {
                responseObserver.onNext(item);
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
    public void getItemById(ID request, StreamObserver<Item> responseObserver) {
        super.getItemById(request, responseObserver);
    }

    @Override
    public void setItemsNumber(TotalItemsNumber request, StreamObserver<Empty> responseObserver) {
        super.setItemsNumber(request, responseObserver);
    }

    @Override
    public void getItemsByOrderId(ID request, StreamObserver<Item> responseObserver) {
        Observable<Item> taskObservable=Observable.fromIterable(Stubs.itemsList).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread());
        taskObservable.subscribe(new Observer<Item>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Item item) {
                if (item.getOrderId()==request.getId()) {
                    responseObserver.onNext(item);
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
    public void putItemIntoBin(ID request, StreamObserver<Empty> responseObserver) {
        super.putItemIntoBin(request, responseObserver);
    }
}
