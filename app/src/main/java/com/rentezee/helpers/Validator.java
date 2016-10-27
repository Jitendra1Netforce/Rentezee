package com.rentezee.helpers;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.util.Patterns;

import com.rentezee.main.R;

import java.util.regex.Pattern;


/**
 * Created by JitendraSingh on 13,Sep 2016.
 * Email jitendra@live.in
 */
public class Validator {
    private static final Pattern NAME = Pattern.compile("([a-zA-Z]{3,30}\\s*)+");
    private static final Pattern PIN = Pattern.compile("^[1-9][0-9]{5}$");
    private static final Pattern MOBILE = Pattern.compile("^[7-9][0-9]{9}$");


    public static boolean isValidEmail(String str) {
        return isValidEmail(null, str, null);
    }


    public static boolean isValidEmail(Context context, String str, TextInputLayout textInputLayout) {
        if(textInputLayout!=null){
            textInputLayout.setError(null);
        }
        if (str.isEmpty()) {
            if (textInputLayout != null) {
                textInputLayout.setError(context.getString(R.string.error_email_empty));
            }
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(str).matches()) {
            if (textInputLayout != null) {
                textInputLayout.setError(context.getString(R.string.error_email_not_valid));
            }
            return false;
        }
        return true;
    }

    public static boolean isValidName(String str) {
        return isValidName(null, str, null);
    }

    public static boolean isValidName(Context context, String str, TextInputLayout textInputLayout) {
        if(textInputLayout!=null){
            textInputLayout.setError(null);
        }
        if (!NAME.matcher(str).matches()) {
            if(textInputLayout!=null){
                textInputLayout.setError(context.getString(R.string.error_name));
            }
            return false;
        }
        return true;
    }

    public static boolean isValidMobile(String str) {
        return isValidMobile(null, str, null);
    }

    public static boolean isValidMobile(Context context, String str, TextInputLayout textInputLayout) {
        if(textInputLayout!=null){
            textInputLayout.setError(null);
        }
        if (str.isEmpty()) {
            if (textInputLayout != null) {
                textInputLayout.setError(context.getString(R.string.error_mobile_empty));
            }
            return false;
        } else if (!MOBILE.matcher(str).matches()) {
            if (textInputLayout != null) {
                textInputLayout.setError(context.getString(R.string.error_mobile_not_valid));
            }
            return false;
        }
        return true;
    }

    public static boolean isValidPassword(String password) {
        return isValidPassword(null, password, null);
    }

    public static boolean isValidPassword(Context context, String password, TextInputLayout textInputLayout) {
        if(textInputLayout!=null){
            textInputLayout.setError(null);
        }
        if (password.contains(" ")) {
            if (textInputLayout != null) {
                textInputLayout.setError(context.getString(R.string.error_pwd_no_space));
            }
            return false;
        } else if (password.length() < 6 || password.length()>10) {
            if (textInputLayout != null) {
                textInputLayout.setError(context.getString(R.string.error_pwd_min_chars));
            }
            return false;
        }
        return true;
    }
}
