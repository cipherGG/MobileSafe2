package com.gg.app.mobilesafe2.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.gg.app.mobilesafe2.R;
import com.gg.app.mobilesafe2.utils.BackUpUtils;
import com.gg.app.mobilesafe2.utils.DialogUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

@ContentView(R.layout.activity_back_up)
public class BackUpActivity extends BaseActivity {

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Event(value = {R.id.btn_back_sms_to_sdcard})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back_sms_to_sdcard:
                backUpSms();
                break;


        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    dialog = DialogUtils.showProgressDialog(BackUpActivity.this, "提示", "正在备份中，请勿离开", false);
                    break;
                case 2:
                    dialog.dismiss();
                    BackUpActivity.this.showToast("备份成功");
                    break;
                case 3:
                    dialog.dismiss();
                    BackUpActivity.this.showToast("备份失败");
                    break;

            }
        }
    };

    //注意看这里的ui更新可以在子线程
    private void backUpSms() {
        new Thread() {
            @Override
            public void run() {
                handler.sendEmptyMessage(1);
                if (BackUpUtils.backUpSmsSdCard(BackUpActivity.this)) {
                    //Looper.prepare();
                    //showToast("备份成功");
                    //Looper.loop();
                    handler.sendEmptyMessage(2);
                } else {
                    //Looper.prepare();
                    //showToast("备份失败");
                    //Looper.loop();
                    handler.sendEmptyMessage(3);
                }
            }
        }.start();
    }


}
