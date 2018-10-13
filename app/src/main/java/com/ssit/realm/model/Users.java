package com.ssit.realm.model;

import com.google.android.gms.maps.model.LatLng;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Users extends RealmObject {

    @PrimaryKey
    private long id;

    private String name;

    private String username;

    private String email;

    private String address;

    private String phone;

    private String website;

    private String company;

    private String userimage;

    private double Lat;

    private double Lang;

    // Standard getters & setters generated by your IDE…
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setUserimage(String userimage) {
        this.userimage = userimage;
    }

    public String getUserimage() {
        return userimage;
    }

    public void setLat(double lat) {
        Lat = lat;
    }

    public double getLat() {
        return Lat;
    }

    public void setLang(double lang) {
        Lang = lang;
    }

    public double getLang() {
        return Lang;
    }
}
