package com.ecarto.cartoapp.database.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.ecarto.cartoapp.utils.StringUtils;

import java.io.Serializable;

import io.reactivex.annotations.NonNull;

@Entity
public class ProjectEntity  implements Serializable {
    @PrimaryKey
    @NonNull
    private Long ProjectID;
    private String Name;
    private long StartDate;
    private String Latitude;
    private String Longitude;
    private String Location;
    private Integer Status;


    public ProjectEntity(){
        setProjectID(StringUtils.getUniqueID());
    }

    @NonNull
    public Long getProjectID() {
        return ProjectID;
    }

    public void setProjectID(@NonNull Long projectID) {
        this.ProjectID = projectID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public long getStartDate() {
        return StartDate;
    }

    public void setStartDate(long startDate) {
        StartDate = startDate;
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

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public Integer getStatus() {
        return Status;
    }

    public void setStatus(Integer status) {
        Status = status;
    }
}
