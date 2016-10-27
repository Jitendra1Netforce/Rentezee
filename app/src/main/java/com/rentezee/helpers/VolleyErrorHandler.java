package com.rentezee.helpers;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.rentezee.main.R;


/**
 * Created by JitendraSingh on 10,Sep 2016.
 * Email jitendra@live.in
 */
public class VolleyErrorHandler {
    /**
     * Returns appropriate message which is to be displayed to the user
     * against the specified error object.
     *
     * @param error
     * @param context
     * @return
     */
    public static String getMessage(Context context, Object error) {
        if (error instanceof TimeoutError) {
            return context.getResources().getString(R.string.timeout_error);
        } else if (isServerProblem(error)) {
            return handleServerError(context, error);
        } else if (isNetworkProblem(error)) {
            return context.getResources().getString(R.string.network_error);
        }
        return context.getResources().getString(R.string.generic_error);
    }

    /**
     * Determines whether the error is related to network
     *
     * @param error
     * @return
     */
    private static boolean isNetworkProblem(Object error) {
        return (error instanceof NetworkError);
    }

    /**
     * Determines whether the error is related to server
     *
     * @param error
     * @return
     */
    private static boolean isServerProblem(Object error) {
        return (error instanceof ServerError) || (error instanceof AuthFailureError);
    }

    /**
     * Handles the server error, tries to determine whether to show a stock message or to
     * show a message retrieved from the server.
     *
     * @param err
     * @param context
     * @return
     */
    private static String handleServerError(Context context, Object err) {
        VolleyError error = (VolleyError) err;

        NetworkResponse response = error.networkResponse;

        if (response != null) {
            switch (response.statusCode) {
                case 401:
                    return context.getResources().getString(R.string.unauthorized_error);

                case 404:
                    return context.getResources().getString(R.string.not_found_error);
            }
        }
        return context.getResources().getString(R.string.server_error);
    }
}
