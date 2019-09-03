package com.altitude.paratrax.Classes;

import com.google.type.Date;

import java.sql.Timestamp;

public class Quick_Log {
    private String fname;
    private String lname;
    private String weight;
    private String age;
    private String email;
    private String phone;
    private String additional;
    private String mUserId;
    private String dateTime;

    public Quick_Log() {
    }
    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }


    public String getdateTime() {
        return dateTime;
    }

    public void setdateTime(String weight) {
        this.dateTime = dateTime;
    }
    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAdditional() {
        return additional;
    }

    public void setAdditional(String additional) {
        this.additional = additional;
    }
    public String getmUserId(){
        return mUserId;
    }
    public void setmUserId(String mUserId){
        this.mUserId = mUserId;
    }

    public Quick_Log(String fname, String lname, String weight, String age, String email, String phone, String additional, String mUserId, String dateTime) {
        this.fname = fname;
        this.lname = lname;
        this.weight = weight;
        this.age = age;
        this.email = email;
        this.phone = phone;
        this.additional = additional;
        this.mUserId = mUserId;
        this.dateTime = dateTime;
    }


}
