package com.nitgen.SDK.AndroidBSP;

import java.io.Serializable;

/**
 * Created by kuush on 12/7/2015.
 */
public class POJO_User_Save implements Serializable {

    String Name_Save_User, DOB_Save_User, CO_Save_User;
    String Fingure_One_User;
    String Fingure_Two_User;
    String Aadhaar_Save_User;

    public String getAadhaar_Save_User() {
        return Aadhaar_Save_User;
    }

    public void setAadhaar_Save_User(String aadhaar_Save_User) {
        Aadhaar_Save_User = aadhaar_Save_User;
    }




    public String getFingure_One_User() {
        return Fingure_One_User;
    }

    public void setFingure_One_User(String fingure_One_User) {
        Fingure_One_User = fingure_One_User;
    }

    public String getCO_Save_User() {
        return CO_Save_User;
    }

    public void setCO_Save_User(String CO_Save_User) {
        this.CO_Save_User = CO_Save_User;
    }

    public String getDOB_Save_User() {
        return DOB_Save_User;
    }

    public void setDOB_Save_User(String DOB_Save_User) {
        this.DOB_Save_User = DOB_Save_User;
    }

    public String getName_Save_User() {
        return Name_Save_User;
    }

    public void setName_Save_User(String name_Save_User) {
        Name_Save_User = name_Save_User;
    }


    public String getFingure_Two_User() {
        return Fingure_Two_User;
    }

    public void setFingure_Two_User(String fingure_Two_User) {
        Fingure_Two_User = fingure_Two_User;
    }




}
