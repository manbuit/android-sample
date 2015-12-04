package com.manbuit.android.fragment;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
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

    private SharedPreferences sp;

    public void setMyContext(Map context) {
        this.myContext = context;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        setMyContext(new LinkedHashMap()); //初始化上下文变量

        sp = getSharedPreferences("mysp", Context.MODE_PRIVATE);
    }

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

    private final static String URL_TEMPLATE = "http://%s:%s/%s";

    //环境参数：0表示正式环境；1表示测试环境；2表示开发环境
    private final int DEFAULT_INDEX = 2;

    public String getLoginCheckUrl() {
        String url = null;
        int choice = sp.getInt("env",DEFAULT_INDEX);
        switch(choice){
            case 0:
                url = String.format(
                        URL_TEMPLATE,
                        getString(R.string.r_server_host_p),
                        getString(R.string.r_server_port_p),
                        getString(R.string.r_login_path)
                );
                break;
            case 1:
                url = String.format(
                        URL_TEMPLATE,
                        getString(R.string.r_server_host_t),
                        getString(R.string.r_server_port_t),
                        getString(R.string.r_login_path)
                );
                break;
            case 2:
                url =  String.format(
                        URL_TEMPLATE,
                        getString(R.string.r_server_host_d),
                        getString(R.string.r_server_port_d),
                        getString(R.string.r_login_path)
                );
                break;
            default:
                break;
        }
        return url;
    }

    public String getDataLoadUrl() {
        String url = null;
        int choice = sp.getInt("env",DEFAULT_INDEX);
        switch(choice){
            case 0:
                url = String.format(
                        URL_TEMPLATE,
                        getString(R.string.r_server_host_p),
                        getString(R.string.r_server_port_p),
                        getString(R.string.r_data_load)
                );
                break;
            case 1:
                url = String.format(
                        URL_TEMPLATE,
                        getString(R.string.r_server_host_t),
                        getString(R.string.r_server_port_t),
                        getString(R.string.r_data_load)
                );
                break;
            case 2:
                url =  String.format(
                        URL_TEMPLATE,
                        getString(R.string.r_server_host_d),
                        getString(R.string.r_server_port_d),
                        getString(R.string.r_data_load)
                );
                break;
            default:
                break;
        }
        return url;
    }

    public String getFileDownloadUrl() {
        String url = null;
        int choice = sp.getInt("env",DEFAULT_INDEX);
        switch(choice){
            case 0:
                url = String.format(
                        URL_TEMPLATE,
                        getString(R.string.r_server_host_p),
                        getString(R.string.r_server_port_p),
                        getString(R.string.r_file_download)
                );
                break;
            case 1:
                url = String.format(
                        URL_TEMPLATE,
                        getString(R.string.r_server_host_t),
                        getString(R.string.r_server_port_t),
                        getString(R.string.r_file_download)
                );
                break;
            case 2:
                url =  String.format(
                        URL_TEMPLATE,
                        getString(R.string.r_server_host_d),
                        getString(R.string.r_server_port_d),
                        getString(R.string.r_file_download)
                );
                break;
            default:
                break;
        }
        return url;
    }

    public SharedPreferences getMySP(){
        return this.sp;
    }
}
