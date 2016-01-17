package com.gg.app.mobilesafe2.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * author cipherGG
 * Created by Administrator on 2015/12/23.
 * describe
 */
public class EnvironmentUtils {

    public static boolean isExternalExit() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static File getExternalDirectory() {
        return Environment.getExternalStorageDirectory();
    }

    public static File getFilesDir(Context context) {
        return context.getFilesDir();
    }

    public static File getCacheDir(Context context) {
        return context.getCacheDir();
    }

    public static FileOutputStream openFileOut(Context context, String file, int mode) {
        FileOutputStream outputStream = null;
        try {
            // Context.MODE_PRIVATE
            outputStream = context.openFileOutput(file, mode);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return outputStream;
    }
}
