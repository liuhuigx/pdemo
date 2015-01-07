
package com.example.pdemo.activity;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.Window;
import android.widget.ArrayAdapter;

import com.example.pdemo.DemoApp;
import com.example.pdemo.R;
import com.example.pdemo.bean.ItemObject;

public class ChecksListActivity extends ListActivity {

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.checkingslist);

        Intent intent = getIntent();
        long pid = intent.getLongExtra("pid", -1);
        if (pid <= 0) {
            return;
        }
        ArrayList<ItemObject> ioa = DemoApp.mcm.getProjectById(pid).getCheckings();
        if (ioa == null || ioa.size() == 0) {
            return;
        }
        String[] array = new String[ioa.size()];

        int i = 0;
        for (ItemObject io : ioa) {
            array[i] = io.desc + "  " + getDate(io.id);
            i++;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, array);

        // Bind to our new adapter.
        setListAdapter(adapter);
    }

    private String getDate(long l) {
        String ret = "";
        if (l > -1) {
            ret = DateUtils.formatDateTime(this, l,
                    DateUtils.FORMAT_SHOW_TIME
                            | DateUtils.FORMAT_SHOW_DATE
                            | DateUtils.FORMAT_SHOW_WEEKDAY
                            | DateUtils.FORMAT_SHOW_YEAR
                    );
        }
        return ret;
    }
}
