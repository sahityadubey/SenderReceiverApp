package com.sahitya.receiver.network;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class NetworkHandler {

    private static Context context;
    private static NetworkHandler instance = null;
    public RequestQueue requestQueue;

    public NetworkHandler(Context con) {
        context = con;
        try {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized NetworkHandler getInstance(Context context) {
        if (null == instance)
            instance = new NetworkHandler(context);
        return instance;
    }

    public static synchronized NetworkHandler getInstance() {
        if (null == instance) {
            throw new IllegalStateException(NetworkHandler.class.getSimpleName() +
                    " is not initialized, call getInstance(...) first");
        }
        return instance;
    }

    public void PostReq(String url, JSONObject reqObj, final ResponseListener<PacketObj> res) {
        JSONObject requestJsonObject = null;
        try {
            requestJsonObject = reqObj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.POST, url,
                requestJsonObject,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Toast.makeText(context, response.toString(), Toast.LENGTH_LONG).show();
                   PacketObj packetObj = new Gson().fromJson(response.toString(), PacketObj.class);
                    if (res != null) {
                        res.getResult(packetObj);
                    }
                } catch (Exception ex) {
                    if (res != null) {
                        res.onErrorResponse(new Throwable());
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        JSONObject obj = new JSONObject(res);
                    } catch (UnsupportedEncodingException e1) {
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                        e2.printStackTrace();
                    }
                }
                return ;
            }
        })
        {
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        try {
            request.getHeaders();
        } catch (Exception e) {
            e.printStackTrace();
        }
        request.setRetryPolicy(new DefaultRetryPolicy(
                400000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }

    public void GetReq(String url, final ResponseListener<PacketObj> res) {
        JSONObject requestJsonObject = null;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,
                requestJsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    PacketObj packetObj = new Gson().fromJson(response.toString(), PacketObj.class);
                    if (res != null) {
                        res.getResult(packetObj);
                    }
                } catch (Exception ex) {
                    if (res != null) {
                        res.onErrorResponse(new Throwable());
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(context, "Time Out Error!" , Toast.LENGTH_LONG).show();
                }
                if (res != null) {
                    res.onErrorResponse(error);
                }

                if (error == null || error.networkResponse == null) {
                    return;
                }

                String body;
                //get status code here
                final String statusCode = String.valueOf(error.networkResponse.statusCode);
                //get response body and parse with appropriate encoding
                try {
                    body = new String(error.networkResponse.data, "UTF-8");
                } catch (Exception e) {
                    // exception
                    e.printStackTrace();
                }
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                40000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }
}