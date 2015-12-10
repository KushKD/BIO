package com.DIT.HP.AadhaarAttendance;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.List;

import android.widget.TextView;

import com.DIT.HP.Aadhaar.R;

/**
 * Created by kuush on 12/9/2015.
 */
public class BinderData extends BaseAdapter {

    LayoutInflater inflater;
    ImageView thumb_image;
    List<HashMap<String,String>> AttendanceList;
    ViewHolder holder;

    public BinderData() {
        // TODO Auto-generated constructor stub
    }

    public BinderData(Activity act, List<HashMap<String,String>> map) {

        this.AttendanceList = map;

        inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public int getCount() {
        return AttendanceList.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null){

            vi = inflater.inflate(R.layout.list_row, null);
            holder = new ViewHolder();

            holder.tvaadhaar = (TextView)vi.findViewById(R.id.aadhaar); // city name
            holder.tvdate_time = (TextView)vi.findViewById(R.id.date_time); // city weather overview
            holder.tvname =  (TextView)vi.findViewById(R.id.name); // city temperature
          //  holder.tvWeatherImage =(ImageView)vi.findViewById(R.id.list_image); // thumb image

            vi.setTag(holder);
        }
        else{

            holder = (ViewHolder)vi.getTag();
        }

        // Setting all values in listview

        holder.tvaadhaar.setText(AttendanceList.get(position).get(DatabaseHandler.AADHAAR_Attendance_DB));
        holder.tvdate_time.setText(AttendanceList.get(position).get(DatabaseHandler.DATETIME_DB));
        holder.tvname.setText(AttendanceList.get(position).get(DatabaseHandler.NAME_ATTENDANCE_DB));

        return vi;
    }

    static class ViewHolder{

        TextView tvaadhaar;
        TextView tvname;
        TextView tvdate_time;
    }
}


