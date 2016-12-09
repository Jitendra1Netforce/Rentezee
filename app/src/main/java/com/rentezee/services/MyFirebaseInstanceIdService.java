package com.rentezee.services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.rentezee.helpers.Debugger;


/**
 * Created by JitendraSingh on 12,Oct 2016.
 * Email jitendra@live.in
 */
public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
    private static final String TAG="MyFirebaseInstanceIdService";

    public  static String refreshedToken;

    /**
     * The onTokenRefresh callback fires whenever a new token is generated,
     * so calling getToken in its context ensures that you are accessing a
     * current, available registration token
     */
    @Override
    public void onTokenRefresh()
    {
        // Get updated InstanceID token.
         refreshedToken = FirebaseInstanceId.getInstance().getToken();

        Debugger.d(TAG, "Refreshed token: " + refreshedToken);
        Log.e("token----------", refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String refreshedToken){



    }
}
