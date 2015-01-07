
package com.example.pdemo.bean;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.pdemo.util.Utils;

import android.text.TextUtils;

public class ProjectObject {
    public final static String KEY_ID = "ID";
    public final static String KEY_NAME = "NAME";
    public final static String KEY_DESC = "DESC";
    public final static String KEY_CHECKINGS = "CHECKINGS";

    private long id;
    private String name;
    private String desc;

    private ArrayList<ItemObject> checkings;

    private ProjectObject() {
        id = System.currentTimeMillis();
    }

    public ProjectObject(String name, String desc) {
        id = System.currentTimeMillis();
        this.name = name;
        this.desc = desc;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public ArrayList<ItemObject> getCheckings() {
        return checkings;
    }

    public void setCheckings(ArrayList<ItemObject> checkings) {
        this.checkings = checkings;
    }

    public void addChecking(ItemObject checking) {
        if (checkings == null) {
            checkings = new ArrayList<ItemObject>();
        }
        checkings.add(checking);
    }

    public String toJsonString() {
        JSONObject jo = new JSONObject();
        try {
            jo.put(KEY_ID, id);
            jo.put(KEY_NAME, name);
            jo.put(KEY_DESC, desc);

            if (null != checkings) {
                JSONArray jArray = new JSONArray();
                for (ItemObject io : checkings) {
                    jArray.put(io.toJsonString());
                }
                jo.put(KEY_CHECKINGS, jArray);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jo.toString();
    }

    public static ProjectObject fromJsonString(String s) {
        if (null == s || TextUtils.isEmpty(s)) {
            return null;
        }
        Utils.log("Project object fromString : " + s);
        ProjectObject p = new ProjectObject();
        p.checkings = new ArrayList<ItemObject>();

        JSONObject jo = null;
        try {
            jo = new JSONObject(s);

            p.id = jo.getLong(KEY_ID);
            p.name = jo.getString(KEY_NAME);
            p.desc = jo.getString(KEY_DESC);

            if (!jo.isNull(KEY_CHECKINGS)) {
                JSONArray jArray = jo.getJSONArray(KEY_CHECKINGS);
                if (null != jArray) {
                    for (int i = 0; i < jArray.length(); i++) {
                        ItemObject io = ItemObject.fromJsonString(jArray.get(i).toString());
                        p.checkings.add(io);
                    }
                }
            }
        } catch (JSONException e) {
            p = null;
            e.printStackTrace();
        }

        return p;
    }
    // private int type, type content;
}
