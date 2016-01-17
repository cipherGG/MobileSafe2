package com.gg.app.mobilesafe2.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gg.app.mobilesafe2.R;
import com.gg.app.mobilesafe2.db.dao.AddressDao;
import com.gg.app.mobilesafe2.utils.DialogUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

@ContentView(R.layout.activity_tools)
public class ToolsActivity extends BaseActivity {

    private AlertDialog vibrateDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //拷贝数据库
        AddressDao.copyDB(this);
    }

    @Event(value = {R.id.tv_tools_phone, R.id.tv_tools_vibrate})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_tools_phone:
                phoneAction();
                break;

            case R.id.tv_tools_vibrate:
                vibrateAction();
                break;

        }
    }

    private void phoneAction() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_checkphone, null);
        final EditText et = (EditText) view.findViewById(R.id.et_dialog_checkPhone);
        Button btn = (Button) view.findViewById(R.id.btn_dialog_checkPhone);
        final TextView tv = (TextView) view.findViewById(R.id.tv_dialog_checkPhone);

        DialogUtils.showAlertDialog(this, "电话归属地查询", null, view, null, "返回", null);

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String address = AddressDao.getAddress(s.toString());
                tv.setText(address);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inNumber = et.getText().toString().trim();

                if (!TextUtils.isEmpty(inNumber)) {
                    String address = AddressDao.getAddress(inNumber);
                    tv.setText(address);
                } else {
                    et.setError("号码不能为空");
                }
            }
        });
    }

    private void vibrateAction() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_vibrate, null);
        Button btn1 = (Button) view.findViewById(R.id.btn_dialog_vibrate1);
        Button btn2 = (Button) view.findViewById(R.id.btn_dialog_vibrate2);
        Button btn3 = (Button) view.findViewById(R.id.btn_dialog_vibrate3);
        Button btn4 = (Button) view.findViewById(R.id.btn_dialog_vibrate4);
        Button cancel = (Button) view.findViewById(R.id.btn_dialog_vibrateCancel);
        btn1.setOnClickListener(vibrateListener);
        btn2.setOnClickListener(vibrateListener);
        btn3.setOnClickListener(vibrateListener);
        btn4.setOnClickListener(vibrateListener);
        cancel.setOnClickListener(vibrateListener);
        vibrateDialog = DialogUtils.showAlertDialog(this, "真的是用来按摩的", null, view, null, null, null);
    }

    View.OnClickListener vibrateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.btn_dialog_vibrate1:
                    vibrate(ToolsActivity.this, 2000, 1000);
                    break;
                case R.id.btn_dialog_vibrate2:
                    vibrate(ToolsActivity.this, 1200, 1000);
                    break;
                case R.id.btn_dialog_vibrate3:
                    vibrate(ToolsActivity.this, 600, 1000);
                    break;
                case R.id.btn_dialog_vibrate4:
                    vibrate(ToolsActivity.this, 100, 1000);
                    break;
                case R.id.btn_dialog_vibrateCancel:
                    vibrate(ToolsActivity.this, 0, 0);
                    vibrateDialog.dismiss();
                    break;
            }
        }
    };

    private void vibrate(Context context, int retire, int run) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        if (run == 0) {
            vibrator.cancel();
        } else {
            vibrator.vibrate(new long[]{retire, run}, 0);
        }
    }

}
