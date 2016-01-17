package com.gg.app.mobilesafe2.bean;

import cn.bmob.v3.BmobObject;

public class Version extends BmobObject {
    String name;
    String code;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
