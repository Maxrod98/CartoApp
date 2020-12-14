package com.example.cartoapp.database.Entities;

public class ExtendedInvoiceEntity extends InvoiceEntity {
    private Integer totalCost;

    public Integer getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Integer totalCost) {
        this.totalCost = totalCost;
    }
}
