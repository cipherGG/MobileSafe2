package com.gg.app.mobilesafe2.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.os.Handler;

import com.gg.app.mobilesafe2.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_rocket_bg)
public class RocketBgActivity extends Activity {
    @ViewInject(R.id.iv_background_bottom)
    private ImageView bottom;
    @ViewInject(R.id.iv_background_top)
    private ImageView top;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AlphaAnimation animation = new AlphaAnimation(0, 1);
        animation.setDuration(500);
        animation.setFillAfter(true);

        bottom.setAnimation(animation);
        top.setAnimation(animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 500);
    }
}
