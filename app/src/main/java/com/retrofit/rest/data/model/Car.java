package com.retrofit.rest.data.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Car {

    @SerializedName("id") @Expose public int id;
    @SerializedName("model") @Expose public String model;
    @SerializedName("year") @Expose public int year;
    @SerializedName("price") @Expose public int price;
    @SerializedName("driverID") @Expose public int driverID;

    public Car() {
    }

    public Car(int id, String model, int year, int price, int driverID) {
        super();
        this.id = id;
        this.model = model;
        this.year = year;
        this.price = price;
        this.driverID = driverID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getDriverID() {
        return driverID;
    }

    public void setDriverID(int driverID) {
        this.driverID = driverID;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", model='" + model + '\'' +
                ", year=" + year +
                ", price=" + price +
                ", driverID=" + driverID +
                '}';
    }
}
