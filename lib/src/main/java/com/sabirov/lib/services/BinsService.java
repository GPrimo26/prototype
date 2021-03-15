package com.sabirov.lib.services;

import com.sabirov.lib.Stubs;

import javax.rmi.CORBA.Stub;

import io.grpc.stub.StreamObserver;
import warehouse.Bin;
import warehouse.Empty;
import warehouse.ID;
import warehouse.WorkWithBinsGrpc;

public class BinsService extends WorkWithBinsGrpc.WorkWithBinsImplBase {
    @Override
    public void getAllBins(Empty request, StreamObserver<Bin> responseObserver) {
        super.getAllBins(request, responseObserver);
    }

    @Override
    public StreamObserver<ID> getBinByItemId(StreamObserver<Bin> responseObserver) {
        return new StreamObserver<ID>() {
            @Override
            public void onNext(ID value) {
                boolean flag=false;
                for (Bin bin: Stubs.binsList){
                    if (bin.getId()==value.getId()){
                        responseObserver.onNext(bin);
                        flag=true;
                        break;
                    }
                }
                if (!flag){
                    responseObserver.onNext(Bin.newBuilder().setName("Not in the bin").build());
                }
            }

            @Override
            public void onError(Throwable t) {
                String text="Error while loading data: "+t.getMessage();
                responseObserver.onError(new Throwable(text));
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }
}
