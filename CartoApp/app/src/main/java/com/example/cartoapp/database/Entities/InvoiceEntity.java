package com.example.cartoapp.database.Entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class InvoiceEntity {
    @PrimaryKey
    @NonNull
    private Integer InvoiceID;
    private String Seller;
    private long Date;
    private long Latitude;
    private long Longitude;
    private String Description;

    @NonNull
    public Integer getInvoiceID() {
        return InvoiceID;
    }

    public void setInvoiceID(@NonNull Integer invoiceID) {
        InvoiceID = invoiceID;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getSeller() {
        return Seller;
    }

    public void setSeller(String seller) {
        Seller = seller;
    }

    public long getDate() {
        return Date;
    }

    public void setDate(long date) {
        this.Date = date;
    }

    public long getLatitude() {
        return Latitude;
    }

    public void setLatitude(long latitude) {
        this.Latitude = latitude;
    }

    public long getLongitude() {
        return Longitude;
    }

    public void setLongitude(long longitude) {
        this.Longitude = longitude;
    }
}
