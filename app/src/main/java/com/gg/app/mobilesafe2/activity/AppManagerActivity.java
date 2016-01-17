package com.gg.app.mobilesafe2.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gg.app.mobilesafe2.R;
import com.gg.app.mobilesafe2.bean.AppInfo;
import com.gg.app.mobilesafe2.engine.AppInfos;
import com.gg.app.mobilesafe2.utils.AnimationUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

@ContentView(R.layout.activity_app_manager)
public class AppManagerActivity extends BaseActivity implements View.OnClickListener {
    @ViewInject(R.id.lv_app_manager)
    private RecyclerView recyclerView;
    @ViewInject(R.id.tv_app_manager_rom)
    private TextView rom;
    @ViewInject(R.id.tv_app_manager_sdcard)
    private TextView sdcard;
    @ViewInject(R.id.linear_app_manager_loading)
    private LinearLayout layout;
    private List<AppInfo> userAppInfos;
    private List<AppInfo> systemAppInfos;
    private PopupWindow popupWindow;
    private String clickPackageName;
    private String clickAppName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout.setVisibility(View.VISIBLE);

        //注册软件卸载广播
        IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_REMOVED);
        filter.addDataScheme("package");
        registerReceiver(uninstallReceiver, filter);

        initUI();

        initData();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(uninstallReceiver);
        super.onDestroy();
    }

    private BroadcastReceiver uninstallReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            initData();
        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            layout.setVisibility(View.INVISIBLE);

            RecycleAdapter adapter = new RecycleAdapter();
            recyclerView.setLayoutManager(new LinearLayoutManager(AppManagerActivity.this));
            recyclerView.setAdapter(adapter);
        }
    };

    private void initUI() {
        //rom剩余空间
        long rom_freeSpace = Environment.getDataDirectory().getFreeSpace();
        //sdCard剩余空间
        long sd_freeSpace = Environment.getExternalStorageDirectory().getFreeSpace();

        //看清导入的包android.text.format.Formatter;
        rom.setText(Formatter.formatFileSize(this, rom_freeSpace));
        sdcard.setText(Formatter.formatFileSize(this, sd_freeSpace));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                popWindowDismiss();
            }
        });
    }

    private void initData() {
        new Thread() {
            @Override
            public void run() {
                List<AppInfo> appInfos = AppInfos.getAppInfos(AppManagerActivity.this);

                userAppInfos = AppInfos.getUserAppInfos(AppManagerActivity.this, appInfos);

                systemAppInfos = AppInfos.getSystemAppInfos(AppManagerActivity.this, appInfos);

                handler.sendEmptyMessage(1);
            }
        }.start();
    }

    private class RecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public int getItemCount() {
            return userAppInfos.size() + systemAppInfos.size() + 2;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return 1;
            } else if (position == userAppInfos.size() + 1) {
                return 2;
            } else {
                return 3;
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == 1) {
                TextView textView = new TextView(AppManagerActivity.this);
                return new TagViewHolder(textView, "用户程序 (" + userAppInfos.size() + ")");

            } else if (viewType == 2) {
                TextView textView = new TextView(AppManagerActivity.this);
                return new TagViewHolder(textView, "系统程序 (" + systemAppInfos.size() + ")");

            } else {
                View view = LayoutInflater.from(AppManagerActivity.this).inflate(R.layout.item_app_manager, parent, false);
                return new ItemViewHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

            if (holder instanceof ItemViewHolder) {
                //当position小于用户程序数时，数据源是用户程序集合，当position大于用户程序数时，数据源是系统应用集合
                AppInfo appInfo;
                if (position < userAppInfos.size() + 1) {
                    appInfo = userAppInfos.get(position - 1);
                } else {
                    appInfo = systemAppInfos.get(position - userAppInfos.size() - 2);
                }

                ((ItemViewHolder) holder).icon.setBackground(appInfo.getIcon());
                ((ItemViewHolder) holder).apkName.setText(appInfo.getApkName());
                ((ItemViewHolder) holder).size.setText(Formatter.formatFileSize(AppManagerActivity.this, appInfo.getApkSize()));
                if (appInfo.isUser()) {
                    ((ItemViewHolder) holder).user.setText("用户程序");
                } else {
                    ((ItemViewHolder) holder).user.setText("系统应用");
                }
                //是这样用吗
                ((ItemViewHolder) holder).packageName = appInfo.getApkPackageName();
            }
        }

        private class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            ImageView icon;
            TextView apkName;
            TextView user;
            TextView size;
            Button delete;
            String packageName;

            //把实例化封装到构造函数里
            public ItemViewHolder(View itemView) {
                super(itemView);
                icon = (ImageView) itemView.findViewById(R.id.iv_item_app_apk_icon);
                apkName = (TextView) itemView.findViewById(R.id.tv_item_app_apk_name);
                user = (TextView) itemView.findViewById(R.id.tv_item_app_apk_user);
                size = (TextView) itemView.findViewById(R.id.tv_item_app_apk_size);
                delete = (Button) itemView.findViewById(R.id.btn_item_app_apk_delete);

                itemView.setOnClickListener(this);
                delete.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btn_item_app_apk_delete) {
                    Intent details_intent = new Intent();
                    details_intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                    details_intent.addCategory(Intent.CATEGORY_DEFAULT);
                    details_intent.setData(Uri.parse("package:" + packageName));

                    AppManagerActivity.this.startActivity(details_intent);
                } else {
                    //以下是弹出pop选项框
                    View contentView = LayoutInflater.from(AppManagerActivity.this).inflate(R.layout.item_app_manager_popup, null);

                    popWindowDismiss();
                    //-2 表示包裹内容
                    popupWindow = new PopupWindow(contentView, -2, -2);
                    //添加动画必须得有背景，这里是透明背景
                    popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    int[] location = new int[2];
                    //获取view展示到窗体上面的位置,int[0]是宽,int[1]是高
                    v.getLocationInWindow(location);

                    popupWindow.showAtLocation(recyclerView, Gravity.LEFT + Gravity.TOP, 200, location[1]);

                    ScaleAnimation scale = AnimationUtils.getScale();
                    AlphaAnimation alpha = AnimationUtils.getAlpha();
                    AnimationSet set = AnimationUtils.getSet(scale, alpha);
                    contentView.startAnimation(set);

                    clickPackageName = packageName;
                    clickAppName = apkName.getText().toString();

                    LinearLayout uninstall = (LinearLayout) contentView.findViewById(R.id.linear_app_pop_uninstall);
                    LinearLayout run = (LinearLayout) contentView.findViewById(R.id.linear_app_pop_run);
                    LinearLayout share = (LinearLayout) contentView.findViewById(R.id.linear_app_pop_share);

                    uninstall.setOnClickListener(AppManagerActivity.this);
                    run.setOnClickListener(AppManagerActivity.this);
                    share.setOnClickListener(AppManagerActivity.this);
                }
            }
        }

        private class TagViewHolder extends RecyclerView.ViewHolder {
            TextView textView;

            public TagViewHolder(View itemView, String text) {
                super(itemView);
                textView = (TextView) itemView;
                textView.setTextColor(Color.WHITE);
                textView.setBackgroundColor(Color.GRAY);
                textView.setText(text);

                //params相当于命名空间
                WindowManager.LayoutParams params = new WindowManager.LayoutParams();
                params.width = WindowManager.LayoutParams.MATCH_PARENT;
                params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                params.verticalMargin = 10;
                textView.setLayoutParams(params);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linear_app_pop_uninstall:
                //不知怎么地报错
//                Intent uninstall_intent = new Intent();
//                uninstall_intent.setAction("android.intent.action.VIEW");
//                uninstall_intent.addCategory("android.intent.category.DEFAULT");
//                uninstall_intent.setData(Uri.parse("package:" + clickPackageName));
//                this.startActivity(uninstall_intent);
                this.popWindowDismiss();
                break;

            case R.id.linear_app_pop_run:
                Intent run_intent = this.getPackageManager().getLaunchIntentForPackage(clickPackageName);
                this.startActivity(run_intent);
                this.popWindowDismiss();
                break;

            case R.id.linear_app_pop_share:
                Intent share_intent = new Intent("android.intent.action.SEND");
                share_intent.setType("text/plain");
                share_intent.putExtra("android.intent.extra.SUBJECT", "f分享");
                share_intent.putExtra("android.intent.extra.TEXT", "Hi!推荐您使用一款软件：" + clickAppName
                        + "\n下载地址:" + "https://play.google.com/store/apps/details?id=" + clickPackageName);
                this.startActivity(Intent.createChooser(share_intent, "分享"));
                this.popWindowDismiss();
                break;
        }
    }

    private void popWindowDismiss() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }

}
