package com.ecarto.cartoapp.database.Entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.ecarto.cartoapp.utils.StringUtils;

import java.io.Serializable;

@Entity
public class InvoiceEntity implements Serializable {
    @PrimaryKey
    @NonNull
    private Long InvoiceID;
    private Long ProjectID; //foreign key
    private String UserID; //foreign
    private String Seller;
    private long Date;
    private String Latitude;
    private String Longitude;
    private String Description;
    private Integer Status;
    private short Version;

    public InvoiceEntity(){
        setInvoiceID(StringUtils.getUniqueID());
    }

    public short getVersion() {
        return Version;
    }

    public void setVersion(short version) {
        Version = version;
    }

    public Integer getStatus() {
        return Status;
    }

    public void setStatus(Integer status) {
        Status = status;
    }

    public Integer getInvoiceStatus() {
        return InvoiceStatus;
    }

    public void setInvoiceStatus(Integer invoiceStatus) {
        InvoiceStatus = invoiceStatus;
    }

    private Integer InvoiceStatus;

    @NonNull
    public Long getInvoiceID() {
        return InvoiceID;
    }

    public void setInvoiceID(@NonNull Long invoiceID) {
        this.InvoiceID = invoiceID;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
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

    public Long getProjectID() {
        return ProjectID;
    }

    public void setProjectID(Long projectID) {
        ProjectID = projectID;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    @Override
    public String toString() {
        return Seller + ' ' + Description + ' ' + StringUtils.formatDateFromLong(getDate()) + " " + StringUtils.formateDateToMonthAndWhetherIsToday(getDate());
    }
}
