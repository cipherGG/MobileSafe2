package com.gg.app.mobilesafe2.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.text.format.Formatter;

import java.util.List;

/**
 * author cipherGG
 * Created by Administrator on 2015/12/23.
 * describe 进程管理类
 */
public class ProcessUtils {

    public static ActivityManager getActivityManager(Context context) {
        return (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    }

    //获取运行的进程
    public static List<ActivityManager.RunningAppProcessInfo> getRunningProcesses(ActivityManager activityManager){
        return activityManager.getRunningAppProcesses();
    }

    public static List<ActivityManager.RunningAppProcessInfo> getRunningProcesses(Context context) {
        ActivityManager activityManager = getActivityManager(context);

        return activityManager.getRunningAppProcesses();
    }

    //获取运行的进程数
    public static int getRunningCount(Context context) {
        return getRunningProcesses(context).size();
    }

    public static ActivityManager.MemoryInfo getMemoryInfo(Context context) {
        ActivityManager activitymanager = getActivityManager(context);

        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();

        activitymanager.getMemoryInfo(memoryInfo);

        return memoryInfo;
    }

    //获取可用运存
    public static String getAvailMem(Context context) {
        ActivityManager.MemoryInfo memoryInfo = getMemoryInfo(context);

        return Formatter.formatFileSize(context, memoryInfo.availMem);
    }

    //获取总共运存
    public static String getTotalMem(Context context) {
        ActivityManager.MemoryInfo memoryInfo = getMemoryInfo(context);

        return Formatter.formatFileSize(context, memoryInfo.totalMem);
    }

}
