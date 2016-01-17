package com.gg.app.mobilesafe2.bean;

import android.graphics.drawable.Drawable;

/**
 * author cipherGG
 * Created by Administrator on 2015/12/23.
 * describe
 */
public class TaskInfo {

    private Drawable icon;
    private String name;
    private String packageName;
    private long memory;
    private boolean isUser;
    private boolean isCheck;

    public TaskInfo() {
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getMemory() {
        return memory;
    }

    public void setMemory(long memory) {
        this.memory = memory;
    }

    public boolean isUser() {
        return isUser;
    }

    public void setIsUser(boolean isUser) {
        this.isUser = isUser;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setIsCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
