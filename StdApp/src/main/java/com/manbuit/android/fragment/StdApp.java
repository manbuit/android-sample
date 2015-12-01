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

    private final static String BASEURL = "192.168.1.70:8080";
    //private final static String BASEURL = "222.190.98.24:8099";

    private final static String LOGINURL = "http://%s/logincheck?json";
    private final static String DATAURL = "http://%s/api/data/load";
    private final static String FILEURL = "http://%s/api/file/download";

    public String getLoginCheckUrl() {
        return String.format(LOGINURL,BASEURL);
    }

    public String getDataLoadUrl() {
        return String.format(DATAURL,BASEURL);
    }

    public String getFileDownloadUrl() {
        return String.format(FILEURL,BASEURL);
    }
}
