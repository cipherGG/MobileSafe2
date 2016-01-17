package com.gg.app.mobilesafe2.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

import com.gg.app.mobilesafe2.R;
import com.gg.app.mobilesafe2.utils.PreferenceUtils;
import com.gg.app.mobilesafe2.utils.TelephonyUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.view.annotation.Event;

@ContentView(R.layout.activity_seek)
public class SeekActivity extends BaseActivity {
    @ViewInject(R.id.btn_find_number)
    private Button number;
    @ViewInject(R.id.tb_find_sim)
    private ToggleButton sim;
    @ViewInject(R.id.tb_find_protect)
    private ToggleButton protect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
    }

    private void initData() {
        String getSim = PreferenceUtils.getString(this, "sim", null);
        sim.setChecked(!TextUtils.isEmpty(getSim));

        String getPhone = PreferenceUtils.getString(this, "bind_phone", null);
        if (!TextUtils.isEmpty(getPhone)) {
            number.setText(getPhone);
        }

        boolean getProtect = PreferenceUtils.getBoolean(this, "protect", false);
        protect.setChecked(getProtect);
    }

    @Event(value = {R.id.btn_find_number, R.id.btn_find_look, R.id.tb_find_sim, R.id.tb_find_protect})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_find_number:
                openActivityResult(ContactsActivity.class, 1);
                break;
            case R.id.btn_find_look:
                openActivity(SeekExplainActivity.class);
                break;
            case R.id.tb_find_sim:
                bindSimAction();
                break;
            case R.id.tb_find_protect:
                protectAction();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == Activity.RESULT_OK) {
                    String phone = data.getStringExtra("phone");
                    number.setText(phone);
                    PreferenceUtils.putString(this, "bind_phone", phone);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void bindSimAction() {
        if (sim.isChecked()) {
            PreferenceUtils.putString(this, "sim", TelephonyUtils.getSimNumber(this));
        } else {
            PreferenceUtils.remove(this, "sim");
        }
    }

    private void protectAction() {
        PreferenceUtils.putBoolean(this, "protect", protect.isChecked());
    }

}
