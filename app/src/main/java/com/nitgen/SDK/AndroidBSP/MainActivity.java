package com.nitgen.SDK.AndroidBSP;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.text.InputFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends Activity {

    Button bt_Add , bt_Delete, bt_Close , bt_Report , bt_Aboutus;
    private static final String PASSWORD_ADMIN = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt_Add = (Button)findViewById(R.id.add);
        bt_Delete = (Button)findViewById(R.id.delete);
        bt_Close = (Button)findViewById(R.id.closemain);
        bt_Report = (Button)findViewById(R.id.report);
        bt_Aboutus = (Button)findViewById(R.id.aboutus);

        bt_Add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent iadd = new Intent(MainActivity.this,AddActivity.class);
                startActivity(iadd);
            }
        });


        /**
         * Reporting
         */
        bt_Report.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent ireport = new Intent(MainActivity.this,ReportActivity.class);
                startActivity(ireport);
            }
        });


        /**
         * About US
         */

        bt_Aboutus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent iaboutus = new Intent(MainActivity.this,AboutUsActivity.class);
                startActivity(iaboutus);
            }
        });




        /**
         * Delete Functionality
         */

        bt_Delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("Delete");
                alertDialog.setMessage("Enter Aadhaar Number");

                final EditText input = new EditText(MainActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                int maxLength = 12;
                InputFilter[] fArray = new InputFilter[1];
                fArray[0] = new InputFilter.LengthFilter(maxLength);
                input.setFilters(fArray);
                alertDialog.setView(input); // uncomment this line

                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // password = input.getText().toString();
                        // if (password.compareTo("") == 0) {
                        //     if (pass.equals(password)) {
                        //         Toast.makeText(getApplicationContext(),"Password Matched", Toast.LENGTH_SHORT).show();
                        //      Intent myIntent1 = new Intent(view.getContext(), Show.class);
                        //     startActivityForResult(myIntent1, 0);
                        //  } else {
                        //      Toast.makeText(getApplicationContext(),"Wrong Password!", Toast.LENGTH_SHORT).show();
                        //  }
                        // }
                    }
                });

                alertDialog.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                alertDialog.show();
            }

        });


        /**
         * Close the Application
         */
        bt_Close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("Close");
                alertDialog.setMessage("Enter Password");

                final EditText input = new EditText(MainActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input); // uncomment this line

                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String password = input.getText().toString();
                        if (password.length() != 0) {
                            if (PASSWORD_ADMIN.equals(password)) {
                                Toast.makeText(getApplicationContext(), "Password Matched", Toast.LENGTH_SHORT).show();
                                android.os.Process.killProcess(android.os.Process.myPid());
                            } else {
                                Toast.makeText(getApplicationContext(), "Wrong Password!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Password cannot be null", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                alertDialog.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                alertDialog.show();


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




