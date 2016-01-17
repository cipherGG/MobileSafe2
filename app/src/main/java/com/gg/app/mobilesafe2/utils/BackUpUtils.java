package com.gg.app.mobilesafe2.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.widget.Toast;

import com.gg.app.mobilesafe2.bean.SMS;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BackUpUtils {

    public static boolean backUpSmsSdCard(Context context) {

        if (!EnvironmentUtils.isExternalExit()) {
            Toast.makeText(context, "请插入SDCard", Toast.LENGTH_SHORT).show();
            return false;
        }

        ContentResolver resolver = context.getContentResolver();
        Uri uri = Uri.parse("content://sms/");
        Cursor cursor = resolver.query(uri, new String[]{"address", "date", "type", "body"}, null, null, null);

        if (cursor == null) {
            Toast.makeText(context, "操作异常", Toast.LENGTH_SHORT).show();
            return false;
        }

        List<SMS> list = new ArrayList<>();

        while (cursor.moveToNext()) {
            SMS sms = new SMS(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
            list.add(sms);
        }

        Gson gson = new Gson();
        String json = gson.toJson(list);
        byte[] buffer = json.getBytes();

        File file = new File(EnvironmentUtils.getExternalDirectory(), "SMS.json");
        FileOutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(file);
            outputStream.write(buffer, 0, buffer.length);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        cursor.close();
        return true;
    }

    public static boolean restoreSmsSdCard(Context context) {
        if (!EnvironmentUtils.isExternalExit()) {
            Toast.makeText(context, "请插入SDCard", Toast.LENGTH_SHORT).show();
            return false;
        }

        File file = new File(EnvironmentUtils.getExternalDirectory(), "SMS.json");
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);

            byte[] buffer = new byte[1024];
            while (inputStream.read() != -1) {

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

}
