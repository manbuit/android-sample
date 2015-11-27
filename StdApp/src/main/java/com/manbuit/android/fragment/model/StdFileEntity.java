package com.manbuit.android.fragment.model;

/**
 * Created by MB on 2015/11/27.
 */
public class StdFileEntity {
    private String id;
    private String name;

    public StdFileEntity(String id, String name){
        this.setId(id);
        this.setName(name);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
