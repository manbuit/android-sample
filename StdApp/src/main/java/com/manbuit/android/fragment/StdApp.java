package com.manbuit.android.fragment;

import android.app.Application;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by MB on 2015/11/24.
 */
public class StdApp extends Application {
    private Map myContext;

    public Map getMyContext() {
        return myContext;
    }

    public void setMyContext(Map context) {
        this.myContext = context;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        setMyContext(new LinkedHashMap()); //初始化上下文变量
    }

    public String getDataLoadUrl() {
        return "http://222.190.98.24:8099/api/data/load";
//        return "http://192.168.1.70:8080/api/data/load";
    }

    public String getLoginCheckUrl() {
        return "http://222.190.98.24:8099/logincheck?json";
//        return "http://192.168.1.70:8080/logincheck?json";
    }
}
