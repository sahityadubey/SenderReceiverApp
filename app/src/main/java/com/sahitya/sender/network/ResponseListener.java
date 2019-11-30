package com.sahitya.sender.network;

public interface ResponseListener<T> {
    public void getResult(T object);
    public void onErrorResponse(Throwable t);
}
