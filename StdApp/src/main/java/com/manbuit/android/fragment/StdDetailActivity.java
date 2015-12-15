package com.manbuit.android.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.manbuit.android.fragment.adapter.StdFileListAdapter;
import com.manbuit.android.fragment.adapter.StdListAdapter;
import com.manbuit.android.fragment.dataRequest.Column;
import com.manbuit.android.fragment.dataRequest.DataRequest;
import com.manbuit.android.fragment.dataRequest.DataRequestUnit;
import com.manbuit.android.fragment.dataRequest.Filter;
import com.manbuit.android.fragment.dataRequest.OrderBy;
import com.manbuit.android.fragment.model.StdEntity;
import com.manbuit.android.fragment.model.StdFileEntity;
import com.manbuit.android.fragment.utils.DownloadFileAsync;
import com.manbuit.android.fragment.utils.FileUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class StdDetailActivity extends AppCompatActivity {

    StdApp global;

    TextView tvCode;
    TextView tvName;
    TextView tvCategory;
    TextView tvFbrq;
    TextView tvSsrq;
    TextView tvFzrq;
    TextView tvSummary;
    TextView tvFilesTitle;
    ListView tvFiles;
    ListView tvOldVers;
    ListView tvNewVers;

    ScrollView scrollView;

    Toolbar toolbar;
    ShareActionProvider mShareActionProvider;

    RequestQueue queue;

    final Handler loadDataHandler = new Handler(){
        public void handleMessage(Message msg){
            JSONObject result = (JSONObject) msg.obj;
            try {
                JSONObject root = result.getJSONObject("root");

                toolbar.setTitle(root.getString("code"));
                toolbar.setTitleTextAppearance(StdDetailActivity.this, R.style.StdCodeTitle);
                toolbar.setSubtitle(root.getString("name"));
                toolbar.setSubtitleTextAppearance(StdDetailActivity.this, R.style.StdCodeSubTitle);
                if(root.getString("status").equals("作废")){
                    toolbar.setTitle(String.format("%s (%s)",root.getString("code"),"作废"));
                    toolbar.setTitleTextColor(Color.rgb(255, 0, 0));
                    scrollView.setBackgroundResource(R.drawable.repeat_bg);
                    //toolbar.setTitleTextAppearance(StdDetailActivity.this,R.style.StdCodeSubTitle);
                }
                else {
                    toolbar.setTitleTextColor(Color.rgb(0, 127, 0));
                }

                MenuItem favoriteMenu = toolbar.getMenu().findItem(R.id.action_favorite);
                if(!root.has("favoriteId") || TextUtils.isEmpty(root.getString("favoriteId"))){
                    favoriteMenu.setIcon(R.drawable.favorite72);
                    favoriteMenu.setTitle("添加收藏");
                }
                else {
                    favoriteMenu.setIcon(R.drawable.favorited72);
                    favoriteMenu.setTitle("取消收藏");
                    favoriteMenu.setTitleCondensed(root.getString("favoriteId"));
                }
                favoriteMenu.setVisible(true);

                tvCode.setHint(root.getString("code"));
                tvCode.setText(String.format("标准号：%s", root.getString("code")));
                tvName.setHint(root.getString("name"));
                //tvName.setText(String.format("标准名称：《%s》", root.getString("name")));
                tvName.setText(String.format("标准名称：%s", root.getString("name")));
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
                                root.get("ssrq") != null && !root.getString("ssrq").equals("null") ? format.format(new Date(root.getLong("ssrq"))) : ""
                        )
                );
                tvFzrq.setText(
                        String.format(
                                "废止日期：%s",
                                root.get("fzrq") != null && !root.getString("fzrq").equals("null") ? format.format(new Date(root.getLong("fzrq"))) : ""
                        )
                );
                tvSummary.setText(root.getString("summary").equals("null")?"":root.getString("summary"));

                int privilege = root.getInt("privilegeCount"); //全文访问权限，大于0表示有权限
                JSONArray files = result.getJSONObject("files").getJSONArray("data");
                if(privilege>0) {
                    tvFiles.setAdapter(new StdFileListAdapter(StdDetailActivity.this, files));
                }
                else {
                    tvFilesTitle.setTextColor(Color.rgb(255, 0, 0));
                    tvFilesTitle.setText(tvFilesTitle.getText() + "\r\n没有全文访问权限");
                }

                JSONArray oldVers = result.getJSONObject("oldVers").getJSONArray("data");
                tvOldVers.setAdapter(new StdListAdapter(StdDetailActivity.this,oldVers));

                JSONArray newVers = result.getJSONObject("newVers").getJSONArray("data");
                tvNewVers.setAdapter(new StdListAdapter(StdDetailActivity.this,newVers));

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

        final String stdId = getIntent().getStringExtra("stdId");

        tvCode = (TextView) findViewById(R.id.detail_tv_code);
        tvName = (TextView) findViewById(R.id.detail_tv_name);
        tvCategory = (TextView) findViewById(R.id.detail_tv_category);
        tvFbrq = (TextView) findViewById(R.id.detail_tv_fbrq);
        tvSsrq = (TextView) findViewById(R.id.detail_tv_ssrq);
        tvFzrq = (TextView) findViewById(R.id.detail_tv_fzrq);
        tvSummary = (TextView) findViewById(R.id.detail_tv_summary);
        tvFilesTitle = (TextView) findViewById(R.id.detail_tv_files_title);
        tvFiles = (ListView) findViewById(R.id.detail_tv_files);
        tvOldVers = (ListView) findViewById(R.id.detail_tv_old_vers);
        tvNewVers = (ListView) findViewById(R.id.detail_tv_new_vers);

        toolbar= (Toolbar) findViewById(R.id.toolbar_detail);
        toolbar.inflateMenu(R.menu.menu_detail);

        scrollView = (ScrollView) findViewById(R.id.scrollView);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final MenuItem item) {
                // Handle the menu item
                //Toast.makeText(MainActivity.this,item.getTitle(),Toast.LENGTH_SHORT).show();
                switch (item.getItemId()) {
                    case R.id.action_favorite:
                        //Toast.makeText(StdDetailActivity.this,item.getTitle(),Toast.LENGTH_SHORT).show();
                        if(item.getTitle().equals("添加收藏")){
                            try {
                                final String favoriteId = UUID.randomUUID().toString();
                                JSONObject itemFavorite = new JSONObject();
                                itemFavorite.put("ds", "db0efa35-3d09-f4b4-6e22-e1ee4690f008");
                                itemFavorite.put("_method", "PUT");
                                Map<String,Object> fieldValues = new LinkedHashMap<String, Object>();
                                fieldValues.put("id", favoriteId);
                                fieldValues.put("std",stdId);
                                fieldValues.put("owner",global.getMyContext().get("userId").toString());
                                JSONObject data = new JSONObject(fieldValues);
                                itemFavorite.put("data",data);
                                final JSONArray dr = new JSONArray();
                                dr.put(itemFavorite);

                                String url = String.format("%s;jsessionid=%s", global.getDataUpdateUrl(), global.getMyContext().get("token").toString());
                                Request request = new StringRequest(
                                        Request.Method.POST,
                                        url,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String s) {
                                                Toast.makeText(global.getApplicationContext(), "添加收藏成功", Toast.LENGTH_SHORT).show();
                                                item.setIcon(R.drawable.favorited72);
                                                item.setTitle("取消收藏");
                                                item.setTitleCondensed(favoriteId);
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError volleyError) {
                                                // TODO 这里需要增加请求错误后的处理
                                                //showProgress(false);
                                                Toast.makeText(global.getApplicationContext(), volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                                                //System.out.println(volleyError.getMessage());
                                            }
                                        }
                                ){
                                    @Override
                                    protected Map<String, String> getParams() {
                                        //在这里设置需要post的参数
                                        Map<String, String> map = new HashMap<String, String>();
                                        map.put("dr", dr.toString());
                                        return map;
                                    }
                                };
                                queue.add(request);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else if(item.getTitle().equals("取消收藏")){
                            try {
                                final String favoriteId = item.getTitleCondensed().toString();
                                JSONObject itemFavorite = new JSONObject();
                                itemFavorite.put("ds", "db0efa35-3d09-f4b4-6e22-e1ee4690f008");
                                itemFavorite.put("id", favoriteId);
                                itemFavorite.put("_method", "DELETE");
                                final JSONArray dr = new JSONArray();
                                dr.put(itemFavorite);

                                String url = String.format("%s;jsessionid=%s", global.getDataUpdateUrl(), global.getMyContext().get("token").toString());
                                Request request = new StringRequest(
                                        Request.Method.POST,
                                        url,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String s) {
                                                Toast.makeText(global.getApplicationContext(), "取消收藏成功", Toast.LENGTH_SHORT).show();
                                                item.setIcon(R.drawable.favorite72);
                                                item.setTitle("添加收藏");
                                                item.setTitleCondensed(null);
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError volleyError) {
                                                // TODO 这里需要增加请求错误后的处理
                                                //showProgress(false);
                                                Toast.makeText(global.getApplicationContext(), volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                                                //System.out.println(volleyError.getMessage());
                                            }
                                        }
                                ){
                                    @Override
                                    protected Map<String, String> getParams() {
                                        //在这里设置需要post的参数
                                        Map<String, String> map = new HashMap<String, String>();
                                        map.put("dr", dr.toString());
                                        return map;
                                    }
                                };
                                queue.add(request);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        break;
                    case R.id.action_share:
                        //Toast.makeText(MainActivity.this,item.getTitle(),Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("image/*");
                        intent.putExtra(Intent.EXTRA_SUBJECT, tvCode.getText());
                        intent.putExtra(
                                Intent.EXTRA_TEXT,
                                String.format("%s\r\n《%s》", tvCode.getHint(),tvName.getHint())
                        );
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(Intent.createChooser(intent, getTitle()));
                        Toast.makeText(StdDetailActivity.this,item.getTitle(),Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        tvOldVers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StdListAdapter adapter = (StdListAdapter) parent.getAdapter();

                Intent intent = new Intent();
                intent.putExtra("stdId", adapter.getItem(position).getId());
                intent.setClassName(StdDetailActivity.this, "com.manbuit.android.fragment.StdDetailActivity");
                startActivity(intent);
            }
        });

        tvNewVers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StdListAdapter adapter = (StdListAdapter) parent.getAdapter();

                Intent intent = new Intent();
                intent.putExtra("stdId", adapter.getItem(position).getId());
                intent.setClassName(StdDetailActivity.this, "com.manbuit.android.fragment.StdDetailActivity");
                startActivity(intent);
            }
        });

        //加载数据
        new Thread(new Runnable() {
            @Override
            public void run () {
                final DataRequest dataRequest = new DataRequest();

                //加载标准信息
                DataRequestUnit stdInfo = new DataRequestUnit();
                stdInfo.setDs("12814070-5537-b95c-e4f9-abfc0d460765");
                stdInfo.setFilter(new Filter("id", "string", "=", "'" + stdId + "'", null));
                dataRequest.getRoot().add(stdInfo);

                //访问权限
                DataRequestUnit privilege = new DataRequestUnit();
                privilege.setDs("d5c9db56-fdc4-de8f-0142-d397353af522");
                privilege.getParams().put("stdId", stdId);
                dataRequest.getRoot().add(privilege);

                //访问权限
                DataRequestUnit favorite = new DataRequestUnit();
                favorite.setDs("db0efa35-3d09-f4b4-6e22-e1ee4690f008");
                favorite.setFields(
                        Arrays.asList(
                                new Column("id", "favoriteId")
                        )
                );
                favorite.setFilter(
                        new Filter("and",null,null,null,
                            Arrays.asList(
                                    new Filter("owner","string","=",  String.format("'%s'",global.getMyContext().get("userId").toString()) ,null),
                                    new Filter("std","string","=", String.format("'%s'",stdId) ,null)
                            )
                    )
                );
                dataRequest.getRoot().add(favorite);

                //加载电子标准
                DataRequestUnit files = new DataRequestUnit();
                files.setDs("b8b1dc9d-ec45-a057-f062-4238063267b4");
                files.setFilter(new Filter("jyjy_std", "string", "=", "'" + stdId + "'", null));
                files.getOrderbies().add(new OrderBy("cTimeStamp", true));
                dataRequest.getNodes().put("files", files);

                //加载旧版标准
                DataRequestUnit oldVers = new DataRequestUnit();
                oldVers.setDs("a6fa4f2e-c7b4-1d20-7ab1-2611230f34eb");
                oldVers.getParams().put("newVerId", stdId);
                oldVers.setOrderbies(Arrays.asList(
                        new OrderBy("orderNo", false)
                ));
                dataRequest.getNodes().put("oldVers", oldVers);

                //加载新版标准
                DataRequestUnit newVers = new DataRequestUnit();
                newVers.setDs("089f3ce1-b10d-b255-0f3b-de6a91bc43f8");
                newVers.getParams().put("oldVerId", stdId);
                newVers.setOrderbies(Arrays.asList(
                        new OrderBy("orderNo", false)
                ));
                dataRequest.getNodes().put("newVers", newVers);

                Request request = dataRequest.genRequest(global, loadDataHandler);
                queue.add(request);
            }
        }).start();

        //电子标准点击打开
        tvFiles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                StdFileListAdapter adapter = (StdFileListAdapter) tvFiles.getAdapter();
                final String fileId = adapter.getItem(position).getId();
                final String fileName = adapter.getItem(position).getName();
                final int filSize = adapter.getItem(position).getSize();

                //用应用内的pdfViewer打开
                /*Intent intent = new Intent();
                intent.putExtra("fileId", stdFileEntities.get(position).getId());
                intent.putExtra("fileName", stdFileEntities.get(position).getName());
                intent.setClassName(StdDetailActivity.this, "com.manbuit.android.fragment.StdFilePdfActivity");
                startActivity(intent);*/

                //使用第三方应用打开
                /*new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String urlString = String.format(global.getFileDownloadUrl()+"/%s;jsessionid=%s",fileId,global.getMyContext().get("token"));
                        try {
                            URL url = new URL(urlString);
                            HttpURLConnection conn=(HttpURLConnection)url.openConnection();

                            InputStream input = conn.getInputStream();
                            FileUtils fileUtils = new FileUtils();
                            File resultFile = fileUtils.write2SDFromInput("/jyjy/", fileName, input);

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
                }).start();*/

                //new DownloadFileAsync(StdDetailActivity.this).execute(fileId,fileName,String.valueOf(filSize));

                int lastDotPosition = fileName.lastIndexOf(".");
                String fileNameWidthCache = String.format(
                        "%s-%s.%s",
                        fileName.substring(0, lastDotPosition),
                        fileId.substring(fileId.lastIndexOf("-") + 1),
                        fileName.substring(lastDotPosition + 1)
                );
                FileUtils fileUtils = new FileUtils();
                File file = new File(fileUtils.getSDPATH() + FileUtils.DOWNLOAD_DIR + fileNameWidthCache);
                if (file.exists()) {
                    FileUtils.openFile(StdDetailActivity.this, Uri.fromFile(file));
                } else {
                    String urlString = String.format(global.getFileDownloadUrl() + "/%s;jsessionid=%s", fileId, global.getMyContext().get("token"));
                    AsyncTask task = new DownloadFileAsync(
                            StdDetailActivity.this,
                            new Handler() {
                                public void handleMessage(Message msg) {
                                    Uri uri = (Uri) msg.obj;
                                    FileUtils.openFile(StdDetailActivity.this, uri);
                                }
                            }
                    );
                    task.execute(urlString, fileNameWidthCache);
                }

                /*try {
                    //new DownloadFileAsync(StdDetailActivity.this).execute(urlString,fileName);
                    Uri uri = new DownloadFileAsync(StdDetailActivity.this).execute(urlString,fileName).get();

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(uri, "application/pdf");
                    // TODO 电子标准不一定是PDF
                    startActivity(intent);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }*/
            }
        });
    }


}
