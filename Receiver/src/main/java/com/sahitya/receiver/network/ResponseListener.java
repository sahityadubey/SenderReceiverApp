package com.sahitya.receiver.network;

public interface ResponseListener<T> {
    public void getResult(T object);
    public void onErrorResponse(Throwable t);
}
