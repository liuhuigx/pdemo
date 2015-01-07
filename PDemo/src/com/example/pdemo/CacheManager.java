
package com.example.pdemo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import com.example.pdemo.bean.ItemObject;
import com.example.pdemo.bean.ProjectObject;
import com.example.pdemo.util.Utils;

import android.content.Context;
import android.widget.Toast;

public class CacheManager {

    private static class ContentsHolder {
        static HashMap<Long, ProjectObject> sContents;
    }

    private Context mContext;
    private static long sCurrentProjectId = -1;
    private ArrayList<OnCacheChangeListener> mOnCacheListeners;

    public void regOnCacheListener(OnCacheChangeListener l) {
        if (mOnCacheListeners == null) {
            mOnCacheListeners = new ArrayList<OnCacheChangeListener>();
        }
        mOnCacheListeners.add(l);
    }

    public void unregOnCacheListener(OnCacheChangeListener l) {
        if (mOnCacheListeners == null) {
            mOnCacheListeners = new ArrayList<OnCacheChangeListener>();
        }
        mOnCacheListeners.remove(l);
    }

    public interface OnCacheChangeListener {
        public void onCacheChange();
    }

    public boolean isAllEmpty() {
        return getAllProjects().isEmpty();
    }

    public HashMap<Long, ProjectObject> getAllProjects() {
        if (null == ContentsHolder.sContents) {
            ContentsHolder.sContents = new HashMap<Long, ProjectObject>();
        }
        return ContentsHolder.sContents;
    }

    public void setAllContens(HashMap<Long, ProjectObject> hm) {
        if (null == ContentsHolder.sContents) {
            ContentsHolder.sContents = new HashMap<Long, ProjectObject>();
        } else {
            ContentsHolder.sContents.clear();
        }

        if (null != hm && hm.size() > 0) {
            ContentsHolder.sContents.putAll(hm);
        }
    }

    public CacheManager(Context c) {
        mContext = c;

        mOnCacheListeners = new ArrayList<OnCacheChangeListener>();

        setAllContens(Utils.readSPContent(c));
        setCurrentProjectId(Utils.readAcitiveProjectFromSP(mContext));
    }

    public void clear() {
        this.setAllContens(null);
        this.mOnCacheListeners.clear();
        this.resetCurrentProject();
    }

    public boolean isCurrentProjectAvaible() {
        return -1 != sCurrentProjectId;
    }

    public void resetCurrentProject() {
        sCurrentProjectId = -1;
    }

    public long getCurrentProjectId() {
        return sCurrentProjectId;
    }

    public void setCurrentProjectId(long currentProjectId) {
        sCurrentProjectId = currentProjectId;
        Utils.writeActiveProjectToSP(mContext, sCurrentProjectId);

        updateUI();
    }

    public ProjectObject getCurrentProject() {
        if (-1 == sCurrentProjectId)
            return null;
        return getAllProjects().get(getCurrentProjectId());
    }

    public ProjectObject getProjectById(long pid) {
        if (pid < 0) {
            return null;
        }

        return getAllProjects().get(pid);
    }

    public void check(long pid) {
        ProjectObject spo = this.getProjectById(pid);
        if (null != spo) {
            ItemObject checking = new ItemObject(spo.getId());
            spo.addChecking(checking);

            updateUI();
        } else {
            Toast.makeText(mContext, "spo is null...", Toast.LENGTH_LONG).show();
        }
    }

    public boolean addProject(String name, String desc) {

        if (getAllProjects().size() > 5) {
            return false;
        }

        ProjectObject po = new ProjectObject(name, desc);
        getAllProjects().put(po.getId(), po);
        setCurrentProjectId(po.getId());
        updateUI();

        return true;
    }

    public boolean checkMaxProject() {
        return !(getAllProjects().size() >= 5);
    }

    private void updateUI() {
        // Notify UI
        if (null != mOnCacheListeners && mOnCacheListeners.size() > 0) {
            for (OnCacheChangeListener l : mOnCacheListeners) {
                l.onCacheChange();
            }
        }
    }

    public void updateSP() {
        // Save to SP.
        long start = System.currentTimeMillis();
        HashSet<String> hs = new HashSet<String>();
        Iterator<ProjectObject> ite = getAllProjects().values().iterator();
        while (ite.hasNext()) {
            String js = ite.next().toJsonString();
            if (null != js) {
                hs.add(js);
            }
        }
        Utils.setSPcontent(mContext, hs);
        Utils.log("updateSP cost : " + (System.currentTimeMillis() - start) + "ms.");
    }

    // private void updateUIAndSP() {
    // updateUI();
    // updateSP();
    // }
}
