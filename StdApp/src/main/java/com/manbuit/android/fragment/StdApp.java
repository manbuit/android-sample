package com.manbuit.android.fragment;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;

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

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

    private final static String URL_TEMPLATE = "http://%s:%s/%s";

    public String getLoginCheckUrl() {
        return String.format(
                URL_TEMPLATE,
                getString(R.string.r_server_host),
                getString(R.string.r_server_port),
                getString(R.string.r_login_path)
        );
    }

    public String getDataLoadUrl() {
        return String.format(
                URL_TEMPLATE,
                getString(R.string.r_server_host),
                getString(R.string.r_server_port),
                getString(R.string.r_data_load)
        );
    }

    public String getFileDownloadUrl() {
        return String.format(
                URL_TEMPLATE,
                getString(R.string.r_server_host),
                getString(R.string.r_server_port),
                getString(R.string.r_file_download)
        );
    }
}
