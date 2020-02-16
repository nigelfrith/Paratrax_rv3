package com.altitude.paratrax.Classes;

import java.util.Date;

public class Quick_Log {
    private String brief;
    private String fname;
    private String lname;
    private String weight;
    private String age;
    public String email;
    private String phone;
    private String lastFlight;
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
    private Date dateTime;
    private long dateTimeL;
    private String company;
    private String location;

    public Quick_Log() {
    }

    public Quick_Log(
            String brief,
            String fname,
            String lname,
            String weight,
            String age,
            String email,
            String phone,
            String lastFlight,
            String additional,
            boolean hasMedical,
            boolean hasDisability,
            boolean hasBaggage,
            boolean hasTransport,
            boolean hasPics,
            boolean hasSherpa,
            boolean hasPacking,
            boolean hasSDGiven,
            String mUserId,
            Date dateTime,
            Long dateTimeL,
            String company,
            String location) {
        this.brief = brief;
        this.fname = fname;
        this.lname = lname;
        this.weight = weight;
        this.age = age;
        this.email = email;
        this.phone = phone;
        this.lastFlight = lastFlight;
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
        this.dateTimeL = dateTimeL;
        this.company = company;
        this.location = location;
    }


    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }



    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
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

    public boolean isHasBaggage() {
        return hasBaggage;
    }

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


    public Long getDateTimeL() {
        return dateTimeL;
    }

    public void setDateTimeL(Long dateTimeL) {
        this.dateTimeL = dateTimeL;//.format(dateTime, "yyyy-MM-dd HH:mm:ss");
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;//.format(dateTime, "yyyy-MM-dd HH:mm:ss");
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

    public String getLastFlight() {
        return lastFlight;
    }

    public void setLastFlight(String lastFlight) {
        this.lastFlight = lastFlight;
    }


    public String getAdditional() {
        return additional;
    }

    public void setAdditional(String additional) {
        this.additional = additional;
    }


    public String getmUserId() {
        return mUserId;
    }

    public void setmUserId(String mUserId) {
        this.mUserId = mUserId;
    }


}
