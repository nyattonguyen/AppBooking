package com.example.appfood.Domain;

import java.io.Serializable;
import java.util.Date;


public class Order implements Serializable {
    private int idHotel;
    private int quantity;

    private double totalPrice;
    private String customerId;
    private String customerName;
    private String nameHotel;
    private String phone;
    private String dateCheckin;
    private String dateCheckout;
    public Order(int idHotel, String nameHotel, int quantity, double totalPrice, String customerId, String customerName, String phone, String dateCheckin, String dateCheckout) {
        this.idHotel = idHotel;
        this.quantity = quantity;
        this.nameHotel = nameHotel;
        this.totalPrice = totalPrice;
        this.customerName = customerName;
        this.customerId = customerId;
        this.phone = phone;
        this.dateCheckin = dateCheckin;
        this.dateCheckout = dateCheckout;

    }
    public String getDateCheckin() {
        return dateCheckin;
    }

    public void setDateCheckin(String dateCheckin) {
        this.dateCheckin = dateCheckin;
    }

    public String getDateCheckout() {
        return dateCheckout;
    }

    public void setDateCheckout(String dateCheckout) {
        this.dateCheckout = dateCheckout;
    }

    // Getters and Setters
    public int getIdHotel() {
        return idHotel;
    }

    public void setIdHotel(int idHotel) {
        this.idHotel = idHotel;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getNameHotel() {
        return nameHotel;
    }

    public void setNameHotel(String nameHotel) {
        this.nameHotel = nameHotel;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Order() {
    }
    // toString Method
    @Override
    public String toString() {
        return "Order{" +
                "idHotel=" + idHotel +
                ", totalPrice=" + totalPrice +
                ", customerName='" + customerName + '\'' +
                ", phone='" + phone + '\'' +
                ", dateCheckin=" + dateCheckin +", dateCheckout=" + dateCheckout +
                '}';
    }
}