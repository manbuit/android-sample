package com.manbuit.android.fragment.model;

/**
 * Created by MB on 2015/11/26.
 */
public class StdEntity {
    private String id;
    private String code;
    private String name;

    public StdEntity(String id,String code, String name){
        this.setId(id);
        this.setCode(code);
        this.setName(name);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
