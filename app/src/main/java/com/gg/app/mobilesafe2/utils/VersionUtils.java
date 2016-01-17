package com.gg.app.mobilesafe2.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Environment;

import com.gg.app.mobilesafe2.activity.BaseActivity;
import com.gg.app.mobilesafe2.db.dao.BombDao;
import com.gg.app.mobilesafe2.utils.DialogUtils;
import com.gg.app.mobilesafe2.utils.PackageUtils;

import java.io.File;

public class VersionUtils {

    public static String getVersionName(Context context) {
        PackageInfo packageInfo = PackageUtils.getPackageInfo(context);

        return packageInfo == null ? "" : packageInfo.versionName;
    }

    public static void checkVersionCode(final BaseActivity baseActivity) {
        PackageInfo packageInfo = PackageUtils.getPackageInfo(baseActivity);

        if (packageInfo == null) {
            return;
        }

        BombDao.queryVersionCode(baseActivity, packageInfo.versionCode + "", new BombDao.Callback3() {
            @Override
            public void toUpdate(String code, String name) {
                DialogUtils.showAlertDialog(baseActivity, "发现新版本 " + name, "此处应该有介绍", null, "立即更新", "暂不更新", new DialogUtils.CallBack2() {
                    @Override
                    public void positive() {
                        downloadPackage(baseActivity);
                    }
                });
            }

            @Override
            public void noUpdate() {
                baseActivity.showToast("已是最新版本");
            }
        });

    }


    public static void downloadPackage(Context context) {
        String target = Environment.getExternalStorageDirectory() + "/update.apk";
        //File apk = new File("此处应放package路径");
        //installAPK(context, apk);
    }

    public static void installAPK(Context context, File apk) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setDataAndType(Uri.fromFile(apk), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

}
