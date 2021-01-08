package com.ecarto.cartoapp.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ecarto.cartoapp.database.Entities.FileEntity;

import java.util.List;

public class MainActivityViewModel extends ViewModel {
    private MutableLiveData<List<FileEntity>> filesSelected = new MutableLiveData<>();

    public MutableLiveData<List<FileEntity>> getFilesSelected() {
        return filesSelected;
    }

    public MutableLiveData<Boolean> onFilesBeingInserted = new MutableLiveData<>();

    public MutableLiveData<Boolean> getOnFilesBeingInserted() {
        return onFilesBeingInserted;
    }

    public void setFilesSelected(List<FileEntity> filesSelected) {
        this.filesSelected.setValue(filesSelected);
    }

    public void setOnFilesBeingInserted(Boolean isInserted) {
        this.onFilesBeingInserted.setValue(isInserted);
    }

    public LiveData<Long> getInvoiceDetailID;


}
