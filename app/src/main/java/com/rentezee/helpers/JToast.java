package com.rentezee.helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by JitendraSingh on 19,Mar 2016.
 * Email jitendra@live.in
 */
public class JToast {
    private static Toast toast;
    private static int LENGTH_LONG=Toast.LENGTH_LONG;
    private static int LENGTH_SHORT=Toast.LENGTH_SHORT;
    @SuppressLint("ShowToast")
    public static Toast makeText(Context context, String text, int duration) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, text, duration);
        return toast;
    }
    public void show(){
        toast.show();
    }
}
