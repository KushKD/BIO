package com.DIT.HP.AadhaarAttendance;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.DIT.HP.Aadhaar.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ReportActivity extends BaseActivity {

    Button bt_CloseReport;

    DatabaseHandler DH = null;
    BinderData bindingData ;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        list = (ListView)findViewById(R.id.listreport);


        ReadDB_Details RDBA = new ReadDB_Details();
        RDBA.execute();

        bt_CloseReport = (Button)findViewById(R.id.closereport);
        bt_CloseReport.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ReportActivity.this.finish();
            }
        });


    }

    class ReadDB_Details extends AsyncTask<String,String,String>{

        ArrayList<HashMap<String,String>> listDB_AttendanceDetails = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            DH = new DatabaseHandler(ReportActivity.this);
            listDB_AttendanceDetails = new ArrayList<HashMap<String,String>>();
            listDB_AttendanceDetails = DH.GetAllData_AttendanceStatus();
            String Message = null;

            if(listDB_AttendanceDetails.size()!=0){


                bindingData = new BinderData(ReportActivity.this,listDB_AttendanceDetails);
                Message = "List is not empty" + Integer.toString( listDB_AttendanceDetails.size());


            }else{
                Message = "List is  empty";
            }





            return Message;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            list.setAdapter(bindingData);
            Toast.makeText(ReportActivity.this,s,Toast.LENGTH_LONG).show();
        }
    }


}
