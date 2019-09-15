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
    private boolean hasMedical;
    private boolean hasDisability;
    private boolean hasBaggage;
    private boolean hasTransport;
    private boolean hasPics;
    private boolean hasSherpa;
    private boolean hasPacking;
    private boolean hasSDGiven;
    private String mUserId;
    private String dateTime;

    public Quick_Log(){}

    public Quick_Log(
            String fname,
            String lname,
            String weight,
            String age,
            String email,
            String phone,
            String additional,
            boolean hasMedical,
            boolean hasDisability,
            boolean hasTransport,
            boolean hasBaggage,
            boolean hasPics,
            boolean hasSherpa,
            boolean hasPacking,
            boolean hasSDGiven,
            String mUserId,
            String dateTime)
    {      this.fname = fname;
        this.lname = lname;
        this.weight = weight;
        this.age = age;
        this.email = email;
        this.phone = phone;
        this.additional = additional;
        this.hasMedical = hasMedical;
        this.hasDisability = hasDisability;
        this.hasBaggage = hasBaggage;
        this.hasTransport = hasTransport;
        this.hasPics = hasPics;
        this.hasSherpa = hasSherpa;
        this.hasPacking = hasPacking;
        this.hasSDGiven = hasSDGiven;
        this.mUserId = mUserId;
        this.dateTime = dateTime;
    }

    public boolean isHasTransport() {
        return hasTransport;
    }
    public void setHasTransport(boolean hasTransport) {
        this.hasTransport = hasTransport;
    }

    public boolean isHasPics() {
        return hasPics;
    }
    public void setHasPics(boolean hasPics) {
        this.hasPics = hasPics;
    }

    public boolean isHasSherpa() {
        return hasSherpa;
    }
    public void setHasSherpa(boolean hasSherpa) {
        this.hasSherpa = hasSherpa;
    }

    public boolean isHasPacking() {
        return hasPacking;
    }
    public void setHasPacking(boolean hasPacking) {
        this.hasPacking = hasPacking;
    }

    public boolean isHasSDGiven() {
        return hasSDGiven;
    }
    public void setHasSDGiven(boolean hasSDGiven) {
        this.hasSDGiven = hasSDGiven;
    }

    public boolean isHasBaggage() {return hasBaggage; }
    public void setHasBaggage(boolean hasBaggage) {
        this.hasBaggage = hasBaggage;
    }

    public boolean isHasMedical() {
        return hasMedical;
    }
    public void setHasMedical(boolean hasMedical) {
        hasMedical = hasMedical;
    }

    public boolean isHasDisability() {
        return hasDisability;
    }
    public void setHasDisability(boolean hasDisability) {
        hasDisability = hasDisability;
    }

    public String getDateTime() {
        return dateTime;
    }
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
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

//    public Quick_Log(String fname, String lname, String weight, String age,
//                     String email, String phone, String additional,
//                     boolean hasMedical, boolean hasDisability,boolean hasBaggage,boolean hasTransport,boolean hasPics,boolean hasSherpa,
//                     boolean hasPacking,boolean hasSDGiven,String mUserId, String dateTime) {
//        this.fname = fname;
//        this.lname = lname;
//        this.weight = weight;
//        this.age = age;
//        this.email = email;
//        this.phone = phone;
//        this.additional = additional;
//        this.hasMedical = hasMedical;
//        this.hasDisability = hasDisability;
//        this.hasBaggage = hasBaggage;
//
//
//        this.mUserId = mUserId;
//        this.dateTime = dateTime;
//    }


}
