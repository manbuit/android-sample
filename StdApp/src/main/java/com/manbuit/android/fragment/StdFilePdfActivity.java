package com.manbuit.android.fragment;

import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

//import com.joanzapata.pdfview.PDFView;
import com.manbuit.android.fragment.utils.FileUtils;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class StdFilePdfActivity extends AppCompatActivity {

    StdApp global;

    TextView stdFilePdf;
    //PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_std_file_pdf);

        global = (StdApp) getApplication();

        /*stdFilePdf = (TextView) findViewById(R.id.stdFilePdf);
        pdfView = (PDFView) findViewById(R.id.pdfview);

        Intent intent = getIntent();
        final String fileId = intent.getStringExtra("fileId");
        final String fileName = intent.getStringExtra("fileName");

        stdFilePdf.setText(fileId);

        final Handler pdfHandler = new Handler(){
            public void handleMessage(Message msg){
                String filePath = (String) msg.obj;
                //显示速度特别慢，可能需要通过fromAsset加载会快点
                pdfView.fromFile(new File(filePath))
                        //.pages(0, 2, 1, 3, 3, 3)
                        .defaultPage(1)
                        .showMinimap(false)
                        .enableSwipe(true)
                        //.onDraw(onDrawListener)
                        //.onLoad(onLoadCompleteListener)
                        //.onPageChange(onPageChangeListener)
                        .load();
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                String urlString = String.format(global.getFileDownloadUrl()+"/%s;jsessionid=%s",fileId,global.getMyContext().get("token"));
                try {
                    URL url = new URL(urlString);
                    HttpURLConnection conn=(HttpURLConnection)url.openConnection();

                    InputStream input = conn.getInputStream();
                    FileUtils fileUtils = new FileUtils();
                    File resultFile=fileUtils.write2SDFromInput("/jyjy/", fileName, input);

                    //Toast.makeText(StdFilePdfActivity.this,"下载成功:"+resultFile.getName(),Toast.LENGTH_SHORT).show();
                    //Toast.makeText(StdFilePdfActivity.this,"下载成功",Toast.LENGTH_SHORT).show();
                    System.out.println("下载成功！！！！！");
                    System.out.println(resultFile.getAbsolutePath());

                    Message msg = new Message();
                    msg.obj = resultFile.getAbsolutePath();
                    pdfHandler.sendMessage(msg);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();*/
    }
}
