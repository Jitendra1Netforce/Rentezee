package com.rentezee.helpers;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by JitendraSingh on 12,Sep 2016.
 * Email jitendra@live.in
 */
public class VolleyGsonRequest<T> extends JsonRequest<T> {
    private Gson gson = new Gson();
    private Class<T> clazz;
    private Map<String, String> headers;
    private Response.Listener<T> listener;
    HashMap<String, String> headers_post = new HashMap<String, String>();


    public VolleyGsonRequest(String url,
                             JSONObject jsonObject,
                             Response.Listener<T> listener,
                             Response.ErrorListener errorListener,
                             Class<T> clazz,
                             Map<String, String> headers) {

        super(Method.POST, url, (jsonObject == null) ? null : jsonObject.toString(), listener, errorListener);
        this.clazz = clazz;
        this.headers = headers;
        this.listener = listener;
    }

    @Override
    public Request<?> setRetryPolicy(RetryPolicy retryPolicy) {
        retryPolicy=new DefaultRetryPolicy(
                60*1000 /*seconds*/,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        return super.setRetryPolicy(retryPolicy);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers));

            System.out.println("json==========="+json);

            return Response.success(
                    gson.fromJson(json, clazz),
                    HttpHeaderParser.parseCacheHeaders(response));


        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }



}
