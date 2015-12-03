package com.manbuit.android.fragment.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by MB on 2015/11/27.
 */
public class FileUtils {

    private String SDPATH;

    public String getSDPATH() {
        return SDPATH;
    }
    public FileUtils() {
        //得到当前外部存储设备的目录
        // /SDCARD
        SDPATH = Environment.getExternalStorageDirectory() + "/";
    }
    /**
     * 在SD卡上创建文件
     *
     * @throws IOException
     */
    public File creatSDFile(String fileName) throws IOException {
        File file = new File(SDPATH + fileName);
        file.createNewFile();
        return file;
    }

    /**
     * 在SD卡上创建目录
     *
     * @param dirName
     */
    public File creatSDDir(String dirName) {
        File dir = new File(SDPATH + dirName);
        dir.mkdir();
        return dir;
    }

    /**
     * 判断SD卡上的文件夹是否存在
     */
    public boolean isFileExist(String fileName){
        File file = new File(SDPATH + fileName);
        return file.exists();
    }

    /**
     * 将一个InputStream里面的数据写入到SD卡中
     */
    public File write2SDFromInput(String path,String fileName,InputStream input){
        File file = null;
        OutputStream output = null;
        try{
            creatSDDir(path);
            file = creatSDFile(path + fileName);
            output = new FileOutputStream(file);
            byte buffer [] = new byte[4 * 1024];
            /*while((input.read(buffer)) != -1){
                output.write(buffer);
            }*/
            int len;
            while ((len=input.read(buffer))!= -1) {
                output.write(buffer,0,len);
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
        return file;
    }

    public static String getMimeTypeFromExtension(String extensioin) {
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extensioin.replaceAll("(\\.)",""));
    }

    public static String getMimeTypeFromFileName(String fileName) {
        String extension = fileName.split("\\.")[fileName.split("\\.").length-1];
        return getMimeTypeFromExtension(extension);
    }

    public static void openFile(Activity activity, Uri uri) {
        String fileName = null;
        try {
            fileName = URLDecoder.decode(uri.toString(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String mimeType = FileUtils.getMimeTypeFromFileName(fileName);
        if(!TextUtils.isEmpty(mimeType)) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, FileUtils.getMimeTypeFromFileName(fileName));

            ResolveInfo ri = activity.getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
            if(ri==null) {
                new AlertDialog.Builder(activity)
                        .setTitle("无法打开")
                        .setMessage(String.format("无法打开\r\n%s", fileName))
                        .setPositiveButton("确定", null)
                        .create()
                        .show();
            }
            else {
                activity.startActivity(intent);
            }
        }
        else {
            new AlertDialog.Builder(activity)
                    .setTitle("未知的文件类型")
                    .setMessage(String.format("未知的文件类型\r\n%s",fileName))
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .create()
                    .show();
        }
    }
}