package com.gg.app.mobilesafe2.utils;

import android.annotation.TargetApi;
import android.os.Build;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

/**
 * author cipherGG
 * Created by Administrator on 2015/12/23.
 * describe
 */
public class SmsUtils {

    public static SmsManager getSmsManager() {
        return SmsManager.getDefault();
    }

    public static SmsMessage getSmsMessage(byte[] pdu) {
        return SmsMessage.createFromPdu(pdu);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static SmsMessage getSmsMessage(byte[] pdu, String format) {
        return SmsMessage.createFromPdu(pdu, format);
    }

    public static String getOriginatingAddress(byte[] pdu) {
        SmsMessage message = SmsMessage.createFromPdu(pdu);
        return message.getOriginatingAddress();
    }

    public static String getOriginatingAddress(SmsMessage message) {
        return message.getOriginatingAddress();
    }

    public static String getMessageBody(byte[] pdu) {
        SmsMessage message = SmsMessage.createFromPdu(pdu);
        return message.getMessageBody();
    }

    public static String getMessageBody(SmsMessage message) {
        return message.getMessageBody();
    }

}
