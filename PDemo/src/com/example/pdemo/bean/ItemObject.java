
package com.example.pdemo.bean;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.pdemo.util.Utils;

public class ItemObject {
    public final static String KEY_ID = "ID";
    public final static String KEY_PROJECT_ID = "PROJECT_ID";
    public final static String KEY_DESC = "DESC";

    public long id;
    public long projectID;
    public String desc;

    private ItemObject() {
        this.id = System.currentTimeMillis();
    }

    public ItemObject(long pid) {
        this.id = System.currentTimeMillis();
        projectID = pid;
    }

    public String toJsonString() {
        JSONObject jo = new JSONObject();
        try {
            jo.put(KEY_ID, id);
            jo.put(KEY_PROJECT_ID, projectID);
            jo.put(KEY_DESC, desc);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jo.toString();
    }

    public static ItemObject fromJsonString(String s) {
        ItemObject io = new ItemObject();
        Utils.log("Item object fromString : " + s);

        JSONObject jo = null;
        try {
            jo = new JSONObject(s);
            io.id = jo.getLong(KEY_ID);
            io.projectID = jo.getLong(KEY_PROJECT_ID);
            if (!jo.isNull(KEY_DESC)) {
                io.desc = jo.getString(KEY_DESC);
            }
        } catch (JSONException e) {
            io = null;
            e.printStackTrace();
        }

        return io;
    }
}
