package com.nitgen.SDK.AndroidBSP;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add = (Button)findViewById(R.id.add);

        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent iadd = new Intent(MainActivity.this,AddActivity.class);
                startActivity(iadd);
            }
        });
    }



}
