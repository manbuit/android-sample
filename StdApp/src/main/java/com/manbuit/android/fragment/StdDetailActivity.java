package com.manbuit.android.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.manbuit.android.fragment.adapter.StdFileListAdapter;
import com.manbuit.android.fragment.adapter.StdListAdapter;
import com.manbuit.android.fragment.dataRequest.DataRequest;
import com.manbuit.android.fragment.dataRequest.DataRequestUnit;
import com.manbuit.android.fragment.dataRequest.Filter;
import com.manbuit.android.fragment.dataRequest.OrderBy;
import com.manbuit.android.fragment.model.StdEntity;
import com.manbuit.android.fragment.model.StdFileEntity;
import com.manbuit.android.fragment.utils.FileUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StdDetailActivity extends AppCompatActivity {

    private StdApp global;
    TextView tvCode;
    TextView tvName;
    TextView tvCategory;
    TextView tvFbrq;
    TextView tvSsrq;
    TextView tvFzrq;
    TextView tvSummary;
    ListView tvFiles;

    List<StdFileEntity> stdFileEntities;

    RequestQueue queue;


    final Handler loadDataHandler = new Handler(){
        public void handleMessage(Message msg){
            JSONObject result = (JSONObject) msg.obj;
            try {
                JSONObject root = result.getJSONObject("root");
                tvCode.setText(String.format("标准号：%s",root.getString("code")));
                if(root.getString("status").equals("作废")){
                    tvCode.setTextColor(Color.rgb(255, 0, 0));
                }
                else {
                    tvCode.setTextColor(Color.rgb(0, 255, 0));
                }
                tvName.setText(String.format("标准名称：《%s》", root.getString("name")));
                tvCategory.setText(String.format("标准类别：%s", root.getString("category_name")));
                SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss
                tvFbrq.setText(
                        String.format(
                                "发布日期：%s",
                                root.get("fbrq")!=null && !root.getString("fbrq").equals("null") ? format.format(new Date(root.getLong("fbrq"))) : ""
                        )
                );
                tvSsrq.setText(
                        String.format(
                                "实施日期：%s",
                                root.get("ssrq")!=null && !root.getString("ssrq").equals("null") ? format.format(new Date(root.getLong("ssrq"))) : ""
                        )
                );
                tvFzrq.setText(
                        String.format(
                                "废止日期：%s",
                                root.get("fzrq")!=null && !root.getString("fzrq").equals("null") ? format.format(new Date(root.getLong("fzrq"))) : ""
                        )
                );
                tvSummary.setText(root.getString("summary"));

                JSONArray files = result.getJSONObject("files").getJSONArray("data");
                StdFileListAdapter adapter = new StdFileListAdapter(StdDetailActivity.this,files);
                tvFiles.setAdapter(adapter);


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_std_detail);

        global = (StdApp)getApplication();

        queue = Volley.newRequestQueue(StdDetailActivity.this);

        tvCode = (TextView) findViewById(R.id.detail_tv_code);
        tvName = (TextView) findViewById(R.id.detail_tv_name);
        tvCategory = (TextView) findViewById(R.id.detail_tv_category);
        tvFbrq = (TextView) findViewById(R.id.detail_tv_fbrq);
        tvSsrq = (TextView) findViewById(R.id.detail_tv_ssrq);
        tvFzrq = (TextView) findViewById(R.id.detail_tv_fzrq);
        tvSummary = (TextView) findViewById(R.id.detail_tv_summary);
        tvFiles = (ListView) findViewById(R.id.detail_tv_files);

        tvFiles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final String fileId = stdFileEntities.get(position).getId();
                final String fileName = stdFileEntities.get(position).getName();

                //用应用内的pdfViewer打开
                /*Intent intent = new Intent();
                intent.putExtra("fileId", stdFileEntities.get(position).getId());
                intent.putExtra("fileName", stdFileEntities.get(position).getName());
                intent.setClassName(StdDetailActivity.this, "com.manbuit.android.fragment.StdFilePdfActivity");
                startActivity(intent);*/

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

                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.fromFile(resultFile), "application/pdf");
                            // TODO 电子标准不一定是PDF
                            startActivity(intent);

                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });


        Intent intent = getIntent();
        final String stdId = intent.getStringExtra("stdId");

        stdFileEntities = new ArrayList<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                final DataRequest dataRequest = new DataRequest();

                DataRequestUnit root = new DataRequestUnit();
                root.setDs("12814070-5537-b95c-e4f9-abfc0d460765");
                root.setFilter(
                        new Filter("and", null, null, null,
                                Arrays.asList(
                                        new Filter("id", "string", "=", "'" + stdId + "'", null)
                                )
                        )
                );
                dataRequest.getRoot().add(root);

                DataRequestUnit files = new DataRequestUnit();
                files.setDs("b8b1dc9d-ec45-a057-f062-4238063267b4");
                files.setFilter(new Filter("jyjy_std", "string", "=", "'" + stdId + "'", null));
                files.getOrderbies().add(new OrderBy("cTimeStamp", true));
                dataRequest.getNodes().put("files", files);

                DataRequestUnit oldVers = new DataRequestUnit();
                oldVers.setDs("089f3ce1-b10d-b255-0f3b-de6a91bc43f8");
                oldVers.getParams().put("oldVerId", stdId);
                oldVers.setOrderbies(Arrays.asList(
                        new OrderBy("orderNo",false)
                ));
                dataRequest.getNodes().put("oldVers", oldVers);

                DataRequestUnit newVers = new DataRequestUnit();
                newVers.setDs("a6fa4f2e-c7b4-1d20-7ab1-2611230f34eb");
                newVers.getParams().put("newVerId", stdId);
                newVers.setOrderbies(Arrays.asList(
                        new OrderBy("orderNo",false)
                ));
                dataRequest.getNodes().put("newVers", newVers);

                Request request = dataRequest.genRequest(global.getMyContext().get("token").toString(),loadDataHandler);
                queue.add(request);
            }
        }).start();

        //findViewById(R.layout.activity_std_detail).scrollTo(0,10);
    }
}
