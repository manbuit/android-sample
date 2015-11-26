package com.manbuit.android.fragment.dataRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by MB on 2015/11/26.
 */
public class DataRequestUnit {
    private String ds;
    public String getDs() {
        return ds;
    }
    public void setDs(String ds) {
        this.ds = ds;
    }

    private Filter filter;
    public Filter getFilter() {
        return filter;
    }
    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    private List<OrderBy> orderbies = new ArrayList<OrderBy>();
    public List<OrderBy> getOrderbies() {
        return orderbies;
    }
    public void setOrderbies(List<OrderBy> orderbies) {
        this.orderbies = orderbies;
    }

    private Map<String,Object> params = new HashMap<String, Object>();
    public Map<String,Object> getParams() {
        if(this.params==null){
            this.params = new HashMap<String, Object>();
        }
        return this.params;
    }
    public void setParams(Map<String,Object> params) {
        this.params = params;
    }

    private List<Column> fields = new ArrayList<>();
    public List<Column> getFields() {
        return fields;
    }
    public void setFields(List<Column> fields) {
        this.fields = fields;
    }

    private Integer start = 0;
    public Integer getStart() {
        return start;
    }
    public void setStart(Integer start) {
        this.start = start;
    }

    private Integer limit = 0;
    public Integer getLimit() {
        return limit;
    }
    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    private String process;
    public String getProcess() {
        return process;
    }
    public void setProcess(String process) {
        this.process = process;
    }

    public JSONObject toJSON(){

        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("ds", getDs());

            JSONArray fields = new JSONArray();
            for(Column column : this.getFields()){
                fields.put(column.toJSON());
            }
            jsonObject.put("fields", fields);

            JSONArray orderbies = new JSONArray();
            for(OrderBy orderBy : this.getOrderbies()){
                orderbies.put(orderBy.toJSON());
            }
            jsonObject.put("orderbies", orderbies);

            JSONObject params = new JSONObject();
            for(String key : this.getParams().keySet()){
                params.put(key,this.getParams().get(key));
            }
            jsonObject.put("params", params);

            jsonObject.put("filter", filter.toJSON());

            jsonObject.put("process", getProcess());

            jsonObject.put("limit", getLimit());
            jsonObject.put("start", getStart());

            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
