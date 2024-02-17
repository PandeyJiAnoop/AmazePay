package com.akp.amazepay.entity;
/**
 * Created by Anoop Pandey on 9696381023.
 */
public class Getter {
    private String name;
    private String mobile;
    private String date;
    private String id;
    private String data;
    public Getter(String name, String mobile, String date,String id) {
        this.name = name;
        this.mobile = mobile;
        this.date = date;
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public String getMobile() {
        return mobile;
    }
    public String getDate() {
        return date;
    }
    public String getId() {
        return id;
    }

}
