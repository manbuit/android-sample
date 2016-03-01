package com.manbuit.android.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.manbuit.android.fragment.utils.DownloadFileAsync;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by MB on 2015/12/6.
 */
public class UpdateAPK {

    /**
     * 检查更新，并提示
     * @param activity
     * @param alert 当版本相同时，是否提示，自动检查更新时，通常不需要更新时不用提示，而手工检查更新时需要提示
     */
    public static void update(final Activity activity, final boolean alert){

        final StdApp global = (StdApp)activity.getApplication();

        Request request = new StringRequest(
                Request.Method.GET,
                global.getApkUpdateUrl(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject result = new JSONObject(s);

                            JSONObject root = result.getJSONObject("root");
                            final String rev = root.getString("rev");
                            final String apkFileId = root.getString("apk");

                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                            builder.setTitle("版本检测");
                            PackageInfo info = null;
                            try {
                                info = activity.getPackageManager().getPackageInfo(activity.getPackageName(),0);
                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                            }
                            if(rev.equals(info.versionName)) {
                                if(!alert) {
                                    return;
                                }
                                builder.setMessage(
                                        String.format("本机：%s\r\n最新：%s\r\n\r\n版本相同，不需要更新。", info.versionName, rev)
                                );
                                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //Toast.makeText(getActivity(), "确定", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else {
                                builder.setMessage(
                                        String.format("本机版本：%s\r\n最新版本：%s\r\n\r\n确定更新到最新版本吗？", info.versionName, rev)
                                );
                                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        AsyncTask task = new DownloadFileAsync(
                                                activity,
                                                new Handler(){
                                                    @Override
                                                    public void handleMessage(Message msg) {
                                                        Uri uri = (Uri) msg.obj;

                                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                                        intent.setDataAndType(uri,
                                                                "application/vnd.android.package-archive");
                                                        activity.startActivity(intent);
                                                    }
                                                }
                                        );
                                        String urlString = String.format(global.getFileDownloadUrl()+"/%s.apk;jsessionid=%s",apkFileId,global.getMyContext().get("token"));
                                        task.execute(urlString,String.format("stdApp_%s.apk",rev));
                                    }
                                });
                                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(activity, "取消更新", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            builder.create().show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        // TODO 这里需要增加请求错误后的处理
                        //showProgress(false);
                        //Toast.makeText(app.getApplicationContext(), volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                        //System.out.println(volleyError.getMessage());
                    }
                }
        );

        global.getRequestQueue().add(request);
    }
}
