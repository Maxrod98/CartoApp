package com.ecarto.cartoapp.database.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import io.reactivex.annotations.NonNull;

@Entity
public class ProjectEntity  implements Serializable {
    @PrimaryKey
    @NonNull
    public Integer ProjectID;
    public String Name;
    public long StartDate;

    @NonNull
    public Integer getProjectID() {
        return ProjectID;
    }

    public void setProjectID(@NonNull Integer projectID) {
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
}
