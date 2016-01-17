package com.gg.app.mobilesafe2.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.gg.app.mobilesafe2.R;
import com.gg.app.mobilesafe2.activity.RocketBgActivity;

public class RocketService extends Service {

    private WindowManager.LayoutParams params;
    private int winWidth;
    private int winHeight;
    private WindowManager windowManager;
    private View view;
    private int startX;
    private int startY;


    public RocketService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        showRocket();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeRocket();
    }

    public void showRocket() {
        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        //注意看好，获取屏幕的宽高，不要用过时方法
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        winWidth = displayMetrics.widthPixels;
        winHeight = displayMetrics.heightPixels;

        params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.format = PixelFormat.TRANSLUCENT;
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        params.gravity = Gravity.LEFT + Gravity.TOP;
        params.setTitle("title");

        view = View.inflate(this, R.layout.rocket, null);
        ImageView rocket = (ImageView) view.findViewById(R.id.iv_rocket1);
        rocket.setBackgroundResource(R.drawable.anim_rocket);
        AnimationDrawable animation = (AnimationDrawable) rocket.getBackground();
        animation.start();

        windowManager.addView(view, params);

        moveRocket();
    }

    private void moveRocket() {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //获取起点坐标
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        int endX = (int) event.getRawX();
                        int endY = (int) event.getRawY();
                        //获取偏移坐标
                        int dX = endX - startX;
                        int dY = endY - startY;
                        //更新坐标
                        params.x += dX;
                        params.y += dY;
                        //放置坐标偏离屏幕
                        if (params.x < 0) {
                            params.x = 0;
                        }
                        if (params.y < 0) {
                            params.y = 0;
                        }
                        if (params.x > winWidth - view.getWidth()) {
                            params.x = winWidth - view.getWidth();
                        }
                        if (params.y > winHeight - view.getHeight()) {
                            params.y = winHeight - view.getHeight();
                        }
                        //更新view
                        windowManager.updateViewLayout(view, params);
                        //重新初始化坐标
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;

                    case MotionEvent.ACTION_UP:
                        if ((params.y >= winHeight - view.getHeight() * 2)) {
                            sendRocket();
                            //启动一个栈来启动backgroundActivity
                            Intent intent = new Intent(RocketService.this, RocketBgActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            //启动烟雾效果
                            RocketService.this.startActivity(intent);
                        }
                        break;
                }
                return true;
            }
        });
    }

    private void sendRocket() {
        new Thread() {
            @Override
            public void run() {
                int totalHeight = winHeight - view.getHeight();

                for (int i = 0; i < 20; i++) {
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Message message = new Message();
                    message.arg1 = totalHeight - totalHeight / 19 * i;
                    mHandler.sendMessage(message);
                }

            }
        }.start();
    }

    public void removeRocket() {
        if (windowManager != null && view != null) {
            windowManager.removeView(view);
            view = null;
        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            params.y = msg.arg1;
            windowManager.updateViewLayout(view, params);

            super.handleMessage(msg);
        }
    };

}
