package com.altitude.paratrax.Models;

public class Full_Logbook_Model {
    public String fname, lname, weight, age, email, phone, additional, userid;

    public Full_Logbook_Model() {

    }

    public Full_Logbook_Model(String userid, String fname, String lname, String email) {
        this.userid = userid;
        this.fname = fname;
        this.lname = lname;
        this.email = email;
    }


    public String getWeight() {
        return weight;
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

    public String getuserid() {
        return userid;
    }

    public void setuserid(String userid) {
        this.userid = userid;
    }


}
