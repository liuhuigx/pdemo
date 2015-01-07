
package com.example.pdemo.view;

import com.example.pdemo.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class PCardView extends LinearLayout {

    private long projectID;

    public PCardView(Context context, long projectID) {
        super(context);
        setProjectID(projectID);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.pcardview, this);
    }

    public long getProjectID() {
        return projectID;
    }

    public void setProjectID(long projectID) {
        this.projectID = projectID;
    }

}
