
package com.example.pdemo.activity;

import com.example.pdemo.DemoApp;
import com.example.pdemo.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class AddProject extends Activity {

    private EditText etName;
    private EditText etDesc;
    private Button btAdd;

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.addproj);

        etName = (EditText) findViewById(R.id.addp_tv_name);
        etDesc = (EditText) findViewById(R.id.addp_tv_desc);
        btAdd = (Button) findViewById(R.id.addp_bt_add);

        btAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                String desc = etDesc.getText().toString();

                add(name, desc);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        btAdd.setClickable(DemoApp.mcm.checkMaxProject());
    }

    private void add(String name, String desc) {
        DemoApp.mcm.addProject(name, desc);
        finish();
    }
}
