package com.nitgen.SDK.AndroidBSP;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
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
import android.widget.Toast;

public class MainActivity extends BaseActivity implements NBioBSPJNI.CAPTURE_CALLBACK, SampleDialogFragment.SampleDialogListener, UserDialog.UserDialogListener {

    Button bt_Add , bt_Delete, bt_Close , bt_Report , bt_Aboutus;
    private static final String PASSWORD_ADMIN = "password";
    ImageView iv_VerifyFinger;

    private DialogFragment          sampleDialogFragment;
    private UserDialog              userDialog;
    private NBioBSPJNI				bsp;
    private NBioBSPJNI.Export       exportEngine;
    private NBioBSPJNI.IndexSearch  indexSearch;

    private byte[]					byTemplate1;
    private byte[]					byCapturedRaw1;
    private int						nCapturedRawWidth1;
    private int						nCapturedRawHeight1;
    private byte[]					byCapturedRaw2;
    private int						nCapturedRawWidth2;
    private int						nCapturedRawHeight2;
    String   Base64_templateVerify ;

    private boolean					bCapturedFirst, bAutoOn = false;
    public static final int QUALITY_LIMIT = 80;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt_Add = (Button)findViewById(R.id.add);
        bt_Delete = (Button)findViewById(R.id.delete);
        bt_Close = (Button)findViewById(R.id.closemain);
        bt_Report = (Button)findViewById(R.id.report);
        bt_Aboutus = (Button)findViewById(R.id.aboutus);
        iv_VerifyFinger = (ImageView)findViewById(R.id.imagefingerverify);

        initData();
        sampleDialogFragment.show(getFragmentManager(), "DIALOG_TYPE_PROGRESS");
        bsp.OpenDevice();



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

        iv_VerifyFinger.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                Toast.makeText(getApplication(),"Clicked Here" , Toast.LENGTH_LONG).show();

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
}




