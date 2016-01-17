package com.gg.app.mobilesafe2.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.gg.app.mobilesafe2.R;
import com.gg.app.mobilesafe2.utils.PreferenceUtils;

import org.xutils.x;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        onPreCreate();

        super.onCreate(savedInstanceState);

        x.view().inject(this);
    }

    public void onPreCreate() {
        final String currentTheme = PreferenceUtils.getString(this, "setting_theme", "Blue");

        if (currentTheme.equals("Blue")) {
            this.setTheme(R.style.BlueTheme);

        } else if (currentTheme.equals("Green")) {
            this.setTheme(R.style.GreenTheme);

        } else if (currentTheme.equals("Red")) {
            this.setTheme(R.style.RedTheme);

        } else if (currentTheme.equals("Indigo")) {
            this.setTheme(R.style.IndigoTheme);

        } else if (currentTheme.equals("BlueGrey")) {
            this.setTheme(R.style.BlueGreyTheme);

        } else if (currentTheme.equals("Black")) {
            this.setTheme(R.style.BlackTheme);

        } else if (currentTheme.equals("Orange")) {
            this.setTheme(R.style.OrangeTheme);

        } else if (currentTheme.equals("Purple")) {
            this.setTheme(R.style.PurpleTheme);

        } else if (currentTheme.equals("Pink")) {
            this.setTheme(R.style.PinkTheme);

        }
    }

    public Context getContext() {
        return this;
    }

    public BaseActivity getBaseActivity() {
        return this;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                onBackPressed();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showToast(String show) {
        Toast.makeText(BaseActivity.this, show, Toast.LENGTH_SHORT).show();
    }

    public void showSnaker(View view, String show, String action, View.OnClickListener listener) {
        Snackbar.make(view, show, Snackbar.LENGTH_SHORT).setAction(action, listener).show();
    }

    public void openActivity(Class mClass) {
        Intent intent = new Intent();
        intent.setClass(this, mClass);
        startActivity(intent);
        openAnim();
    }

    public void openActivityResult(Class mClass, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, mClass);
        startActivityForResult(intent, requestCode);
        openAnim();
    }

    public void openService(Class mClass) {
        Intent intent = new Intent();
        intent.setClass(this, mClass);
        startService(intent);
    }

    public void stopService(Class mClass) {
        Intent intent = new Intent();
        intent.setClass(this, mClass);
        stopService(intent);
    }

    private void openAnim() {
        overridePendingTransition(R.anim.trans_in, R.anim.trans_out);
    }

}
