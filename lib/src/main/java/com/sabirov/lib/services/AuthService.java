package com.sabirov.lib.services;

import io.grpc.stub.StreamObserver;
import userauth.AuthorizationGrpc;
import userauth.LoginRequest;
import userauth.LogoutRequest;
import userauth.Response;
import userauth.User;

public class AuthService extends AuthorizationGrpc.AuthorizationImplBase {
    @Override
    public void logIn(LoginRequest request, StreamObserver<Response> responseObserver) {
        Response.Builder response= Response.newBuilder();
        String email=request.getUserName();
        String password=request.getPassword();

        response.setMessage("Welcome!")
                .setCode(1)
                .setUser(User.newBuilder()
                        .setUserId(1)
                        .setEmail(email)
                        .setPhoneNumber("+1234")
                        .setFirstName("FName")
                        .setLastName("LName"));

        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    @Override
    public void logOut(LogoutRequest request, StreamObserver<Response> responseObserver) {
        super.logOut(request, responseObserver);
    }
}
