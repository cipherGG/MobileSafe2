package com.gg.app.mobilesafe2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.gg.app.mobilesafe2.R;
import com.gg.app.mobilesafe2.utils.MD5Utils;
import com.gg.app.mobilesafe2.db.dao.BombDao;
import com.gg.app.mobilesafe2.utils.PreferenceUtils;
import com.gg.app.mobilesafe2.utils.DialogUtils;
import com.gg.app.mobilesafe2.utils.YouMiUtils;

/**
 * by 2015/12/11
 */
public class HomeActivity extends BaseActivity implements View.OnClickListener {
    private EditText first;
    private EditText second;
    private AlertDialog dialog;
    private AlertDialog putDialog;
    private EditText putPassword;
    private String password;
    private long lastTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BombDao.initBomb(this);

        YouMiUtils.init(this);

        YouMiUtils.addAdView(this, (LinearLayout) findViewById(R.id.adLayout));

        LinearAnimation();

        initListener();
    }

    @Override
    protected void onStop() {
        YouMiUtils.flushAd(this);
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //换肤+开场动画
        finish();
        startActivity(new Intent(HomeActivity.this, HomeActivity.class));
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - lastTime < 1000) {
            super.onBackPressed();
        } else {
            showToast("再按一次退出程序");
            lastTime = System.currentTimeMillis();
        }
    }

    @Override
    protected void onDestroy() {
        YouMiUtils.flushAd(this);
        super.onDestroy();
    }

    private void LinearAnimation() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.linear_home);

        AlphaAnimation animation = new AlphaAnimation(0.3f, 1f);
        animation.setDuration(1000);
        layout.startAnimation(animation);
    }

    private void initListener() {
        //为什么这里cardView不能用xUtils注解绑定监听器？
        findViewById(R.id.home11).setOnClickListener(this);
        findViewById(R.id.home12).setOnClickListener(this);
        findViewById(R.id.home13).setOnClickListener(this);
        findViewById(R.id.home21).setOnClickListener(this);
        findViewById(R.id.home22).setOnClickListener(this);
        findViewById(R.id.home23).setOnClickListener(this);
        findViewById(R.id.home31).setOnClickListener(this);
        findViewById(R.id.home32).setOnClickListener(this);
        findViewById(R.id.home33).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home11:
                //手机防盗
                verifyPassword();
                break;
            case R.id.home12:
                //通讯卫士
                openActivity(CallSafeActivity.class);
                break;
            case R.id.home13:
                //流量统计?????????????????

                break;
            case R.id.home21:
                //软件管理
                openActivity(AppManagerActivity.class);
                break;
            case R.id.home22:
                //进程管理
                openActivity(TaskManagerActivity.class);
                break;
            case R.id.home23:
                //缓存清理
                openActivity(ClearCacheActivity.class);
                break;
            case R.id.home31:
                //数据备份??????????
                openActivity(BackUpActivity.class);
                break;
            case R.id.home32:
                //高级工具?????????
                openActivity(ToolsActivity.class);
                break;
            case R.id.home33:
                //设置中心?????????
                openActivity(SettingsActivity.class);
                break;
            //对话框点击事件
            case R.id.btn_dialog_surePassword:
                registerPassword();
                break;
            case R.id.btn_dialog_exitPassword:
                dialog.dismiss();
                break;
            case R.id.btn_dialog_sure_putPassword:
                login();
                break;
            case R.id.btn_dialog_exit_putPassword:
                putDialog.dismiss();
                break;
        }
    }

    private void verifyPassword() {
        password = PreferenceUtils.getString(HomeActivity.this, "find_password", "");
        if (TextUtils.isEmpty(password)) {
            setPassword();
        } else {
            putPassword();
        }
    }

    private void setPassword() {
        View view = LayoutInflater.from(HomeActivity.this).inflate(R.layout.dialog_set_password, null);
        first = (EditText) view.findViewById(R.id.et_dialog_firstPassword);
        second = (EditText) view.findViewById(R.id.et_dialog_secondPassword);
        Button sure = (Button) view.findViewById(R.id.btn_dialog_surePassword);
        Button exit = (Button) view.findViewById(R.id.btn_dialog_exitPassword);

        dialog = DialogUtils.showAlertDialog(HomeActivity.this, null, null, view, null, null, null);

        sure.setOnClickListener(this);
        exit.setOnClickListener(this);
    }

    private void putPassword() {
        View view = LayoutInflater.from(HomeActivity.this).inflate(R.layout.dialog_put_password, null);
        putPassword = (EditText) view.findViewById(R.id.et_dialog_first_putPassword);
        Button sure = (Button) view.findViewById(R.id.btn_dialog_sure_putPassword);
        Button exit = (Button) view.findViewById(R.id.btn_dialog_exit_putPassword);

        putDialog = DialogUtils.showAlertDialog(HomeActivity.this, null, null, view, null, null, null);

        sure.setOnClickListener(this);
        exit.setOnClickListener(this);
    }

    private void login() {
        String putPassword = this.putPassword.getText().toString();

        if (putPassword.length() < 5) {
            this.putPassword.setError("密码长度大于4，再好好想想");
            return;
        }

        if (!MD5Utils.MD5Encode(putPassword).equals(password)) {
            this.putPassword.setError("密码错误，再好好想想！");
            return;
        }

        openActivity(SeekActivity.class);
    }


    private void registerPassword() {
        first.setError(null);
        second.setError(null);

        String firstPassword = first.getText().toString();
        String secondPassword = second.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!(firstPassword.length() > 4)) {
            first.setError("密码大于4位，让小偷哭去吧！");
            focusView = first;
            cancel = true;
        }

        if (!(firstPassword.equals(secondPassword))) {
            second.setError("两次输入不一致，请核对一下下");
            focusView = second;
            cancel = true;
        }

        if (cancel) {
            //获取焦点，增强用户体验
            focusView.requestFocus();

        } else {
            PreferenceUtils.putString(this, "find_password", MD5Utils.MD5Encode(firstPassword));
            dialog.dismiss();
            this.showToast("设置成功，请尝试登录");
        }
    }

}
