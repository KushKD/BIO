package com.nitgen.SDK.AndroidBSP;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.os.DeadObjectException;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class AddActivity extends Activity implements NBioBSPJNI.CAPTURE_CALLBACK, SampleDialogFragment.SampleDialogListener, UserDialog.UserDialogListener {

    private NBioBSPJNI				bsp;
    private NBioBSPJNI.Export       exportEngine;
    private NBioBSPJNI.IndexSearch  indexSearch;
    private Button                  bt_Close , bt_Verify , bt_DetectDevice,save;
    private EditText                et_Aadhaar, et_Name  ,et_CO;
    private DatePicker              et_DOB;
    private TextView                tv_Name, tv_DOB , tv_CO , tv_Status;
    private List<UserPojo>          userlist;

    private byte[]					byTemplate1Fingure;  //Fingure 1
    private byte[]					byTemplate2Fingure;  //Fingure 2

    private byte[]					byCapturedRaw1;
    private int						nCapturedRawWidth1;
    private int						nCapturedRawHeight1;

    private byte[]					byCapturedRaw2;
    private int						nCapturedRawWidth2;
    private int						nCapturedRawHeight2;


    private DialogFragment          sampleDialogFragment;
    private UserDialog              userDialog;
    private boolean					bCapturedFirst, bAutoOn = false;
    private String                  Base64_template1 , Base64_template2;  //Base64 Fingure one and Fingure Two
    private ImageView               iv_ImageFingureOne, iv_ImageFingureTwo;

    public static final int QUALITY_LIMIT = 80;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        save = (Button)findViewById(R.id.save);
        save.setEnabled(false);
        bt_Close = (Button)findViewById(R.id.close);
        bt_Verify = (Button)findViewById(R.id.verifyaadhaar);
        bt_DetectDevice = (Button)findViewById(R.id.detectdeviceadd);
        et_Aadhaar = (EditText)findViewById(R.id.et_aadhaar);
        tv_Name = (TextView)findViewById(R.id.tvname);
        tv_DOB = (TextView)findViewById(R.id.tvdob);
        tv_CO = (TextView)findViewById(R.id.tvco);
        tv_Status = (TextView)findViewById(R.id.tvstatus);
        iv_ImageFingureOne = (ImageView)findViewById(R.id.imagefingureone);
        iv_ImageFingureTwo = (ImageView)findViewById(R.id.imagefinguretwo);
        et_Name = (EditText)findViewById(R.id.etname);
        et_DOB = (DatePicker)findViewById(R.id.etdob);
        et_CO = (EditText)findViewById(R.id.etco);


        initData();
        sampleDialogFragment.show(getFragmentManager(), "DIALOG_TYPE_PROGRESS");
        bsp.OpenDevice();

        bt_Close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AddActivity.this.finish();
            }
        });


        /**
         * ImageViews on Click Listener
         */
        iv_ImageFingureOne.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sampleDialogFragment.show(getFragmentManager(), "DIALOG_TYPE_PROGRESS");
                sampleDialogFragment.setCancelable(false);

                new Thread(new Runnable() {

                    public void run() {

                        OnCapture1(10000);

                    }
                }).start();
            }
        });

        iv_ImageFingureTwo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sampleDialogFragment.show(getFragmentManager(), "DIALOG_TYPE_PROGRESS");
                sampleDialogFragment.setCancelable(false);

                new Thread(new Runnable() {

                    public void run() {

                        OnCapture2(10000);

                    }
                }).start();
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

        /**
         * Save Button
         */
        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //Check Weather the Data is not null
                 //name
                 //DOB
                //Care Off
                //Fingure 1
                //Fingure 2
                //Aadhaar No

            }
        });

    }

    /**
     * INIT DATA
     */
    /**
     * void
     */
    public void initData(){

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
            tv_Status.setText(msg);
        }

        sampleDialogFragment = new SampleDialogFragment();
        userDialog = new UserDialog();

    }


    /**
     * Check Weather The Ineternet is there or not
     * @return
     */
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
     * Update Display2
     */
    protected void updateDisplay2() {

        //Change the TextView To EditText
        /*UserAdapter adapter = new UserAdapter(this, R.layout.item_flower, userlist);
        listv.setAdapter(adapter);*/
        tv_Name.setVisibility(View.GONE);
        tv_DOB.setVisibility(View.GONE);
        tv_CO.setVisibility(View.GONE);

        et_Name.setVisibility(View.VISIBLE);
        et_DOB.setVisibility(View.VISIBLE);
        et_CO.setVisibility(View.VISIBLE);


    }


    /**
     * On Destroy
     */
    @Override
    public void onDestroy(){

        super.onDestroy();

        if (bsp != null) {
            bsp.dispose();
            bsp = null;
        }

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

        bsp.Capture(NBioBSPJNI.FIR_PURPOSE.ENROLL, hCapturedFIR, timeout, hAuditFIR, capturedData, AddActivity.this, 0, null);

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
                            tv_Status.setText(msg);
                            Toast.makeText(AddActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    });
                    return ;
                }

                if (byTemplate1Fingure != null)
                    byTemplate1Fingure = null;

                byTemplate1Fingure = new byte[exportData.FingerData[0].Template[0].Data.length];
                byTemplate1Fingure = exportData.FingerData[0].Template[0].Data;

                Base64_template1  = Base64.encodeToString(byTemplate1Fingure, Base64.DEFAULT);
                //Toast.makeText(getApplicationContext(),Base64_template1,Toast.LENGTH_LONG).show();
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
                            tv_Status.setText(msg);
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
                tv_Status.setText(msg);

                if (byTemplate1Fingure != null && byTemplate2Fingure != null)  {
                    save.setEnabled(true);
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

    /**
     * Capture Two
     * @param timeout
     */
    public synchronized void OnCapture2(int timeout) {


        NBioBSPJNI.FIR_HANDLE hCapturedFIR, hAuditFIR;
        NBioBSPJNI.CAPTURED_DATA capturedData;
        hCapturedFIR = bsp.new FIR_HANDLE();
        hAuditFIR = bsp.new FIR_HANDLE();
        capturedData = bsp.new CAPTURED_DATA();
        bCapturedFirst = false;

        bsp.Capture(NBioBSPJNI.FIR_PURPOSE.ENROLL, hCapturedFIR, timeout, hAuditFIR, capturedData, this, 0, null);
        if(sampleDialogFragment!=null && "DIALOG_TYPE_PROGRESS".equals(sampleDialogFragment.getTag()))
            sampleDialogFragment.dismiss();

        if (bsp.IsErrorOccured()) {
            msg = "NBioBSP Capture Error: " + bsp.GetErrorCode();
        } else {
            NBioBSPJNI.INPUT_FIR inputFIR;

            inputFIR = bsp.new INPUT_FIR();

            // Make ISO 19794-2 data
            {
                NBioBSPJNI.Export.DATA exportData;

                inputFIR.SetFIRHandle(hCapturedFIR);

                exportData = exportEngine.new DATA();

                exportEngine.ExportFIR(inputFIR, exportData, NBioBSPJNI.EXPORT_MINCONV_TYPE.OLD_FDA);

                if (bsp.IsErrorOccured()) {
                    runOnUiThread(new Runnable() {

                        public void run() {
                            msg = "NBioBSP ExportFIR Error: " + bsp.GetErrorCode();
                            tv_Status.setText(msg);
                            Toast.makeText(AddActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }

                if (byTemplate2Fingure != null)
                    byTemplate2Fingure = null;

                byTemplate2Fingure = new byte[exportData.FingerData[0].Template[0].Data.length];
                byTemplate2Fingure = exportData.FingerData[0].Template[0].Data;
                Base64_template2 = Base64.encodeToString(byTemplate2Fingure, Base64.DEFAULT);
            }

            // Make Raw Image data
            {
                NBioBSPJNI.Export.AUDIT exportAudit;

                inputFIR.SetFIRHandle(hAuditFIR);

                exportAudit = exportEngine.new AUDIT();

                exportEngine.ExportAudit(inputFIR, exportAudit);

                if (bsp.IsErrorOccured()) {

                    runOnUiThread(new Runnable() {

                        public void run() {
                            msg = "NBioBSP ExportAudit Error: " + bsp.GetErrorCode();
                            tv_Status.setText(msg);
                            // Toast.makeText(Android_Demo.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    });

                    return;
                }

                if (byCapturedRaw2 != null)
                    byCapturedRaw2 = null;

                byCapturedRaw2 = new byte[exportAudit.FingerData[0].Template[0].Data.length];
                byCapturedRaw2 = exportAudit.FingerData[0].Template[0].Data;

                nCapturedRawWidth2 = exportAudit.ImageWidth;
                nCapturedRawHeight2 = exportAudit.ImageHeight;

                msg = "Second Capture Success";
            }

        }

        runOnUiThread(new Runnable() {

            public void run() {
                tv_Status.setText(msg);

                if (byTemplate1Fingure != null && byTemplate2Fingure != null) {
                    save.setEnabled(true);
                } else {
                    //btnVerifyTemplate.setEnabled(false);
                }

               /* if (byCapturedRaw1 != null && byCapturedRaw2 != null)  {
                    btnVerifyRaw.setEnabled(true);
                }else{
                    btnVerifyRaw.setEnabled(false);
                }*/

            }
        });
    }


    public int OnCaptured(NBioBSPJNI.CAPTURED_DATA capturedData){

      //  tvDevice.setText("IMAGE Quality: "+capturedData.getImageQuality());

        if( capturedData.getImage()!=null){
            if (bCapturedFirst){
                iv_ImageFingureOne.setImageBitmap( capturedData.getImage());
            }else{
                iv_ImageFingureTwo.setImageBitmap( capturedData.getImage());
            }
        }

        // quality : 40~100
        if(capturedData.getImageQuality()>=QUALITY_LIMIT){
            if(sampleDialogFragment!=null && "DIALOG_TYPE_PROGRESS".equals(sampleDialogFragment.getTag()))
                sampleDialogFragment.dismiss();
            return NBioBSPJNI.ERROR.NBioAPIERROR_USER_CANCEL;
        }else if(capturedData.getDeviceError()!=NBioBSPJNI.ERROR.NBioAPIERROR_NONE){
            if(sampleDialogFragment!=null && "DIALOG_TYPE_PROGRESS".equals(sampleDialogFragment.getTag()))
                sampleDialogFragment.dismiss();
            return capturedData.getDeviceError();
        }else{
            return NBioBSPJNI.ERROR.NBioAPIERROR_NONE;
        }

    }

    public void OnConnected() {
        if(sampleDialogFragment!=null)
            sampleDialogFragment.dismiss();

        String message = "Device Open Success";
        tv_Status.setText(message);

    }

    public void OnDisConnected() {
        NBioBSPJNI.CURRENT_PRODUCT_ID = 0;

        if(sampleDialogFragment!=null)
            sampleDialogFragment.dismiss();

        String message = "NBioBSP Disconnected: " + bsp.GetErrorCode();
        tv_Status.setText(message);

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


    /**
     * Verify User Async
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
                    updateDisplay2();
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
