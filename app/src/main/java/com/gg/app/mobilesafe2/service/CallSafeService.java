package com.gg.app.mobilesafe2.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.SmsMessage;
import android.util.Log;

import com.gg.app.mobilesafe2.db.dao.BlackNumberDao;
import com.gg.app.mobilesafe2.utils.SmsUtils;

/**
 * 拦截不了 所以就不实例了
 */
public class CallSafeService extends Service {

    private BlackNumberDao dao;

    public CallSafeService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        dao = new BlackNumberDao(this);

        IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        filter.setPriority(Integer.MAX_VALUE);
        registerReceiver(innerReceiver, filter);
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(innerReceiver);
        super.onDestroy();
    }

    private BroadcastReceiver innerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Object[] objects = (Object[]) intent.getExtras().get("pdus");

            if (objects == null) {
                return;
            }

            for (Object object : objects) {
                String originatingAddress = SmsUtils.getOriginatingAddress((byte[]) object);

                String mode = dao.query(originatingAddress);

                if (mode.equals("1") || mode.equals("3")) {
                    abortBroadcast();
                }
            }
        }
    };
}
