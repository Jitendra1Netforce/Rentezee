package com.rentezee.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.rentezee.helpers.Constants;
import com.rentezee.helpers.Util;

/**
 * Created by JitendraSingh on 10/15/2016.
 */

public class NoInternetReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent noInternetIntent = new Intent(Constants.NO_INTERNET_BROADCAST_RECEIVER);
        noInternetIntent.putExtra(Constants.noInternet, Util.isConnectedToInternet(context));
        LocalBroadcastManager.getInstance(context).sendBroadcast(noInternetIntent);

    }
}
