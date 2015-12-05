package com.nitgen.SDK.AndroidBSP;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class AddActivity extends Activity {

    Button bt_Close , bt_Verify , bt_DetectDevice;
    EditText et_Aadhaar;
    TextView tv_Name, tv_DOB , tv_CO;
    List<UserPojo> userlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        bt_Close = (Button)findViewById(R.id.close);
        bt_Verify = (Button)findViewById(R.id.verifyaadhaar);
        bt_DetectDevice = (Button)findViewById(R.id.detectdeviceadd);

        et_Aadhaar = (EditText)findViewById(R.id.et_aadhaar);

        tv_Name = (TextView)findViewById(R.id.tvname);
        tv_DOB = (TextView)findViewById(R.id.tvdob);
        tv_CO = (TextView)findViewById(R.id.tvco);

        bt_Close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AddActivity.this.finish();
            }
        });

        bt_Verify.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //Check Weather the Aadhaar is entered or not
                String VerifyAadhaar = et_Aadhaar.getText().toString().trim();
                if (VerifyAadhaar.length() != 0) {
                    if (VerifyAadhaar.length() == 12) {

                        if (isOnline()) {
                            //Start Async Task
                            String Service_Aadhaar = VerifyAadhaar;
                            VerifyUser VU = new VerifyUser();
                            VU.execute(Service_Aadhaar);
                        } else {
                            Toast.makeText(getApplicationContext(), "Network isn't available", Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Please enter a valid Aadhaar Number", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Please enter your Aadhaar Number", Toast.LENGTH_LONG).show();
                }


            }
        });

    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Update Display
     */
    protected void updateDisplay() {

        /*UserAdapter adapter = new UserAdapter(this, R.layout.item_flower, userlist);
        listv.setAdapter(adapter);*/
        tv_Name.setText(userlist.get(0).getResident_Name_user());
        tv_DOB.setText(userlist.get(0).getDOB_user());
        tv_CO.setText(userlist.get(0).getCare_OFF_User());


    }


    /**
     *
     */
    class VerifyUser extends AsyncTask<String,String,String>{

        private ProgressDialog dialog;
        String SendAadhaar = null;
        String url = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(AddActivity.this);
            this.dialog.setMessage(EConstants.ProgressDialog_Message);
            this.dialog.show();
            this.dialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
           SendAadhaar = params[0];
            StringBuilder sb_search = new StringBuilder();
            sb_search.append(EConstants.URLAAdhaarVerify);sb_search.append(EConstants.url_Delemetre);
            sb_search.append(EConstants.methord_SearchAadaar);sb_search.append(EConstants.url_Delemetre);
            sb_search.append( SendAadhaar );
            url = sb_search.toString();
            Log.d("URL",url);
            String content = HttpManager.getData(url);
            return content;
        }

        @Override
        protected void onPostExecute(String content) {
            super.onPostExecute(content);
            this.dialog.dismiss();
           Log.d("Content", content);

            try {
                userlist = UserJson_UID.parseFeed(content);
                if (userlist.isEmpty()) {
                    Toast.makeText(getApplicationContext(), EConstants.ListEmpty, Toast.LENGTH_LONG).show();
                }
                if (userlist.size() > 1) {
                    Toast.makeText(getApplicationContext(), EConstants.MultipleUser, Toast.LENGTH_LONG).show();
                }
                if (userlist.size() == 1) {
                    updateDisplay();
                } else {
                    Toast.makeText(getApplicationContext(), EConstants.ErrorMessageUnknow, Toast.LENGTH_LONG).show();
                }
            }catch (Exception e){
                Toast.makeText(getApplicationContext(), e.getLocalizedMessage().toString() , Toast.LENGTH_LONG).show();
            }

        }
    }

}
