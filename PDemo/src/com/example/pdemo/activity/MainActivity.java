
package com.example.pdemo.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pdemo.CacheManager;
import com.example.pdemo.DemoApp;
import com.example.pdemo.R;
import com.example.pdemo.bean.ItemObject;
import com.example.pdemo.bean.ProjectObject;
import com.example.pdemo.util.Utils;
import com.example.pdemo.view.PCardView;

public class MainActivity extends Activity {

    private LinearLayout mMainView;

    private TextView tvInfoView;
    private Button btAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        mMainView = (LinearLayout) findViewById(R.id.main_view);
        tvInfoView = (TextView) findViewById(R.id.main_tv_infoview);

        DemoApp.mcm.regOnCacheListener(new CacheManager.OnCacheChangeListener() {

            @Override
            public void onCacheChange() {
                Utils.log("MainActivity onCacheChange...");
                updateUI();
            }
        });

        if (DemoApp.mcm.isAllEmpty()) {
            add();
        }

        btAdd = (Button) findViewById(R.id.main_bt_add);
        btAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                add();
            }
        });

        updateViews(DemoApp.mcm.getAllProjects());
    }

    private void updateViews(HashMap<Long, ProjectObject> hm) {
        long start = System.currentTimeMillis();

        setMainViewVisbile(!DemoApp.mcm.isAllEmpty());

        if (null == hm || hm.size() == 0) {
            return;
        }
        Long[] tempL = new Long[hm.size()];

        Iterator<Long> itt = hm.keySet().iterator();
        int i = 0;
        while (itt.hasNext()) {
            tempL[i] = itt.next();
            i++;
        }

        Arrays.sort(tempL);

        int duration = 300;
        for (long l : tempL) {
            final ProjectObject po = hm.get(l);

            PCardView pcdView = new PCardView(this, po.getId());
            CardView cdView = (CardView) pcdView.findViewById(R.id.card_view);
            Button btCheck = (Button) cdView.findViewById(R.id.main_bt_check);

            pcdView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    listChecks(po.getId());
                }
            });

            btCheck.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    check(po.getId());
                }
            });

            Animation anim = AnimationUtils.loadAnimation(this, R.anim.fade);
            pcdView.setAnimation(anim);
            anim.setDuration(duration += 500);
            mMainView.addView(pcdView);
        }
        Utils.log("updateViews cost: " + (System.currentTimeMillis() - start) + "ms.");
    }

    private void setMainViewVisbile(boolean isVis) {
        if (isVis) {
            mMainView.setVisibility(View.VISIBLE);
            tvInfoView.setVisibility(View.GONE);
        } else {
            mMainView.setVisibility(View.GONE);
            tvInfoView.setVisibility(View.VISIBLE);
        }
    }

    private void updateUI() {

        // NEED CHECK IF VIEW COUNT NOT EQUAL PO'S COUNT.
        ArrayList<Long> hasViewPids = new ArrayList<Long>();
        int cc = mMainView.getChildCount();
        for (int i = 0; i < cc; i++) {
            View v = mMainView.getChildAt(i);
            if (v instanceof PCardView) {
                PCardView pv = (PCardView) v;
                ProjectObject po = DemoApp.mcm.getProjectById(pv.getProjectID());
                if (null == po) {
                    pv.setVisibility(View.GONE);
                    continue;
                } else {
                    pv.setVisibility(View.VISIBLE);
                    hasViewPids.add(po.getId());
                }

                CardView cdView = (CardView) v.findViewById(R.id.card_view);

                TextView tvName = (TextView) cdView.findViewById(R.id.main_tv_name);
                TextView tvDesc = (TextView) cdView.findViewById(R.id.main_tv_desc);
                TextView tvCount = (TextView) cdView.findViewById(R.id.main_tv_count);
                TextView tvLastcheckDate = (TextView) cdView
                        .findViewById(R.id.main_tv_lastcheckdate);

                tvName.setText(po.getName());
                tvDesc.setText(po.getDesc());
                ArrayList<ItemObject> ios = po.getCheckings();
                int count = null == ios ? 0 : ios.size();
                tvCount.setText("Check count: " + count);
                String lcd = getLastCheckDate(po);
                tvLastcheckDate.setText("Last check date:" + lcd);
            }
        }

        HashMap<Long, ProjectObject> unviewsPidMap = new HashMap<Long, ProjectObject>();
        unviewsPidMap.putAll(DemoApp.mcm.getAllProjects());

        Iterator<Long> ite = DemoApp.mcm.getAllProjects().keySet().iterator();
        while (ite.hasNext()) {
            Long pid = ite.next();
            if (hasViewPids.contains(pid)) {
                unviewsPidMap.remove(pid);
            }
        }

        if (unviewsPidMap.size() > 0) {
            updateViews(unviewsPidMap);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utils.log("main activity onResume.");
        updateUI();
    }

    @Override
    protected void onPause() {
        super.onPause();
        DemoApp.mcm.updateSP();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);
    }

    private long getLastCheckDateInMills(ProjectObject po) {
        if (null == po.getCheckings() || po.getCheckings().size() == 0) {
            return -1;
        }
        int lastIndex = po.getCheckings().size() - 1;

        long lastCheckDate = lastIndex >= 0 ? po.getCheckings().get(lastIndex).id : -1;
        return lastCheckDate;
    }

    private void add() {
        if (DemoApp.mcm.checkMaxProject()) {
            Intent inte = new Intent(this, AddProject.class);
            startActivity(inte);
        } else {
            Toast.makeText(this, "You can focus 5 items per day!", Toast.LENGTH_LONG).show();
        }
    }

    private void check(long pid) {
        if (!DemoApp.mcm.isAllEmpty()) {
            // long lastCheckDate = getLastCheckDateInMills();
            // if (lastCheckDate <= 0 || !Utils.isSameday(lastCheckDate)) {
            DemoApp.mcm.check(pid);
            // } else {
            // Toast.makeText(this, "Can check once a day!", Toast.LENGTH_LONG)
            // .show();
            // }
        } else {
            Toast.makeText(this, "You need a target!", Toast.LENGTH_LONG).show();
        }
    }

    private void listChecks(long pid) {
        Intent intent = new Intent(this, ChecksListActivity.class);
        intent.putExtra("pid", pid);
        startActivity(intent);
    }

    private String getLastCheckDate(ProjectObject po) {
        String ret = "";
        if (!DemoApp.mcm.isAllEmpty()) {
            long lastCheckDate = getLastCheckDateInMills(po);
            if (lastCheckDate > -1) {
                ret = DateUtils.formatDateTime(this, lastCheckDate, DateUtils.FORMAT_SHOW_YEAR
                        | DateUtils.FORMAT_SHOW_WEEKDAY | DateUtils.FORMAT_ABBREV_ALL
                        | DateUtils.FORMAT_SHOW_DATE);
            }
        }
        return ret;
    }
}
