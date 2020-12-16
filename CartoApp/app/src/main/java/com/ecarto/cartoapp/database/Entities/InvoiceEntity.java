package com.ecarto.cartoapp.database.Entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class InvoiceEntity implements Serializable {
    @PrimaryKey
    @NonNull
    private Integer InvoiceID;
    private Integer ProjectID; //foreign key
    private Integer UserID; //foreign
    private String Seller;
    private long Date;
    private long Latitude;
    private long Longitude;
    private String Description;

    public Integer getInvoiceStatus() {
        return InvoiceStatus;
    }

    public void setInvoiceStatus(Integer invoiceStatus) {
        InvoiceStatus = invoiceStatus;
    }

    private Integer InvoiceStatus;

    @NonNull
    public Integer getInvoiceID() {
        return InvoiceID;
    }

    public void setInvoiceID(@NonNull Integer invoiceID) {
        this.InvoiceID = invoiceID;
    }

    public Integer getUserID() {
        return UserID;
    }

    public void setUserID(Integer userID) {
        UserID = userID;
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

    public Integer getProjectID() {
        return ProjectID;
    }

    public void setProjectID(Integer projectID) {
        ProjectID = projectID;
    }

    @Override
    public String toString() {
        return "{" +
                "'" + Seller + '\'' +
                ",='" + Description + '\'' +
                '}';
    }
}
