package com.manbuit.android.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Config;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.manbuit.android.fragment.dataRequest.DataRequest;
import com.manbuit.android.fragment.dataRequest.DataRequestUnit;
import com.manbuit.android.fragment.dataRequest.Filter;
import com.manbuit.android.fragment.dataRequest.OrderBy;
import com.manbuit.android.fragment.utils.FileUtils;

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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class OtherFragment extends Fragment {

    private StdApp global;

    Button btnUpdate;
    Button btnTestDataRequest;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View messageLayout = inflater.inflate(R.layout.activity_other, container, false);
        return messageLayout;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        global = (StdApp) getActivity().getApplication();

        btnUpdate = (Button) getActivity().findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final DataRequest dataRequest = new DataRequest();
                DataRequestUnit data = new DataRequestUnit();
                data.setDs("cc405ad5-7db7-279f-a2e8-7a96dd45135f"); //角色

                {
                    List<OrderBy> orderBies = new ArrayList<OrderBy>();

                    OrderBy cTimeStamp = new OrderBy();
                    //categoryOrderNo.setExp("(select orderNo from jyjy_std_category where id=rptSql.category)");
                    cTimeStamp.setExp("cTimeStamp");
                    cTimeStamp.setAsc(false);
                    orderBies.add(cTimeStamp);

                    data.setOrderbies(orderBies);
                }

                dataRequest.getRoot().add(data);

                RequestQueue queue = Volley.newRequestQueue(getActivity());
                String url = String.format("%s;jsessionid=%s",global.getDataLoadUrl(),global.getMyContext().get("token"));

                StringRequest request = new StringRequest(
                        Request.Method.POST,
                        url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                //Toast.makeText(LoginActivity.this,s,Toast.LENGTH_SHORT).show();
                                try {
                                    JSONObject root = new JSONObject(s).getJSONObject("root");
                                    final String rev = root.getString("rev");
                                    final String apkFileId = root.getString("apk");

                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.setTitle("title");
                                    PackageInfo info = null;
                                    try {
                                        info = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(),0);
                                    } catch (PackageManager.NameNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                    if(rev.equals(info.versionName)) {
                                        builder.setMessage(
                                                String.format("本机版本：%s\r\n最新版本：%s\r\n\r\n版本相同，不需要更新。", info.versionName, rev)
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
                                                //Toast.makeText(getActivity(), "确定", Toast.LENGTH_SHORT).show();

                                                new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        String urlString = String.format(global.getFileDownloadUrl()+"/%s;jsessionid=%s",apkFileId,global.getMyContext().get("token"));
                                                        try {
                                                            URL url = new URL(urlString);
                                                            HttpURLConnection conn=(HttpURLConnection)url.openConnection();
                                                            String fileName = String.format("jyjy_std_%s.apk",rev);

                                                            InputStream input = conn.getInputStream();
                                                            FileUtils fileUtils = new FileUtils();
                                                            File resultFile=fileUtils.write2SDFromInput("/jyjy/", fileName, input);

                                                            //Toast.makeText(StdFilePdfActivity.this,"下载成功:"+resultFile.getName(),Toast.LENGTH_SHORT).show();
                                                            //Toast.makeText(StdFilePdfActivity.this,"下载成功",Toast.LENGTH_SHORT).show();
                                                            System.out.println("下载成功！！！！！");
                                                            System.out.println(resultFile.getAbsolutePath());

                                                            Intent intent = new Intent(Intent.ACTION_VIEW);
                                                            intent.setDataAndType(Uri.fromFile(new File(resultFile.getAbsolutePath())),
                                                                    "application/vnd.android.package-archive");
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
                                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Toast.makeText(getActivity(), "取消更新", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                    builder.create().show();
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                    Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                //showProgress(false);
                                Toast.makeText(getActivity(), volleyError.getMessage(), Toast.LENGTH_SHORT).show();
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
        });

        btnTestDataRequest = (Button) getActivity().findViewById(R.id.btnTestDataRequest);
        btnTestDataRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DataRequest dataRequest = new DataRequest();
                DataRequestUnit data = new DataRequestUnit();
                data.setDs("2ee611a7-a3b6-4e7a-8f5f-677f0cad8b46"); //角色

                {
                    Filter AND = new Filter();
                    AND.setExp("and");
                    AND.setLeaf(false);
                    List<Filter> AND_children = new ArrayList<Filter>();
                    AND.setChildren(AND_children);
                    {
                        Filter filter = new Filter();
                        filter.setExp("code");
                        filter.setValue("'系统'");
                        filter.setOperate(">");
                        filter.setType("string");
                        AND_children.add(filter);
                    }
                    {
                        Filter filter = new Filter();
                        filter.setExp("name");
                        filter.setValue("'员'");
                        filter.setOperate(">");
                        filter.setType("string");
                        AND_children.add(filter);
                    }

                    data.setFilter(AND);
                }

                dataRequest.getNodes().put("data", data);

                RequestQueue queue = Volley.newRequestQueue(getActivity());
                String url = String.format("%s;jsessionid=%s",global.getDataLoadUrl(),global.getMyContext().get("token"));

                /*JsonObjectRequest request = new JsonObjectRequest(
                        Request.Method.POST,
                        url,
                        dataRequest.toJSON(),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject obj) {
                                Toast.makeText(getActivity(),obj.toString(),Toast.LENGTH_SHORT).show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_SHORT).show();
                                System.out.println(error.getMessage());
                        }
                });*/

                StringRequest request = new StringRequest(
                        Request.Method.POST,
                        url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                //Toast.makeText(LoginActivity.this,s,Toast.LENGTH_SHORT).show();
                                try {
                                    JSONObject result = new JSONObject(s);
                                    JSONObject data = result.getJSONObject("data");
                                    Toast.makeText(getActivity(),data.get("totalCount").toString(),Toast.LENGTH_SHORT).show();
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                    Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                //showProgress(false);
                                Toast.makeText(getActivity(), volleyError.getMessage(), Toast.LENGTH_SHORT).show();
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
        });
    }
}
