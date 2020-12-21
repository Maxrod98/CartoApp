package com.ecarto.cartoapp.database.Entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.ecarto.cartoapp.utils.StringUtils;

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
    private Long FileID;
    private Long InvoiceDetailID; //foreign key
    private String PathToFile;
    private String TypeOfFile;
    private String OriginalName;
    private Integer Status;

    public FileEntity(){
        setFileID(StringUtils.getUniqueID());
    }

    public Integer getStatus() {
        return Status;
    }

    public void setStatus(Integer status) {
        Status = status;
    }

    public String getOriginalName() {
        return OriginalName;
    }

    public void setOriginalName(String originalName) {
        OriginalName = originalName;
    }

    @NonNull
    public Long getFileID() {
        return FileID;
    }

    public void setFileID(@NonNull Long fileID) {
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

    public Long getInvoiceDetailID() {
        return InvoiceDetailID;
    }

    public void setInvoiceDetailID(Long invoiceDetailID) {
        InvoiceDetailID = invoiceDetailID;
    }
}
