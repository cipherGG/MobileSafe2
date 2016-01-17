package com.gg.app.mobilesafe2.bean;

import cn.bmob.v3.BmobObject;

public class Person extends BmobObject {

    private String username;
    private String password;

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

}
