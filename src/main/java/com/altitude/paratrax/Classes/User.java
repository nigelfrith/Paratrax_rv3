package com.altitude.paratrax.Classes;

public class User {
    public String firstName;
    public String lastName;
    public String userName;
    public String phone;
    public String email;
    public String gender;
    public int age;
    public int weight_kg;
    public String licenceGrade;
    public String licenceNumber;

    public User() {}

    public User(
            String firstName,
            String lastName,
            String userName,
            String phone,
            String email,
            String gender,
            int age,
            int weight_kg,
            String licenceGrade,
            String licenceNumber
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
        this.age = age;
        this.weight_kg = weight_kg;
        this.licenceGrade = licenceGrade;
        this.licenceNumber = licenceNumber;
    }

}
