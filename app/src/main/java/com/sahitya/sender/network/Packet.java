package com.sahitya.sender.network;

import com.android.volley.Header;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Packet<T> implements Serializable {
    @SerializedName("header")
    public Header header;
    @SerializedName("body")
    public T body;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {this.header = header;}

    public T getBody() {return body;}

    public void  setBody(T body) {this.body = body;}
}
