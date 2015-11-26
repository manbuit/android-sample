package com.manbuit.android.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.manbuit.android.fragment.dataRequest.DataRequest;
import com.manbuit.android.fragment.dataRequest.DataRequestUnit;
import com.manbuit.android.fragment.dataRequest.Filter;
import com.manbuit.android.fragment.model.StdEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StdDetailActivity extends AppCompatActivity {

    private StdApp global;
    TextView tvCode;
    TextView tvName;
    ListView tvFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_std_detail);

        tvCode = (TextView) findViewById(R.id.detail_tv_code);
        tvName = (TextView) findViewById(R.id.detail_tv_name);
        tvFiles = (ListView) findViewById(R.id.detail_tv_files);

        global = (StdApp)getApplication();

        Intent intent = getIntent();
        final String stdId = intent.getStringExtra("stdId");


        final Handler loadDataHandler = new Handler(){
            public void handleMessage(Message msg){
                JSONObject result = (JSONObject) msg.obj;
                try {
                    JSONObject root = result.getJSONObject("root");
                    tvCode.setText(root.getString("code"));
                    tvName.setText(root.getString("name"));


                    JSONArray files = result.getJSONObject("files").getJSONArray("data");
                    List<String> _files = new ArrayList<>();
                    for(int i=0;i< files.length();i++){
                        _files.add(files.getJSONObject(i).getString("name"));
                    }
                    ArrayAdapter arrayAdapter = new ArrayAdapter<String>(
                            StdDetailActivity.this,
                            android.R.layout.simple_expandable_list_item_1,
                            _files
                    );
                    tvFiles.setAdapter(arrayAdapter);
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
