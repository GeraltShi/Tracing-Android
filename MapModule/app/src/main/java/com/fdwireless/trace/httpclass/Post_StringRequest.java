package com.fdwireless.trace.httpclass;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

/**
 * Created by 汪励颢 on 2017/2/8.
 */

public class Post_StringRequest extends StringRequest {
    protected Map<String,String> mParams=null;
    public void setParams(Map<String,String> params) {
        this.mParams = params;
    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
      return mParams;
    }
    public Post_StringRequest(String url, Response.Listener<String> listener,
                              Response.ErrorListener errorListener,Map<String,String> params){
        super(Method.POST, url, listener,errorListener);
        setParams(params);
    }
}
