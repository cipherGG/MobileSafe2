package com.gg.app.mobilesafe2.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * author cipherGG
 * Created by Administrator on 2015/12/23.
 * describe
 */
public class TelephonyUtils {

    public static TelephonyManager getTelephonyManager(Context context) {
        return (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    public static String getSimNumber(TelephonyManager telephonyManager) {
        return telephonyManager.getSimSerialNumber();
    }

    public static String getSimNumber(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getSimSerialNumber();
    }

}
