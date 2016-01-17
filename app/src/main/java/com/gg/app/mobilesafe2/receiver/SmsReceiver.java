package com.gg.app.mobilesafe2.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsManager;

import com.gg.app.mobilesafe2.R;
import com.gg.app.mobilesafe2.service.LocationService;
import com.gg.app.mobilesafe2.utils.PreferenceUtils;
import com.gg.app.mobilesafe2.utils.SmsUtils;

/**
 * android4.4之后google添加了default sms机制,所以这里的代码是拦截不成功的
 */
public class SmsReceiver extends BroadcastReceiver {
    DevicePolicyManager mDPM;
    ComponentName componentName;

    public SmsReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Boolean protect = PreferenceUtils.getBoolean(context, "protect", false);
        if (!protect) {
            return;
        }

        Object[] objects = (Object[]) intent.getExtras().get("pdus");
        if (objects == null) {
            return;
        }

        for (Object object : objects) {
            String originatingAddress = SmsUtils.getOriginatingAddress((byte[]) object);
            if (!originatingAddress.equals(PreferenceUtils.getString(context, "bind_phone", ""))) {
                return;
            }

            // 获取设备策略服务,在设备激活的情况下才能进行远程操作
            mDPM = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
            componentName = new ComponentName(context, AdminReceiver.class);

            String messageBody = SmsUtils.getMessageBody((byte[]) object);
            if ("#*alarm*#".equals(messageBody)) {
                MediaPlayer player = MediaPlayer.create(context, R.raw.alarm);
                //设置音量，左声道和右声道最大值
                player.setVolume(1f, 1f);
                player.setLooping(true);
                player.start();
                abortBroadcast();

            } else if ("#*location*#".equals(messageBody)) {
                context.startService(new Intent(context, LocationService.class));
                String location = PreferenceUtils.getString(context, "location", "正在获取中，稍后再询问...");
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(originatingAddress, null, location, null, null);
                abortBroadcast();

            } else if ("#*lockScreen*#".equals(messageBody)) {
                if (mDPM.isAdminActive(componentName)) {
                    mDPM.lockNow();
                    // String newPassword = PreferenceUtils.getString(context, "bind_phone", "1314");
                    mDPM.resetPassword("1314", 0);
                }
                abortBroadcast();
            } else if ("#*wipeData*#".equals(messageBody)) {
                if (mDPM.isAdminActive(componentName)) {
                    mDPM.wipeData(0);
                }
                abortBroadcast();

            } else if ("#*wipeAllData*#".equals(messageBody)) {
                if (mDPM.isAdminActive(componentName)) {
                    mDPM.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
                }
                abortBroadcast();
            }
        }
    }
}
