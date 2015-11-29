package com.manbuit.android.fragment.dataRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by MB on 2015/11/26.
 */
public class OrderBy {
    private String exp;
    private String name;
    private boolean asc;

    public OrderBy(String exp, String name, boolean asc) {
        this.setExp(exp);
        this.setName(name);
        this.setAsc(asc);
    }

    public OrderBy(String exp, boolean asc) {
        this(exp,null,asc);
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAsc() {
        return asc;
    }

    public void setAsc(boolean asc) {
        this.asc = asc;
    }

    public JSONObject toJSON(){

        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("name", getName());
            jsonObject.put("exp", getExp());
            jsonObject.put("asc", isAsc());

            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
