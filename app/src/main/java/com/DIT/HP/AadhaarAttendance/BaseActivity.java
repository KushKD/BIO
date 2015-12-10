package com.DIT.HP.AadhaarAttendance;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;

/**
 * Created by kuush on 12/7/2015.
 */
public class BaseActivity extends Activity {




    //Handling the Other Keys Hardware
    @Override

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_HOME)) {
            System.out.println("KEYCODE_HOME");
            return true;
        }
        if ((keyCode == KeyEvent.KEYCODE_POWER)) {
            System.out.println("KEYCODE_POWER");
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
        if ((keyCode == KeyEvent.KEYCODE_APP_SWITCH)) {
            System.out.println("KEYCODE_APP_SWITCH");
            return true;
        }


        //Recents
        return false;
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        Log.d("Focus debug", "Focus changed !");

        if(!hasFocus) {
            Log.d("Focus debug", "Lost focus !");

            Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            sendBroadcast(closeDialog);
        }
    }

/*    @Override
    public void onAttachedToWindow() {
        getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);
        super.onAttachedToWindow();
    }*/
}
