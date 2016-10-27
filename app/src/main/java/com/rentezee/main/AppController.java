package com.rentezee.main;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

/**
 * Created by JitendraSingh on 08,Sep 2016.
 * Email jitendra@live.in
 */
public class AppController extends Application {
    private static AppController appController; //Instance of this application class
    private RequestQueue requestQueue; //volley request queue

    @Override
    public void onCreate() {
        super.onCreate();
        appController=this;
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }

    public static  synchronized AppController getInstance(){
        return appController;
    }

    private RequestQueue getRequestQueue(){
        if(requestQueue==null){
            requestQueue= Volley.newRequestQueue(getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request){
        getRequestQueue().add(request);
    }

    public <T> void addToRequestQueue(Request<T> request, String tag){
        request.setTag(tag);
        getRequestQueue().add(request);
    }

    public void cancelPendingRequest(String tag){
        if(requestQueue!=null) {
            requestQueue.cancelAll(tag);
        }
    }
}
