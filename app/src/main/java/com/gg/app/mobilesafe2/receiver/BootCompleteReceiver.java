package com.gg.app.mobilesafe2.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.text.TextUtils;

import com.gg.app.mobilesafe2.utils.PreferenceUtils;
import com.gg.app.mobilesafe2.utils.SmsUtils;
import com.gg.app.mobilesafe2.utils.TelephonyUtils;

public class BootCompleteReceiver extends BroadcastReceiver {
    public BootCompleteReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Boolean protect = PreferenceUtils.getBoolean(context, "protect", false);
        if (!protect) {
            return;
        }

        String sim = PreferenceUtils.getString(context, "sim", null);
        if (TextUtils.isEmpty(sim)) {
            return;
        }

        String currentSim = TelephonyUtils.getSimNumber(context);
        if (sim.equals(currentSim)) {
            return;
        }

        String phone = PreferenceUtils.getString(context, "bind_phone", null);
        if (TextUtils.isEmpty(phone)) {
            return;
        }

        SmsManager smsManager = SmsUtils.getSmsManager();
        smsManager.sendTextMessage(phone, null,
                "您的号码为" + phone + "的朋友手机SIM卡被换，可发送短信指令进行追踪！" +
                        "\n1.GPS追踪:#*location*#" +
                        "\n2.播放报警音乐:#*alarm*#" +
                        "\n3.远程删除内存数据:#*wipeData*#" +
                        "\n4.远程删除所有数据:#*wipeAllData*#" +
                        "\n5.远程锁屏:#*lockScreen*#", null, null);
    }
}
