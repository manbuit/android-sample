package com.manbuit.android.fragment.dataRequest;

import android.app.Application;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.manbuit.android.fragment.StdApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by MB on 2015/11/26.
 */
public class DataRequest {

    /*private final static String BASEURL = "222.190.98.24:8099";
    //private final static String BASEURL = "192.168.1.70:8080";

    private final static String LOGIN_URL = "http://%s/logincheck?json";
    private final static String DATA_LOAD_URL = "http://%s/api/data/load";
    private final static String DATA_UPDATE_URL = "http://%s/api/data/load";
    private final static String FILE_DOWNLOAD_URL = "http://%s/api/file/download";*/

    Map<String,Object> params = new HashMap<String,Object>();
    List<DataRequestUnit> root = new ArrayList<DataRequestUnit>();
    Map<String,DataRequestUnit> nodes = new LinkedHashMap<String, DataRequestUnit>();

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public List<DataRequestUnit> getRoot() {
        return root;
    }

    public void setRoot(List<DataRequestUnit> root) {
        this.root = root;
    }

    public Map<String, DataRequestUnit> getNodes() {
        return nodes;
    }

    public void setNodes(Map<String, DataRequestUnit> nodes) {
        this.nodes = nodes;
    }

    public JSONObject toJSON() {

        JSONObject jsonObject= new JSONObject();

        try {
            JSONArray root = new JSONArray();
            for(DataRequestUnit u : this.root){
                root.put(u.toJSON());
            }
            jsonObject.put("root",root);

            JSONObject nodes = new JSONObject();
            for(String key : this.getNodes().keySet()){
                nodes.put(key, this.getNodes().get(key).toJSON());
            }
            jsonObject.put("nodes",nodes);

            JSONObject params = new JSONObject();
            for(String key : this.getParams().keySet()){
                params.put(key,this.getParams().get(key));
            }
            jsonObject.put("params", params);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public Request genRequest(final StdApp app, final Handler handler){
        final DataRequest me = this;

        String url = String.format("%s;jsessionid=%s", app.getDataLoadUrl(), app.getMyContext().get("token").toString());

        Request request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Toast.makeText(LoginActivity.this,s,Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject result = new JSONObject(s);
                            //JSONObject data = result.getJSONObject("data");
                            //Toast.makeText(getActivity(),data.get("totalCount").toString(),Toast.LENGTH_SHORT).show();
                            Message msg = new Message();
                            msg.obj = result;
                            handler.sendMessage(msg);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                            // TODO 这里需要增加解析错误后的处理
                            //Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        // TODO 这里需要增加请求错误后的处理
                        //showProgress(false);
                        Toast.makeText(app.getApplicationContext(), volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                        //System.out.println(volleyError.getMessage());
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() {
                //在这里设置需要post的参数
                Map<String, String> map = new HashMap<String, String>();
                map.put("dr", me.toJSON().toString());
                return map;
            }
        };
        //queue.add(request);

        return request;
    }
}
