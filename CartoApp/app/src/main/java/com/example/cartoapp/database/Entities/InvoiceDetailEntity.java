package com.example.cartoapp.database.Entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import io.reactivex.annotations.NonNull;

@Entity
public class InvoiceDetailEntity {
    @PrimaryKey
    @NonNull
    private Integer InvoiceDetailID;
    private Integer InvoiceID;
    private Integer Quantity;
    private String Unit;
    private String Product;
    private double cost;
    private double totalCostOfItem;
    private String typeOfFile;
    private String pathToFile;

    @NonNull
    public Integer getInvoiceDetailID() {
        return InvoiceDetailID;
    }

    public void setInvoiceDetailID(@NonNull Integer invoiceDetailID) {
        InvoiceDetailID = invoiceDetailID;
    }

    public Integer getInvoiceID() {
        return InvoiceID;
    }

    public void setInvoiceID(Integer invoiceID) {
        InvoiceID = invoiceID;
    }

    public Integer getQuantity() {
        return Quantity;
    }

    public void setQuantity(Integer quantity) {
        Quantity = quantity;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getProduct() {
        return Product;
    }

    public void setProduct(String product) {
        Product = product;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getTotalCostOfItem() {
        return totalCostOfItem;
    }

    public void setTotalCostOfItem(double totalCostOfItem) {
        this.totalCostOfItem = totalCostOfItem;
    }

    public String getTypeOfFile() {
        return typeOfFile;
    }

    public void setTypeOfFile(String typeOfFile) {
        this.typeOfFile = typeOfFile;
    }

    public String getPathToFile() {
        return pathToFile;
    }

    public void setPathToFile(String pathToFile) {
        this.pathToFile = pathToFile;
    }
}
