package com.manbuit.android.fragment.dataRequest;

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
}
