package com.example.cartoapp.database.Entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import io.reactivex.annotations.NonNull;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey
        (entity = InvoiceEntity.class,
                parentColumns = "InvoiceID",
                childColumns = "InvoiceID",
                onDelete = CASCADE))

public class InvoiceDetailEntity implements Serializable {
    @PrimaryKey
    @NonNull
    private Integer InvoiceDetailID;
    private Integer InvoiceID; //foreign key
    private Double Quantity;
    private String Product;
    private String Note;


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

    public String getProduct() {
        return Product;
    }

    public void setProduct(String product) {
        Product = product;
    }

    public Double getQuantity() {
        return Quantity;
    }

    public void setQuantity(Double quantity) {
        Quantity = quantity;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

}
