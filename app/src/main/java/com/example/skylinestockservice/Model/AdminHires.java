package com.example.skylinestockservice.Model;

public class AdminHires {
private  String Iname ,contact,address,suburb,date,time,delivery ,totalAmount;
public AdminHires() {

}
    public AdminHires(String iname, String contact, String address, String suburb, String date, String time, String delivery, String totalAmount) {
        Iname = iname;
        this.contact = contact;
        this.address = address;
        this.suburb = suburb;
        this.date = date;
        this.time = time;
        this.delivery = delivery;
        this.totalAmount = totalAmount;
    }

    public String getIname() {
        return Iname;
    }

    public void setIname(String iname) {
        Iname = iname;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}
