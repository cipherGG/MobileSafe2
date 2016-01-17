package com.gg.app.mobilesafe2.activity;

import android.app.ActivityManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gg.app.mobilesafe2.R;
import com.gg.app.mobilesafe2.bean.TaskInfo;
import com.gg.app.mobilesafe2.engine.TaskInfos;
import com.gg.app.mobilesafe2.utils.ProcessUtils;

import java.util.ArrayList;
import java.util.List;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.view.annotation.Event;

@ContentView(R.layout.activity_task_manager)
public class TaskManagerActivity extends BaseActivity {
    @ViewInject(R.id.rv_task_manager)
    private RecyclerView recyclerView;
    @ViewInject(R.id.tv_task_manager_run)
    private TextView runName;
    @ViewInject(R.id.tv_task_manager_memory)
    private TextView appMemory;
    @ViewInject(R.id.linear_task_manager_loading)
    private LinearLayout layout;
    private Adapter adapter;
    private List<TaskInfo> userProcessInfos;
    private List<TaskInfo> systemProcessInfos;
    private List<TaskInfo> taskInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout.setVisibility(View.VISIBLE);

        initUI();

        initData();
    }

    private void initUI() {
        int runningCount = ProcessUtils.getRunningCount(this);
        runName.setText(String.valueOf(runningCount));

        String availMem = ProcessUtils.getAvailMem(this);
        String totalMem = ProcessUtils.getTotalMem(this);
        appMemory.setText(availMem + "/" + totalMem);

    }

    private void initData() {
        new Thread() {
            @Override
            public void run() {
                taskInfos = TaskInfos.getTaskInfos(TaskManagerActivity.this);
                userProcessInfos = TaskInfos.getUserProcessInfos(taskInfos);
                systemProcessInfos = TaskInfos.getSystemProcessInfos(taskInfos);

                handler.sendEmptyMessage(1);
            }
        }.start();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            layout.setVisibility(View.INVISIBLE);

            adapter = new Adapter();
            recyclerView.setLayoutManager(new LinearLayoutManager(TaskManagerActivity.this));
            recyclerView.setAdapter(adapter);
        }
    };

    @Event(value = {R.id.btn_task_clear, R.id.btn_task_user_check, R.id.btn_task_system_check, R.id.btn_task_relative_check},
            type = View.OnClickListener.class)
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_task_clear:
                killProcess();
                break;

            case R.id.btn_task_user_check:
                selectUser();
                break;

            case R.id.btn_task_system_check:
                selectSystem();
                break;

            case R.id.btn_task_relative_check:
                relativeCheck();
                break;
        }
    }

    private void selectUser() {
        for (TaskInfo info : userProcessInfos) {
            info.setIsCheck(true);
        }
        adapter.notifyDataSetChanged();
    }

    private void selectSystem() {
        for (TaskInfo info : systemProcessInfos) {
            info.setIsCheck(true);
        }
        adapter.notifyDataSetChanged();
    }

    private void relativeCheck() {
        for (TaskInfo info : taskInfos) {
            info.setIsCheck(!info.isCheck());
        }
        adapter.notifyDataSetChanged();
    }

    private void killProcess() {
        ActivityManager activityManager = ProcessUtils.getActivityManager(this);

        int total = 0;

        List<TaskInfo> clearTasks = new ArrayList<>();

        for (TaskInfo info : userProcessInfos) {
            if (info.isCheck()) {
                //本身程序不可删
                if (info.getPackageName().equals(getPackageName())) {
                    continue;
                }
                activityManager.killBackgroundProcesses(info.getPackageName());
                // userProcessInfos.remove(info);这样会报错
                clearTasks.add(info);
                total++;
            }
        }

        userProcessInfos.removeAll(clearTasks);
        clearTasks.clear();

        for (TaskInfo info : systemProcessInfos) {
            if (info.isCheck()) {
                activityManager.killBackgroundProcesses(info.getPackageName());
                // systemProcessInfos.remove(info);这样会报错
                clearTasks.add(info);
                total++;
            }
        }

        systemProcessInfos.removeAll(clearTasks);
        clearTasks.clear();

        adapter.notifyDataSetChanged();
        showSnaker(recyclerView, "共清理" + total + "个软件", null, null);
    }

    private class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public int getItemCount() {
            return userProcessInfos.size() + systemProcessInfos.size() + 2;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return 1;

            } else if (position == userProcessInfos.size() + 1) {
                return 2;

            } else {
                return 3;
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == 1) {
                TextView textView = new TextView(TaskManagerActivity.this);
                return new TagViewHolder(textView, "用户程序 (" + userProcessInfos.size() + ")");

            } else if (viewType == 2) {
                TextView textView = new TextView(TaskManagerActivity.this);
                return new TagViewHolder(textView, "系统程序 (" + systemProcessInfos.size() + ")");

            } else {
                View view = LayoutInflater.from(TaskManagerActivity.this).inflate(R.layout.item_task_manager, parent, false);
                return new ItemViewHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof ItemViewHolder) {
                TaskInfo taskInfo;

                if (position < userProcessInfos.size() + 1) {
                    taskInfo = userProcessInfos.get(position - 1);
                } else {
                    taskInfo = systemProcessInfos.get(position - userProcessInfos.size() - 2);
                }

                ((ItemViewHolder) holder).info = taskInfo;
                ((ItemViewHolder) holder).icon.setBackground(taskInfo.getIcon());
                ((ItemViewHolder) holder).apkName.setText(taskInfo.getName());
                ((ItemViewHolder) holder).memory.setText(Formatter.formatFileSize(TaskManagerActivity.this, taskInfo.getMemory()));
                //本身程序不提供CheckBox
                if (taskInfo.getPackageName().equals(TaskManagerActivity.this.getPackageName())) {
                    ((ItemViewHolder) holder).kill.setVisibility(View.INVISIBLE);
                } else {
                    ((ItemViewHolder) holder).kill.setChecked(taskInfo.isCheck());
                }
            }
        }

        private class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TaskInfo info;
            ImageView icon;
            TextView apkName;
            TextView memory;
            CheckBox kill;

            public ItemViewHolder(View itemView) {
                super(itemView);
                icon = (ImageView) itemView.findViewById(R.id.iv_item_task_apk_icon);
                apkName = (TextView) itemView.findViewById(R.id.tv_item_task_apk_name);
                memory = (TextView) itemView.findViewById(R.id.tv_item_task_apk_memory);
                kill = (CheckBox) itemView.findViewById(R.id.cb_item_task_apk_kill);

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                kill.setChecked(!kill.isChecked());
                info.setIsCheck(kill.isChecked());
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

}
