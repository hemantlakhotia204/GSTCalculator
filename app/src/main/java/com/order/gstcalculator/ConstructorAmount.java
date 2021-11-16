package com.order.gstcalculator;


public class ConstructorAmount {
    final String  quantity;
    final String listPrice;
    final String discount;
    final String price;
    final String taxableValue;
    final String taxRate;
    final String GST;
    final String totalAmount;
    final int id;
    public ConstructorAmount(int id, String quantity, String listPrice, String discount, String price, String taxableValue, String taxRate, String GST, String totalAmount) {
        this.id = id;
        this.quantity = quantity;
        this.listPrice = listPrice;
        this.discount = discount;
        this.price = price;
        this.taxableValue = taxableValue;
        this.taxRate = taxRate;
        this.GST = GST;
        this.totalAmount = totalAmount;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getListPrice() {
        return listPrice;
    }

    public String getDiscount() {
        return discount;
    }

    public String getPrice() {
        return price;
    }

    public String getTaxableValue() {
        return taxableValue;
    }

    public String getTaxRate() {
        return taxRate;
    }

    public String getGST() {
        return GST;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public int getId() {
        return id;
    }
}
