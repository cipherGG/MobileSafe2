package com.gg.app.mobilesafe2.utils;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import net.youmi.android.AdManager;
import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;
import net.youmi.android.spot.SpotManager;

/**
 * author cipherGG
 * Created by Administrator on 2015/12/24.
 * describe
 */
public class YouMiUtils {

    public static void init(Context context) {
        AdManager.getInstance(context).init("6ef149673badb9cf", "85531ab836c1f7bd", false);
    }

    public static void addAdView(final Context context, LinearLayout layout) {
        AdView adView = new AdView(context, AdSize.FIT_SCREEN);

        layout.addView(adView);
    }

    public static void addAdView(final Context context) {
        AdView adView = new AdView(context, AdSize.FIT_SCREEN);

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams
                (FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.gravity = Gravity.BOTTOM | Gravity.RIGHT;

        ((Activity) context).addContentView(adView, layoutParams);
    }

    // 如果不调用此方法，则按home键的时候会出现图标无法显示的情况。
    public static void flushAd(Context context) {
        SpotManager.getInstance(context).onStop();
    }

    public static void showSplash(Context context, Class<?> openActivity) {
        SpotManager.getInstance(context).showSplashSpotAds(context, openActivity);
    }

}
