package com.nitgen.SDK.AndroidBSP;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.text.InputFilter;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends BaseActivity implements NBioBSPJNI.CAPTURE_CALLBACK, SampleDialogFragment.SampleDialogListener, UserDialog.UserDialogListener {

    Button bt_Add , bt_Delete, bt_Close , bt_Report , bt_Aboutus , bt_countUsers, bt_hash;
    private static final String PASSWORD_ADMIN = "password";
    ImageView iv_VerifyFinger;
    private Handler  StopCodeForTwoSeconds = new Handler();

    private DialogFragment          sampleDialogFragment;
    private UserDialog              userDialog;
    private NBioBSPJNI				bsp;
    private NBioBSPJNI.Export       exportEngine;
    private NBioBSPJNI.IndexSearch  indexSearch;

    private byte[]					byTemplate1;

    //DB Fingures
    private byte[]					byTemplateFingure1_DB;
    private byte[]					byTemplateFingure2_DB;

    private byte[]					byCapturedRaw1;
    private int						nCapturedRawWidth1;
    private int						nCapturedRawHeight1;
    private byte[]					byCapturedRaw2;
    private int						nCapturedRawWidth2;
    private int						nCapturedRawHeight2;
    String   Base64_templateVerify ;

    private boolean					bCapturedFirst, bAutoOn = false;
    public static final int QUALITY_LIMIT = 80;
    public Boolean flag_db_TableOneSearch = false;
    public Boolean flag_db_TableTwoSearch = false;

    private TextView tv_Name , tv_Aadhaar ,tv_Punch_time;
    private LinearLayout ll_ColorChange;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt_Add = (Button)findViewById(R.id.add);
        bt_Delete = (Button)findViewById(R.id.delete);
        bt_Close = (Button)findViewById(R.id.closemain);
        bt_Report = (Button)findViewById(R.id.report);
        bt_Aboutus = (Button)findViewById(R.id.aboutus);
        bt_countUsers = (Button)findViewById(R.id.countusers);
        iv_VerifyFinger = (ImageView)findViewById(R.id.imagefingerverify);
        bt_hash =(Button)findViewById(R.id.hash);

        tv_Name = (TextView)findViewById(R.id.tvname);
        tv_Aadhaar = (TextView)findViewById(R.id.tvaadhaarno);
        tv_Punch_time = (TextView)findViewById(R.id.tvpunchtime);

        ll_ColorChange = (LinearLayout)findViewById(R.id.l2_colorchange);


        //getList__();
        initData();
        sampleDialogFragment.show(getFragmentManager(), "DIALOG_TYPE_PROGRESS");
        bsp.OpenDevice();




        bt_Add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent iadd = new Intent(MainActivity.this,AddActivity.class);
                startActivity(iadd);
            }
        });


        //Count Users
        bt_countUsers.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DatabaseHandler DH_Count = new DatabaseHandler(MainActivity.this);
                int USERS = DH_Count.getContactsCount();
                Toast.makeText(MainActivity.this, "Total No.Of Users :-"+Integer.toString(USERS), Toast.LENGTH_LONG).show();
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

        iv_VerifyFinger.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {




                sampleDialogFragment.show(getFragmentManager(), "DIALOG_TYPE_PROGRESS");
                sampleDialogFragment.setCancelable(false);

                new Thread(new Runnable() {

                    public void run() {
                        OnCapture1(10000);
                        if(Base64_templateVerify!=null){
                            //Convert Base64_templateVerify to Byte Array and Verify
                        }
                    }
                }).start();





            }
        });


    }

   /* private void getList__() {
        DatabaseHandler DH = new DatabaseHandler(MainActivity.this);
        listDB = DH.GetAllData();

        if(listDB.size()!=0){
            Toast.makeText(MainActivity.this,"Ready For Loop",Toast.LENGTH_LONG).show();

            for(int i=0; i<listDB.size();i++){
                String Aashaar = listDB.get(i).get(DatabaseHandler.AADHAAR_DB);
                Log.d("Aadhaar",Aashaar);
                String FingerTwo = listDB.get(i).get(DatabaseHandler.FINGER_TWO_DB);
                Log.d("FingerTwo",FingerTwo);
                String FingureOne = listDB.get(i).get(DatabaseHandler.FINGER_ONE_DB);
                Log.d("Fingure One",FingureOne);
                String Name = listDB.get(i).get(DatabaseHandler.NAME_DB);
                Log.d("Name",Name);
                String CareOFf = listDB.get(i).get(DatabaseHandler.CAREOFF_DB);
                Log.d("CareOFf",CareOFf);
                String DOB = listDB.get(i).get(DatabaseHandler.DOB_DB);
                Log.d("DOB",DOB);
            }

        }else{
            Toast.makeText(MainActivity.this,"List is Empty",Toast.LENGTH_LONG).show();
        }
    }*/

    /**
     * Captured One Button (Fingure)
     */
    String msg = "";
    public synchronized void OnCapture1(int timeout){

        NBioBSPJNI.FIR_HANDLE hCapturedFIR, hAuditFIR;
        NBioBSPJNI.CAPTURED_DATA capturedData;

        hCapturedFIR = bsp.new FIR_HANDLE();
        hAuditFIR = bsp.new FIR_HANDLE();
        capturedData = bsp.new CAPTURED_DATA();

        bCapturedFirst = true;

        bsp.Capture(NBioBSPJNI.FIR_PURPOSE.ENROLL, hCapturedFIR, timeout, hAuditFIR, capturedData, MainActivity.this, 0, null);

        if(sampleDialogFragment!=null && "DIALOG_TYPE_PROGRESS".equals(sampleDialogFragment.getTag()))
            sampleDialogFragment.dismiss();

        if (bsp.IsErrorOccured())  {
            msg = "NBioBSP Capture Error: " + bsp.GetErrorCode();
        }
        else  {
            NBioBSPJNI.INPUT_FIR inputFIR;

            inputFIR = bsp.new INPUT_FIR();

            // Make ISO 19794-2 data
            {
                NBioBSPJNI.Export.DATA exportData;

                inputFIR.SetFIRHandle(hCapturedFIR);

                exportData = exportEngine.new DATA();

                exportEngine.ExportFIR(inputFIR, exportData, NBioBSPJNI.EXPORT_MINCONV_TYPE.OLD_FDA);

                if (bsp.IsErrorOccured())  {
                    runOnUiThread(new Runnable() {

                        public void run() {
                            msg = "NBioBSP ExportFIR Error: " + bsp.GetErrorCode();
                            //tv_Status.setText(msg);
                            //Toast.makeText(AddActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    });
                    return ;
                }

                if (byTemplate1 != null)
                    byTemplate1 = null;

                byTemplate1 = new byte[exportData.FingerData[0].Template[0].Data.length];
                byTemplate1 = exportData.FingerData[0].Template[0].Data;

                Base64_templateVerify  = Base64.encodeToString(byTemplate1, Base64.DEFAULT);

                //Verification Goes Here
                //Base64_templateVerify is the fingure that is placed on the machine
                 //Start ASYNC TASK
                if(Base64_templateVerify!=null) {
                    VerifyFinger VF = new VerifyFinger();
                    VF.execute(Base64_templateVerify);
                }else{
                    msg="String Empty";
                }

            }

            // Make Raw Image data
            {
                NBioBSPJNI.Export.AUDIT exportAudit;

                inputFIR.SetFIRHandle(hAuditFIR);

                exportAudit = exportEngine.new AUDIT();

                exportEngine.ExportAudit(inputFIR, exportAudit);

                if (bsp.IsErrorOccured())  {

                    runOnUiThread(new Runnable() {

                        public void run() {
                            msg = "NBioBSP ExportAudit Error: " + bsp.GetErrorCode();
                            //tv_Status.setText(msg);
                            // Toast.makeText(AddActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    });

                    return ;
                }

                if (byCapturedRaw1 != null)
                    byCapturedRaw1 = null;

                byCapturedRaw1 = new byte[exportAudit.FingerData[0].Template[0].Data.length];
                byCapturedRaw1 = exportAudit.FingerData[0].Template[0].Data;

                nCapturedRawWidth1 = exportAudit.ImageWidth;
                nCapturedRawHeight1 = exportAudit.ImageHeight;

                msg = "First Capture Success";

            }

        }

        runOnUiThread(new Runnable() {

            public void run() {
               // tv_Status.setText(msg);

                if (byTemplate1 != null && byTemplate1 != null)  {
                   // save.setEnabled(true);
                }else{
                    // btnVerifyTemplate.setEnabled(false);
                }

                /*if (byCapturedRaw1 != null && byCapturedRaw2 != null)  {
                    btnVerifyRaw.setEnabled(true);
                }else{
                    btnVerifyRaw.setEnabled(false);
                }*/

            }
        });

    }

    private void initData() {
        NBioBSPJNI.CURRENT_PRODUCT_ID = 0;
        if(bsp==null){
            bsp = new NBioBSPJNI("010701-613E5C7F4CC7C4B0-72E340B47E034015", this);
            String msg = null;
            if (bsp.IsErrorOccured())
                msg = "NBioBSP Error: " + bsp.GetErrorCode();
            else  {
                msg = "SDK Version: " + bsp.GetVersion();
                exportEngine = bsp.new Export();
                indexSearch = bsp.new IndexSearch();
            }
           // tv_Status.setText(msg);
        }

        sampleDialogFragment = new SampleDialogFragment();
        userDialog = new UserDialog();
    }

   /* private Boolean Verify_DB(byte[] dbFingure) {


        String msg = "";

        byte [] a = Base64.decode(Base64_template1, Base64.DEFAULT);


        byTemplate1 = a;
        byTemplate2 = dbFingure;

        if (byTemplate1 != null && byTemplate2 != null)  {
            NBioBSPJNI.FIR_HANDLE hLoadFIR1, hLoadFIR2;

            {
                hLoadFIR1 = bsp.new FIR_HANDLE();

                exportEngine.ImportFIR(byTemplate1, byTemplate1.length, NBioBSPJNI.EXPORT_MINCONV_TYPE.OLD_FDA, hLoadFIR1);

                if (bsp.IsErrorOccured())  {
                    msg = "Template NBioBSP ImportFIR Error: " + bsp.GetErrorCode();
                    //tvInfo.setText(msg);
                    return false ;
                }
            }

            {
                hLoadFIR2 = bsp.new FIR_HANDLE();

                exportEngine.ImportFIR(byTemplate2, byTemplate2.length, NBioBSPJNI.EXPORT_MINCONV_TYPE.OLD_FDA, hLoadFIR2);

                if (bsp.IsErrorOccured())  {
                    hLoadFIR1.dispose();
                    msg = "Template NBioBSP ImportFIR Error: " + bsp.GetErrorCode();
                    //tvInfo.setText(msg);
                    return false ;
                }
            }

            // Verify Match
            NBioBSPJNI.INPUT_FIR inputFIR1, inputFIR2;
            Boolean bResult = new Boolean(false);

            inputFIR1 = bsp.new INPUT_FIR();
            inputFIR2 = bsp.new INPUT_FIR();

            inputFIR1.SetFIRHandle(hLoadFIR1);
            inputFIR2.SetFIRHandle(hLoadFIR2);

            bsp.VerifyMatch(inputFIR1, inputFIR2, bResult, null);

            if (bsp.IsErrorOccured())  {
                msg = "Template NBioBSP Verify Match Error: " + bsp.GetErrorCode();
            }else  {
                if (bResult){
                    msg = "Template VerifyMatch Successed";

                }else{
                    msg = "Template VerifyMatch Failed";
                    hLoadFIR1.dispose();
                    hLoadFIR2.dispose();
                    return false;
                }
            }

            hLoadFIR1.dispose();
            hLoadFIR2.dispose();
        }else{
            msg = "Can not find captured data";
        }

        //tvInfo.setText(msg);
        return true;
    }*/


    public int OnCaptured(NBioBSPJNI.CAPTURED_DATA captured_data) {
        //  tvDevice.setText("IMAGE Quality: "+capturedData.getImageQuality());

        if( captured_data.getImage()!=null){
            if (bCapturedFirst){
                iv_VerifyFinger.setImageBitmap( captured_data.getImage());
            }
        }

        // quality : 40~100
        if(captured_data.getImageQuality()>=QUALITY_LIMIT){
            if(sampleDialogFragment!=null && "DIALOG_TYPE_PROGRESS".equals(sampleDialogFragment.getTag()))
                sampleDialogFragment.dismiss();
            return NBioBSPJNI.ERROR.NBioAPIERROR_USER_CANCEL;
        }else if(captured_data.getDeviceError()!=NBioBSPJNI.ERROR.NBioAPIERROR_NONE){
            if(sampleDialogFragment!=null && "DIALOG_TYPE_PROGRESS".equals(sampleDialogFragment.getTag()))
                sampleDialogFragment.dismiss();
            return captured_data.getDeviceError();
        }else{
            return NBioBSPJNI.ERROR.NBioAPIERROR_NONE;
        }
    }

    public void OnConnected() {
        if(sampleDialogFragment!=null)
            sampleDialogFragment.dismiss();

        String message = "Device Open Success";
        //tv_Status.setText(message);

    }

    public void OnDisConnected() {
        NBioBSPJNI.CURRENT_PRODUCT_ID = 0;

        if(sampleDialogFragment!=null)
            sampleDialogFragment.dismiss();

        String message = "NBioBSP Disconnected: " + bsp.GetErrorCode();
        //tv_Status.setText(message);

    }

    public void onClickStopBtn(DialogFragment dialogFragment) {
        bAutoOn = false;
        sampleDialogFragment.dismiss();

    }

    public void onClickPositiveBtn(DialogFragment dialogFragment, String id) {
        if("add_fir".equals(dialogFragment.getTag())){
            //  OnAddFIR(5000, id);
        }else if("remove".equals(dialogFragment.getTag())){
            //  OnRemoveUser(id);
        }

    }

    private Boolean Verify_DB_TableONE(byte[] dbFingureOne) {


        String msg = "";

        byte [] VerifyFingure = Base64.decode(Base64_templateVerify,Base64.DEFAULT);


        byTemplate1 = VerifyFingure;
        byTemplateFingure1_DB  = dbFingureOne;


        if (byTemplate1 != null && byTemplateFingure1_DB != null )  {
            NBioBSPJNI.FIR_HANDLE hLoadFIR1, hLoadFIR2 ;

            {
                hLoadFIR1 = bsp.new FIR_HANDLE();

                exportEngine.ImportFIR(byTemplate1, byTemplate1.length, NBioBSPJNI.EXPORT_MINCONV_TYPE.OLD_FDA, hLoadFIR1);

                if (bsp.IsErrorOccured())  {
                    msg = "Template NBioBSP ImportFIR Error: " + bsp.GetErrorCode();
                    //tvInfo.setText(msg);
                    return false ;
                }
            }

            {
                hLoadFIR2 = bsp.new FIR_HANDLE();

                exportEngine.ImportFIR(byTemplateFingure1_DB, byTemplateFingure1_DB.length, NBioBSPJNI.EXPORT_MINCONV_TYPE.OLD_FDA, hLoadFIR2);

                if (bsp.IsErrorOccured())  {
                    hLoadFIR1.dispose();
                    msg = "Template NBioBSP ImportFIR Error: " + bsp.GetErrorCode();
                    //tvInfo.setText(msg);
                    return false ;
                }
            }



            // Verify Match
            NBioBSPJNI.INPUT_FIR inputFIR1, inputFIR2 ;
            Boolean bResult = new Boolean(false);

            inputFIR1 = bsp.new INPUT_FIR();
            inputFIR2 = bsp.new INPUT_FIR();

            inputFIR1.SetFIRHandle(hLoadFIR1);
            inputFIR2.SetFIRHandle(hLoadFIR2);

            bsp.VerifyMatch(inputFIR1, inputFIR2, bResult, null);

            if (bsp.IsErrorOccured())  {
                msg = "Template NBioBSP Verify Match Error: " + bsp.GetErrorCode();
            }else  {
                if (bResult){
                    msg = "Template VerifyMatch Successed";

                }else{
                    msg = "Template VerifyMatch Failed";
                    hLoadFIR1.dispose();
                    hLoadFIR2.dispose();
                    return false;
                }
            }

            hLoadFIR1.dispose();
            hLoadFIR2.dispose();
        }else{
            msg = "Can not find captured data";
            return false;
        }

        //tvInfo.setText(msg);
        return true;
    }

    //Verify Table Two
    private Boolean Verify_DB_TableTWO(byte[] dbFingureTwo) {


        String msg = "";

        byte [] VerifyFingure = Base64.decode(Base64_templateVerify,Base64.DEFAULT);


        byTemplate1 = VerifyFingure;
        byTemplateFingure2_DB  = dbFingureTwo;


        if (byTemplate1 != null && byTemplateFingure2_DB != null )  {
            NBioBSPJNI.FIR_HANDLE hLoadFIR1, hLoadFIR2 ;

            {
                hLoadFIR1 = bsp.new FIR_HANDLE();

                exportEngine.ImportFIR(byTemplate1, byTemplate1.length, NBioBSPJNI.EXPORT_MINCONV_TYPE.OLD_FDA, hLoadFIR1);

                if (bsp.IsErrorOccured())  {
                    msg = "Template NBioBSP ImportFIR Error: " + bsp.GetErrorCode();
                    //tvInfo.setText(msg);
                    return false ;
                }
            }

            {
                hLoadFIR2 = bsp.new FIR_HANDLE();

                exportEngine.ImportFIR(byTemplateFingure2_DB, byTemplateFingure2_DB.length, NBioBSPJNI.EXPORT_MINCONV_TYPE.OLD_FDA, hLoadFIR2);

                if (bsp.IsErrorOccured())  {
                    hLoadFIR1.dispose();
                    msg = "Template NBioBSP ImportFIR Error: " + bsp.GetErrorCode();
                    //tvInfo.setText(msg);
                    return false ;
                }
            }



            // Verify Match
            NBioBSPJNI.INPUT_FIR inputFIR1, inputFIR2 ;
            Boolean bResult = new Boolean(false);

            inputFIR1 = bsp.new INPUT_FIR();
            inputFIR2 = bsp.new INPUT_FIR();

            inputFIR1.SetFIRHandle(hLoadFIR1);
            inputFIR2.SetFIRHandle(hLoadFIR2);

            bsp.VerifyMatch(inputFIR1, inputFIR2, bResult, null);

            if (bsp.IsErrorOccured())  {
                msg = "Template NBioBSP Verify Match Error: " + bsp.GetErrorCode();
            }else  {
                if (bResult){
                    msg = "Template VerifyMatch Successed";

                }else{
                    msg = "Template VerifyMatch Failed";
                    hLoadFIR1.dispose();
                    hLoadFIR2.dispose();
                    return false;
                }
            }

            hLoadFIR1.dispose();
            hLoadFIR2.dispose();
        }else{
            msg = "Can not find captured data";
            return false;
        }

        //tvInfo.setText(msg);
        return true;
    }

    public class  VerifyFinger extends AsyncTask<String,String,String>{
        ArrayList<HashMap<String,String>> listDB = new ArrayList<HashMap<String,String>>();
        DatabaseHandler DH_VERIFY_DB = new DatabaseHandler(getApplicationContext());
        public String message= null;

        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* dialog = new ProgressDialog(MainActivity.this);
            this.dialog.setMessage(EConstants.ProgressDialog_Message);
            this.dialog.show();
            this.dialog.setCancelable(false);*/

        }

        @Override
        protected String doInBackground(String... params) {

            String PlacedFingure = params[0];
            listDB = DH_VERIFY_DB.GetAllData();


            if(listDB.size()!= 0){

            for (int i=0; i<listDB.size();i++){

                //getting the Fingures From the DB
                String DB_Base64_Fingure_ONE = listDB.get(i).get(DatabaseHandler.FINGER_ONE_DB);
                String DB_Base64_Fingure_TWO = listDB.get(i).get(DatabaseHandler.FINGER_TWO_DB);


                //Convert it to Byte Array
                byte [] DBFingureONE = Base64.decode(DB_Base64_Fingure_ONE,Base64.DEFAULT);
                byte [] DBFingureTWO = Base64.decode(DB_Base64_Fingure_TWO,Base64.DEFAULT);

                //Verify Goes Here

                flag_db_TableOneSearch = Verify_DB_TableONE(DBFingureONE);
                flag_db_TableTwoSearch = Verify_DB_TableTWO(DBFingureTWO);

                if(flag_db_TableTwoSearch == true || flag_db_TableOneSearch == true ){
                    String name = listDB.get(i).get(DatabaseHandler.NAME_DB);
                    String Aadhaar = listDB.get(i).get(DatabaseHandler.AADHAAR_DB);
                    StringBuilder SB = new StringBuilder();
                    SB.append("Matched:");SB.append(name);SB.append(":");SB.append(Aadhaar);

                    message = SB.toString();
                    break;

                }else{
                    continue;
                }

            }
            }else{
                message = "There is No Data in the Database.Please Add the User";
            }
            if(message==null){
                message="User Not Registered";
            }
          return message;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);




            if(s.length()<=20){

                update_UI_AfterAttendence_Faliure(s);

            }else{
              //  Toast.makeText(MainActivity.this,s,Toast.LENGTH_LONG).show();
                //Break the String using Delemeter $
                String[] str_array = s.split(":");
                String message = str_array[0];
                String name = str_array[1];
                String aadhaar = str_array[2];
              //  Toast.makeText(MainActivity.this,aadhaar ,Toast.LENGTH_LONG).show();

                update_UI_AfterAttendence(name,aadhaar,message);
            }








        }
    }

    private void update_UI_AfterAttendence_Faliure(String s) {
        ll_ColorChange.setBackgroundColor(Color.RED);
        tv_Punch_time.setText(s);
        StopCodeForTwoSeconds.postDelayed(new Runnable() {
            public void run() {
                doStufftwo();
            }
        }, 3000);

    }

    private void update_UI_AfterAttendence(String name, String aadhaar , String message) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String currentDateandTime = null;

        currentDateandTime = sdf.format(new Date());

        ll_ColorChange.setBackgroundColor(Color.GREEN);
        tv_Name.setText(name);
        tv_Aadhaar.setText(aadhaar);
        tv_Punch_time.setText(currentDateandTime);

        //Saving Algorithm Goes Here


        StopCodeForTwoSeconds.postDelayed(new Runnable() {
            public void run() {
                doStuff_Save();
            }
        }, 3000);
    }

    private void doStuff_Save() {


        ll_ColorChange.setBackgroundColor(Color.WHITE);
        tv_Name.setText("");
        tv_Aadhaar.setText("");
        tv_Punch_time.setText("");
        iv_VerifyFinger.setImageBitmap(null);

    }

    private void doStufftwo() {
        ll_ColorChange.setBackgroundColor(Color.WHITE);
        tv_Name.setText("");
        tv_Aadhaar.setText("");
        tv_Punch_time.setText("");
        iv_VerifyFinger.setImageBitmap(null);

    }
}




