package com.example.skylinestockservice.Model;
//H I R E C L A S S
public class Hire {
    //same keynames
private String Iname,
            paid,price,quantity,reduce;

    public Hire() {
    }

    public Hire(String iname, String paid, String price, String quantity, String reduce) {
        Iname = iname;
        this.paid = paid;
        this.price = price;
        this.quantity = quantity;
        this.reduce = reduce;
    }

    public String getIname() {
        return Iname;
    }

    public void setIname(String iname) {
        Iname = iname;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getReduce() {
        return reduce;
    }

    public void setReduce(String reduce) {
        this.reduce = reduce;
    }
}
