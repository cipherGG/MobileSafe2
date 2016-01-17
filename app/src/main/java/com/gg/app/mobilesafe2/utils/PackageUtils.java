package com.gg.app.mobilesafe2.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Debug;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;

/**
 * author cipherGG
 * Created by Administrator on 2015/12/23.
 * describe
 */
public class PackageUtils {

    public static PackageManager getPackageManager(Context context) {
        return context.getPackageManager();
    }

    public static List<PackageInfo> getPackageInfos(PackageManager packageManager) {
        return packageManager.getInstalledPackages(0);
    }

    public static List<PackageInfo> getPackageInfos(Context context) {
        PackageManager packageManager = getPackageManager(context);

        return packageManager.getInstalledPackages(0);
    }

    public static PackageInfo getPackageInfo(PackageManager packageManager, String packageName) {
        try {
            return packageManager.getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static PackageInfo getPackageInfo(Context context, String name) {
        try {
            return getPackageManager(context).getPackageInfo(name, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PackageInfo getPackageInfo(Context context) {
        try {
            return getPackageManager(context).getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Drawable getPackageIcon(PackageInfo packageInfo, Context context) {
        return packageInfo.applicationInfo.loadIcon(getPackageManager(context));
    }

    public static Drawable getPackageIcon(PackageInfo packageInfo, PackageManager packageManager) {
        return packageInfo.applicationInfo.loadIcon(packageManager);
    }

    public static String getApkName(PackageInfo packageInfo, Context context) {
        return packageInfo.applicationInfo.loadLabel(getPackageManager(context)).toString();
    }

    public static String getApkName(PackageInfo packageInfo, PackageManager packageManager) {
        return packageInfo.applicationInfo.loadLabel(packageManager).toString();
    }

    public static String getPackageName(PackageInfo packageInfo) {
        return packageInfo.packageName;
    }

    public static long getPackageSize(PackageInfo packageInfo) {
        // packageInfo.applicationInfo.sourceDir.length(); 行不行
        String sourceDir = packageInfo.applicationInfo.sourceDir;

        return new File(sourceDir).length();
    }

    public static int getApkRunMemory(ActivityManager activityManager, ActivityManager.RunningAppProcessInfo processInfo) {
        Debug.MemoryInfo[] memoryInfo = activityManager.getProcessMemoryInfo(new int[]{processInfo.pid});

        return memoryInfo[0].getTotalPrivateDirty() * 1024;
    }

    public static boolean isUser(PackageInfo packageInfo) {
        int flags = packageInfo.applicationInfo.flags;

        return (flags & ApplicationInfo.FLAG_SYSTEM) == 0;
    }

    public static boolean isRom(PackageInfo packageInfo) {
        int flags = packageInfo.applicationInfo.flags;

        return (flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == 0;
    }

}
