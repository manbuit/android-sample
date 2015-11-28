package com.manbuit.android.fragment;

import android.app.Activity;
import android.content.Intent;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StdDetailActivity extends AppCompatActivity {

    private StdApp global;
    TextView tvCode;
    TextView tvName;
    ListView tvFiles;

    List<StdFileEntity> stdFileEntities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_std_detail);

        global = (StdApp)getApplication();

        tvCode = (TextView) findViewById(R.id.detail_tv_code);
        tvName = (TextView) findViewById(R.id.detail_tv_name);
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

                //用第三方应用查看pdf
                /*Uri fileUri = Uri.parse(String.format(global.getFileDownloadUrl()+"/%s;jsessionid=%s.pdf",fileId,global.getMyContext().get("token")));
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setAction(Intent.ACTION_VIEW);
                //intent.setDataAndType(Uri.fromFile(resultFile), "application/pdf");
                intent.setDataAndType(fileUri, "application/pdf");
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

        final Handler loadDataHandler = new Handler(){
            public void handleMessage(Message msg){
                JSONObject result = (JSONObject) msg.obj;
                try {
                    JSONObject root = result.getJSONObject("root");
                    tvCode.setText(root.getString("code"));
                    tvName.setText(root.getString("name"));


                    JSONArray files = result.getJSONObject("files").getJSONArray("data");
                    //List<String> _files = new ArrayList<>();
                    for(int i=0;i< files.length();i++){
                        //_files.add(files.getJSONObject(i).getString("name"));
                        JSONObject item = files.getJSONObject(i);
                        String id = item.getString("id");
                        String name = item.getString("name");
                        StdFileEntity stdFileEntity = new StdFileEntity(id,name);
                        stdFileEntities.add(stdFileEntity);
                    }

                    StdFileListAdapter adapter = new StdFileListAdapter(StdDetailActivity.this,stdFileEntities);
                    /*ArrayAdapter adapter = new ArrayAdapter<String>(
                            StdDetailActivity.this,
                            //android.R.layout.simple_expandable_list_item_1,
                            android.R.layout.simple_list_item_1,
                            _files
                    );*/
                    tvFiles.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                final DataRequest dataRequest = new DataRequest();

                DataRequestUnit root = new DataRequestUnit();
                root.setDs("12814070-5537-b95c-e4f9-abfc0d460765");
                {
                    Filter AND = new Filter();
                    AND.setExp("and");
                    AND.setLeaf(false);
                    List<Filter> AND_children = new ArrayList<Filter>();
                    AND.setChildren(AND_children);
                    {
                        Filter filter = new Filter();
                        filter.setExp("id");
                        filter.setValue("'" + stdId + "'");
                        filter.setOperate("=");
                        filter.setType("string");
                        AND_children.add(filter);
                    }

                    root.setFilter(AND);
                }
                dataRequest.getRoot().add(root);

                DataRequestUnit files = new DataRequestUnit();
                //files.setDs("b12da205-2ce4-ce8b-ce13-f6fc40f944f1");
                files.setDs("b8b1dc9d-ec45-a057-f062-4238063267b4");
                {
                    Filter AND = new Filter();
                    AND.setExp("and");
                    AND.setLeaf(false);
                    List<Filter> AND_children = new ArrayList<Filter>();
                    AND.setChildren(AND_children);
                    {
                        Filter filter = new Filter();
                        filter.setExp("jyjy_std");
                        filter.setValue("'" + stdId + "'");
                        filter.setOperate("=");
                        filter.setType("string");
                        AND_children.add(filter);
                    }

                    files.setFilter(AND);
                }
                dataRequest.getNodes().put("files",files);

                RequestQueue queue = Volley.newRequestQueue(StdDetailActivity.this);

                String url = String.format("%s;jsessionid=%s", global.getDataLoadUrl(), global.getMyContext().get("token"));
                StringRequest request = new StringRequest(
                        Request.Method.POST,
                        url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                //Toast.makeText(LoginActivity.this,s,Toast.LENGTH_SHORT).show();
                                try {
                                    JSONObject result = new JSONObject(s);
                                    //JSONObject root = result.getJSONObject("root");
                                    //Toast.makeText(getActivity(),data.get("totalCount").toString(),Toast.LENGTH_SHORT).show();
                                    Message msg = new Message();
                                    msg.obj = result;
                                    loadDataHandler.sendMessage(msg);
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                    Toast.makeText(StdDetailActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                //showProgress(false);
                                Toast.makeText(StdDetailActivity.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                ){
                    @Override
                    protected Map<String, String> getParams() {
                        //在这里设置需要post的参数
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("dr", dataRequest.toJSON().toString());
                        return map;
                    }
                };
                queue.add(request);
            }
        }).start();
    }
}
