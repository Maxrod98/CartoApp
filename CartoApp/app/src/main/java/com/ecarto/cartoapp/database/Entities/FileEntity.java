package com.ecarto.cartoapp.database.Entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import io.reactivex.annotations.NonNull;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey
        (entity = InvoiceDetailEntity.class,
        parentColumns = "InvoiceDetailID",
        childColumns = "InvoiceDetailID",
        onDelete = CASCADE))

public class FileEntity implements Serializable {
    @PrimaryKey
    @NonNull
    private Integer FileID;
    private Integer InvoiceDetailID; //foreign key
    private String PathToFile;
    private String TypeOfFile;
    private String OriginalName;

    public String getOriginalName() {
        return OriginalName;
    }

    public void setOriginalName(String originalName) {
        OriginalName = originalName;
    }




    @NonNull
    public Integer getFileID() {
        return FileID;
    }

    public void setFileID(@NonNull Integer fileID) {
        this.FileID = fileID;
    }


    public String getPathToFile() {
        return PathToFile;
    }

    public void setPathToFile(String pathToFile) {
        PathToFile = pathToFile;
    }

    public String getTypeOfFile() {
        return TypeOfFile;
    }

    public void setTypeOfFile(String typeOfFile) {
        TypeOfFile = typeOfFile;
    }

    public Integer getInvoiceDetailID() {
        return InvoiceDetailID;
    }

    public void setInvoiceDetailID(Integer invoiceDetailID) {
        InvoiceDetailID = invoiceDetailID;
    }
}
