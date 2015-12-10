package com.DIT.HP.AadhaarAttendance;

import java.io.Serializable;

/**
 * Created by kuush on 12/9/2015.
 */
public class SaveAttendanceUser_POJO implements Serializable {

    private String Name;
    private String Aadhaar;
    private String PunchTime;

    public String getSync() {
        return Sync;
    }

    public void setSync(String sync) {
        Sync = sync;
    }

    private String Sync;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAadhaar() {
        return Aadhaar;
    }

    public void setAadhaar(String aadhaar) {
        Aadhaar = aadhaar;
    }

    public String getPunchTime() {
        return PunchTime;
    }

    public void setPunchTime(String punchTime) {
        PunchTime = punchTime;
    }


}
