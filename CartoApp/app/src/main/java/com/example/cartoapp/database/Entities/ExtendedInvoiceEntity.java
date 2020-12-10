package com.example.cartoapp.database.Entities;

public class ExtendedInvoiceEntity extends InvoiceEntity {
    private double totalCost;

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }
}
