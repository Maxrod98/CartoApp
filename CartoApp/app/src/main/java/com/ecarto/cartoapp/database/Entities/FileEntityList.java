package com.ecarto.cartoapp.database.Entities;

import java.io.Serializable;
import java.util.ArrayList;

public class FileEntityList implements Serializable {
    public ArrayList<FileEntity> files;

    public FileEntityList(ArrayList<FileEntity> files){
        this.files = files;
    }
}
