package com.gg.app.mobilesafe2.db.dao;

import android.content.Context;
import android.widget.Toast;

import com.gg.app.mobilesafe2.bean.Person;
import com.gg.app.mobilesafe2.bean.Version;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class BombDao {

    public static void initBomb(Context context) {
        Bmob.initialize(context, "ced757b19f87562a25bde117d2b8e9c3");
    }


    public static void queryVersionCode(final Context context, final String code, final Callback3 callback3) {
        BmobQuery<Version> query = new BmobQuery<>();

        query.findObjects(context, new FindListener<Version>() {
            @Override
            public void onSuccess(List<Version> list) {
                int maxCode = Integer.parseInt(code);
                String maxName = "";
                for (Version version : list) {
                    int queryCode = Integer.parseInt(version.getCode());
                    if (queryCode > maxCode) {
                        maxCode = queryCode;
                        maxName = version.getName();
                    }
                }
                if (maxCode > Integer.parseInt(code)) {
                    callback3.toUpdate(maxCode + "", maxName);
                } else {
                    callback3.noUpdate();
                }
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(context, "检查更新失败" + i + "\n" + s, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public static void insertPerson(final Context context, final String username, String password,
                                    final Callback1 callback1) {
        final Person person = new Person();
        person.setUsername(username);
        person.setPassword(password);

        person.save(context, new SaveListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(context, "用户注册成功", Toast.LENGTH_SHORT).show();
                //MyConstants.PersonObjectId = person.getObjectId();
                //MyConstants.PersonUsername = username;
                callback1.action();
            }

            @Override
            public void onFailure(int code, String msg) {
                Toast.makeText(context, "用户注册失败" + code + "\n" + msg, Toast.LENGTH_SHORT).show();
            }
        });
    }


    public static void queryPerson(final Context context, final String username,
                                   final String password, final Callback2 callback2) {
        BmobQuery<Person> query = new BmobQuery<>();
        query.addWhereEqualTo("username", username);

        query.findObjects(context, new FindListener<Person>() {
            @Override
            public void onSuccess(List<Person> list) {
                String outUsername = "";
                String outPassword = "";
                String outObjectId = "";
                for (Person person : list) {
                    outUsername = person.getUsername();
                    outPassword = person.getPassword();
                    outObjectId = person.getObjectId();
                }
                if (!outUsername.equals("")) {
                    if (password.equals(outPassword)) {
                        Toast.makeText(context, "登录成功", Toast.LENGTH_SHORT).show();
                        //MyConstants.PersonObjectId = outObjectId;
                        //MyConstants.PersonUsername = outUsername;
                        //MyConstants.PersonPassword = outPassword;
                        callback2.action1();
                    } else if (!password.equals(outPassword)) {
                        Toast.makeText(context, "密码错误", Toast.LENGTH_SHORT).show();
                        callback2.action2();
                    }
                } else {
                    callback2.action3();
                }

            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(context, "查询用户信息失败" + i + "\n" + s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface Callback1 {
        void action();
    }

    public interface Callback2 {
        void action1();

        void action2();

        void action3();
    }

    public interface Callback3 {
        void toUpdate(String code, String name);

        void noUpdate();
    }

}
