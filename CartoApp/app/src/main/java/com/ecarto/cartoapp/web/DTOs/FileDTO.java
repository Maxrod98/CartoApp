package com.ecarto.cartoapp.web.DTOs;

import com.ecarto.cartoapp.database.Entities.FileEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Blob;

public class FileDTO {
    private Long FileID;
    private Long InvoiceDetailID; //foreign key
    private String PathToFile;
    private String TypeOfFile;
    private String OriginalName;
    private Integer Status;
    private Blob File;

    public FileDTO(FileEntity fileEntity){
        setFileID(fileEntity.getFileID());
        setInvoiceDetailID(fileEntity.getInvoiceDetailID());
        setPathToFile(fileEntity.getPathToFile());
        setTypeOfFile(fileEntity.getTypeOfFile());
        setOriginalName(fileEntity.getOriginalName());
        setStatus(fileEntity.getStatus());
        byte[] fileInBytes;

        //converting file to blob
        try {
            fileInBytes = Files.readAllBytes(Paths.get(fileEntity.getPathToFile()));
            //TODO: FINISH THIS LATER


        } catch ( IOException e){

        }


    }

    public Long getFileID() {
        return FileID;
    }

    public void setFileID(Long fileID) {
        FileID = fileID;
    }

    public Long getInvoiceDetailID() {
        return InvoiceDetailID;
    }

    public void setInvoiceDetailID(Long invoiceDetailID) {
        InvoiceDetailID = invoiceDetailID;
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

    public String getOriginalName() {
        return OriginalName;
    }

    public void setOriginalName(String originalName) {
        OriginalName = originalName;
    }

    public Integer getStatus() {
        return Status;
    }

    public void setStatus(Integer status) {
        Status = status;
    }

    public Blob getFile() {
        return File;
    }

    public void setFile(Blob file) {
        File = file;
    }
}
