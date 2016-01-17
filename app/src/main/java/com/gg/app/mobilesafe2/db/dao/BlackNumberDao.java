package com.gg.app.mobilesafe2.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;

import com.gg.app.mobilesafe2.bean.BlackNumberInfo;

import java.util.ArrayList;
import java.util.List;

public class BlackNumberDao {

    private final BlackNumberOpenHelper openHelper;


    public BlackNumberDao(Context context) {
        openHelper = new BlackNumberOpenHelper(context);
    }

    public boolean insert(String number, String mode) {
        SQLiteDatabase db = openHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("number", number);
        values.put("mode", mode);

        long rowId = db.insert(openHelper.table, null, values);
        return rowId != -1;
    }

    public boolean delete(String number) {
        SQLiteDatabase db = openHelper.getWritableDatabase();

        int rowNumber = db.delete(openHelper.table, "number=?", new String[]{number});
        return rowNumber != 0;
    }

    public boolean update(String number, String mode) {
        SQLiteDatabase db = openHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("mode", mode);

        int rowNumber = db.update(openHelper.table, values, "number=?", new String[]{number});
        return rowNumber != 0;
    }

    public String query(String number) {
        String mode = "";
        SQLiteDatabase db = openHelper.getReadableDatabase();

        Cursor cursor = db.query(openHelper.table, new String[]{"mode"}, "number=?",
                new String[]{number}, null, null, null);

        if (cursor.moveToNext()) {
            mode = cursor.getString(0);
        }

        cursor.close();
        db.close();
        return mode;
    }

    public List<BlackNumberInfo> queryAll() {
        List<BlackNumberInfo> list = new ArrayList<>();

        SQLiteDatabase db = openHelper.getReadableDatabase();
        Cursor cursor = db.query(openHelper.table, new String[]{"number", "mode"}, null, null, null, null, null);

        while (cursor.moveToNext()) {
            BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
            blackNumberInfo.setNumber(cursor.getString(0));
            blackNumberInfo.setMode(cursor.getString(1));

            list.add(blackNumberInfo);
        }

        cursor.close();
        db.close();
        //SystemClock.sleep(2000);
        return list;
    }

    /**
     * 分页加载数据,用sql语句实现的
     *
     * @param pagerNumber 当前是哪一页
     * @param pageSize    每一页有多少条数据
     * @return
     */
    public List<BlackNumberInfo> queryPager(int pagerNumber, int pageSize) {
        List<BlackNumberInfo> list = new ArrayList<>();

        SQLiteDatabase db = openHelper.getReadableDatabase();
        //limit 是限制， offset 是忽略
        Cursor cursor = db.rawQuery("select number,mode from " + openHelper.table + " limit ? offset ?",
                new String[]{String.valueOf(pageSize), String.valueOf(pageSize * pagerNumber)});

        while (cursor.moveToNext()) {
            BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
            blackNumberInfo.setNumber(cursor.getString(0));
            blackNumberInfo.setMode(cursor.getString(1));

            list.add(blackNumberInfo);
        }

        cursor.close();
        db.close();
        //睡睡更健康！
        SystemClock.sleep(2000);
        return list;
    }


}
