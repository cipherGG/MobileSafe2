package com.gg.app.mobilesafe2;

import android.app.Application;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.gg.app.mobilesafe2.activity.TaskManagerActivity;
import com.gg.app.mobilesafe2.bean.TaskInfo;
import com.gg.app.mobilesafe2.db.dao.BlackNumberDao;
import com.gg.app.mobilesafe2.engine.TaskInfos;

import java.util.List;
import java.util.Random;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void testMethod() {
        List<TaskInfo> taskInfos = TaskInfos.getTaskInfos(getContext());

        Log.e("taskinfos", "------>" + taskInfos.toString());
    }


}