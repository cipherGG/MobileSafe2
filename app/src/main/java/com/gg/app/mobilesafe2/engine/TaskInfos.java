package com.gg.app.mobilesafe2.engine;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import com.gg.app.mobilesafe2.R;
import com.gg.app.mobilesafe2.bean.TaskInfo;
import com.gg.app.mobilesafe2.utils.PackageUtils;
import com.gg.app.mobilesafe2.utils.ProcessUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * author cipherGG
 * Created by Administrator on 2015/12/23.
 * describe 进程列表信息获取
 */
public class TaskInfos {

    public static List<TaskInfo> getTaskInfos(Context context) {
        List<TaskInfo> list = new ArrayList<>();

        ActivityManager activityManager = ProcessUtils.getActivityManager(context);

        PackageManager packageManager = PackageUtils.getPackageManager(context);

        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = ProcessUtils.getRunningProcesses(activityManager);

        for (ActivityManager.RunningAppProcessInfo processInfo : runningAppProcesses) {
            TaskInfo taskInfo = new TaskInfo();

            String packageName = processInfo.processName;

            taskInfo.setPackageName(packageName);

            PackageInfo packageInfo = PackageUtils.getPackageInfo(packageManager, packageName);

            // 有些进程使用c写的，没信息,需要自己设置
            if (packageInfo == null) {
                taskInfo.setIcon(ContextCompat.getDrawable(context, R.mipmap.ic_launcher));

                taskInfo.setName(packageName);

                //taskInfo.setMemory(PackageUtils.getApkRunMemory(activityManager));
                taskInfo.setMemory(0);

                taskInfo.setIsUser(false);

                list.add(taskInfo);

                continue;
            }

            taskInfo.setIcon(PackageUtils.getPackageIcon(packageInfo, packageManager));

            taskInfo.setName(PackageUtils.getApkName(packageInfo, packageManager));

            taskInfo.setMemory(PackageUtils.getApkRunMemory(activityManager, processInfo));

            taskInfo.setIsUser(PackageUtils.isUser(packageInfo));

            list.add(taskInfo);
        }

        return list;
    }

    public static List<TaskInfo> getUserProcessInfos(List<TaskInfo> infos) {
        List<TaskInfo> list = new ArrayList<>();

        for (TaskInfo info : infos) {
            if (info.isUser()) {
                list.add(info);
            }
        }

        return list;
    }

    public static List<TaskInfo> getSystemProcessInfos(List<TaskInfo> infos) {
        List<TaskInfo> list = new ArrayList<>();

        for (TaskInfo info : infos) {
            if (!info.isUser()) {
                list.add(info);
            }
        }

        return list;
    }


}
