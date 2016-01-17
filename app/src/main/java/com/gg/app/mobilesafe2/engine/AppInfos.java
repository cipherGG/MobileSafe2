package com.gg.app.mobilesafe2.engine;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.gg.app.mobilesafe2.bean.AppInfo;
import com.gg.app.mobilesafe2.utils.PackageUtils;

import java.util.ArrayList;
import java.util.List;

public class AppInfos {

    public static List<AppInfo> getAppInfos(Context context) {
        List<AppInfo> list = new ArrayList<>();

        PackageManager packageManager = PackageUtils.getPackageManager(context);

        List<PackageInfo> installedPackages = PackageUtils.getPackageInfos(packageManager);

        for (PackageInfo installedPackage : installedPackages) {
            AppInfo appInfo = new AppInfo();

            appInfo.setIcon(PackageUtils.getPackageIcon(installedPackage, packageManager));

            appInfo.setApkName(PackageUtils.getApkName(installedPackage, packageManager));

            appInfo.setApkPackageName(PackageUtils.getPackageName(installedPackage));

            appInfo.setApkSize(PackageUtils.getPackageSize(installedPackage));

            appInfo.setIsUser(PackageUtils.isUser(installedPackage));

            appInfo.setIsRom(PackageUtils.isRom(installedPackage));

            list.add(appInfo);
        }
        return list;
    }

    public static List<AppInfo> getUserAppInfos(Context context) {
        List<AppInfo> infos = getAppInfos(context);

        List<AppInfo> userInfos = new ArrayList<>();

        for (AppInfo info : infos) {
            if (info.isUser()) {
                userInfos.add(info);
            }
        }

        return userInfos;
    }

    public static List<AppInfo> getUserAppInfos(Context context, List<AppInfo> infos) {
        List<AppInfo> userInfos = new ArrayList<>();

        for (AppInfo info : infos) {
            if (info.isUser()) {
                userInfos.add(info);
            }
        }

        return userInfos;
    }

    public static List<AppInfo> getSystemAppInfos(Context context) {
        List<AppInfo> infos = getAppInfos(context);

        List<AppInfo> systemInfos = new ArrayList<>();

        for (AppInfo info : infos) {
            if (!info.isUser()) {
                systemInfos.add(info);
            }
        }

        return systemInfos;
    }

    public static List<AppInfo> getSystemAppInfos(Context context, List<AppInfo> infos) {
        List<AppInfo> systemInfos = new ArrayList<>();

        for (AppInfo info : infos) {
            if (!info.isUser()) {
                systemInfos.add(info);
            }
        }

        return systemInfos;
    }

}
