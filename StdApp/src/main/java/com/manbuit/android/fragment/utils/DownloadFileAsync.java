package com.manbuit.android.fragment.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by MB on 2015/12/3.
 */
public class DownloadFileAsync extends AsyncTask<Object, Integer, Uri> {
    ProgressDialog mProgressDialog;
    Handler handler;

    public DownloadFileAsync(Activity activity, Handler handler){

        this.handler = handler;

        mProgressDialog = new ProgressDialog(activity);
        mProgressDialog.setTitle("正在下载");
        mProgressDialog.setMessage("下载进度...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        //设置点击进度条外部，不响应；
        mProgressDialog.setCancelable(false);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog.show();
    }

    @Override
    protected Uri doInBackground(Object... params) {
        String urlString = params[0].toString();
        String fileName = params[1].toString();
        int fileLength = 0;
        if(params.length>2 && params[2]!=null) {
            fileLength = Integer.parseInt(params[2].toString());
        }

        File resultFile = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn=(HttpURLConnection)url.openConnection();

            InputStream input = conn.getInputStream();
            FileUtils fileUtils = new FileUtils();
            //File resultFile = fileUtils.write2SDFromInput("/jyjy/", fileName, input);

            OutputStream output = null;
            try{
                fileUtils.creatSDDir("/jyjy/");
                resultFile = fileUtils.creatSDFile("/jyjy/" + fileName);
                output = new FileOutputStream(resultFile);
                if(fileLength == 0) {
                    fileLength = conn.getContentLength();
                }
                byte buffer [] = new byte[4 * 1024];  //可以用了
                int total_length = 0;
                int len;
                int progress = 0;
                while ((len=input.read(buffer))!= -1) {
                    output.write(buffer,0,len);
                    total_length += len;
                    if((int) ((total_length / (float) fileLength) * 100) > progress){
                        progress = (int) ((total_length / (float) fileLength) * 100);
                        publishProgress(progress);
                    }
                }
                output.flush();
            }
            catch(Exception e){
                e.printStackTrace();
            }
            finally{
                try{
                    output.close();
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        Message msg = new Message();
        msg.obj = Uri.fromFile(resultFile);
        handler.sendMessage(msg);

        return Uri.fromFile(resultFile);
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        mProgressDialog.setProgress(progress[0]);
    }

    @Override
    protected void onPostExecute(Uri unused) {
        mProgressDialog.dismiss();
    }
}