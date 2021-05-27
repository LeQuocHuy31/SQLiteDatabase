package com.example.sqliteuser;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class User implements Serializable{
    private long id;
    private String name;
    private String address;
    private  String year;

    public User() {
    }

    public User(long id, String name, String address, String year) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.year = year;
    }

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
