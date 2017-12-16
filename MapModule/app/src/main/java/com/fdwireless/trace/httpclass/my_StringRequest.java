package com.fdwireless.trace.httpclass;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

/**
 * Created by 汪励颢 on 2017/2/6.
 */

public class my_StringRequest<T> extends my_BaseRequest {
    public my_StringRequest(Context pContext){
        super(pContext);
    }
    public void get(String url, final com.fdwireless.trace.httpclass.my_CallBackListener<T> listener){
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(null != listener){
                    listener.onSuccessResponse((T) response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(null != listener){
                    listener.onErrorResponse(error);
                }
            }
        });
        addRequest(stringRequest);
    }
    public void post(String url, final com.fdwireless.trace.httpclass.my_CallBackListener<T> listener, Map<String,String>params){
        Post_StringRequest postStringRequest=new Post_StringRequest(url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(null != listener){
                    listener.onSuccessResponse((T) response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(null != listener){
                    listener.onErrorResponse(error);
                }
            }
        },params);
        addRequest(postStringRequest);
    }

}
