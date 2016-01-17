package com.gg.app.mobilesafe2.db.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AddressDao {
    private static final String PATH = "data/data/com.gg.app.mobilesafe2/files/address.db";

    public static void copyDB(Context context) {
        String name = "address.db";
        File file = new File(context.getFilesDir(), name);

        if (file.exists()) {
            return;
        }

        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            inputStream = context.getAssets().open(name);
            outputStream = new FileOutputStream(file);

            int len = -1;
            byte[] buffer = new byte[1024];
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getAddress(String number) {
        String address = "未知号码";
        //获取数据库对象
        SQLiteDatabase database = SQLiteDatabase.openDatabase(PATH, null, SQLiteDatabase.OPEN_READONLY);
        // 手机号码：1+（3，4，5，6，7，8）+（9位数字）,正则表达式：^1[3-8]\d{9}$
        if (number.matches("^1[3-8]\\d{9}$")) {

            Cursor cursor = database.rawQuery(
                    "select location from data2 where id=(select outkey from data1 where id=?)",
                    new String[]{number.substring(0, 7)});
            if (cursor.moveToNext()) {
                address = cursor.getString(0);
            }
            cursor.close();
        } else if (number.matches("^\\d+$")) {
            switch (number.length()) {
                case 3:
                    address = "报警电话";
                    break;
                case 4:
                    address = "模拟器";
                    break;
                case 5:
                    address = "客服电话";
                    break;
                case 7:
                case 8:
                    address = "本地电话";
                    break;
                default:
                    //有可能是长途电话
                    if (number.startsWith("0") && number.length() > 10) {

                        //有些区号是4位(包括0)
                        Cursor cursor = database.rawQuery("select location from data2 where area=?",
                                new String[]{number.substring(1, 4)});
                        if (cursor.moveToNext()) {
                            address = cursor.getString(0);
                        } else {
                            cursor.close();

                            //有些区号是3位(包括0)
                            cursor = database.rawQuery("select location from data2 where area=?",
                                    new String[]{number.substring(1, 3)});
                            if (cursor.moveToNext()) {
                                address = cursor.getString(0);
                            }
                            cursor.close();
                        }
                    }
                    break;
            }
        } else {
            address = "请输入正确的电话号码";
        }
        database.close();
        return address;
    }


}
