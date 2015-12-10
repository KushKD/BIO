package com.DIT.HP.AadhaarAttendance;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.DIT.HP.Aadhaar.R;

public class AboutUsActivity extends Activity {

    Button bt_Close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        bt_Close = (Button)findViewById(R.id.closeaboutus);

        bt_Close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AboutUsActivity.this.finish();
            }
        });

    }

    //Handling the Other Keys Hardware
    @Override

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_HOME)) {
            System.out.println("KEYCODE_HOME");
            return true;
        }
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            System.out.println("KEYCODE_BACK");
            return true;
        }
        if ((keyCode == KeyEvent.KEYCODE_MENU)) {
            System.out.println("KEYCODE_MENU");
            return true;
        }
        if ((keyCode == KeyEvent.KEYCODE_CAMERA)) {
            System.out.println("KEYCODE_CAMERA");
            return true;
        }
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)) {
            System.out.println("KEYCODE_VOLUME_DOWN");
            return true;
        }
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP)) {
            System.out.println("KEYCODE_VOLUME_UP");
            return true;
        }
        return false;
    }
}
