package com.example.skylinestockservice.Model;

public class Items {
     private String Iname,description,Image,
             price,category,paid,date,time;
     public Items(){



     }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    //cons..
    //mote that the descriped must match with DB
    public Items(String Iname , String description,
                 String image, String price, String category,
                 String paid, String date, String time) {
        this.Iname = Iname;
        this.description = description;
        this.Image = image;
        this.price = price;
        this.category = category;
        this.paid = paid;
        this.date = date;
        this.time = time;
    }

    public String getiName() {
        return Iname;
    }

    public void setiName(String Iname) {
        this.Iname = Iname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        this.Image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
}
