
package com.example.pdemo.activity;

import com.example.pdemo.R;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Drawer extends Activity {
    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer);
        
        this.getActionBar().setTitle("Titleee");
        this.getActionBar().setSubtitle("subTitleee");
        
        
        mPlanetTitles = new String[] {
                "Add", "Doing", "Waive", "About"
        };
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawerlist_item, mPlanetTitles));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new ListView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
                showT(mPlanetTitles[pos] + " is clicked...");
            }
        });
    }

    private void showT(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }
}
