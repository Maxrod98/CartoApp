package com.ecarto.cartoapp.database.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import io.reactivex.annotations.NonNull;

@Deprecated
@Entity
public class SellerEntity  implements Serializable {
    @PrimaryKey
    @NonNull
    private Integer SellerID;
    private String Name;
    private String Phone;
    private String Location;

    @NonNull
    public Integer getSellerID() {
        return SellerID;
    }

    public void setSellerID(@NonNull Integer sellerID) {
        this.SellerID = sellerID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }
}
