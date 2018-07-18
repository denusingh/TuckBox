package com.rahulsinghkamboj.android.tuckbox.Utils;



public class TextUtils {

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();  //For Email validations
        }
    }
}
