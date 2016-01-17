package com.gg.app.mobilesafe2;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.gg.app.mobilesafe2.bean.BlackNumberInfo;
import com.gg.app.mobilesafe2.db.dao.BlackNumberDao;

import java.util.List;
import java.util.Random;

public class BlackNumberDaoTest extends ApplicationTestCase<Application> {
    public BlackNumberDaoTest() {
        super(Application.class);
    }

    public void testInsert() {
        BlackNumberDao dao = new BlackNumberDao(getContext());

        Random random = new Random();

        for (int i = 0; i < 100; i++) {
            long number = 1234500000 + i;
            dao.insert(number + "", String.valueOf(random.nextInt(3) + 1));
        }
    }

    public void testDelete() {
        BlackNumberDao dao = new BlackNumberDao(getContext());

        boolean delete = dao.delete("1234500000");

        //新技能啊， get！
        assertEquals(true, delete);
    }

    public void testQuery() {
        BlackNumberDao dao = new BlackNumberDao(getContext());

        String query = dao.query("1234500001");
        Log.e("info", "-----" + query);
    }

    public void testQueryAll() {
        BlackNumberDao dao = new BlackNumberDao(getContext());

        List<BlackNumberInfo> list = dao.queryAll();

        for (BlackNumberInfo info : list) {
            Log.e("info", info.getNumber() + "-----" + info.getMode());
        }

    }
}
