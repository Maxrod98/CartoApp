package com.ecarto.cartoapp.database.Entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.ecarto.cartoapp.utils.StringUtils;

import java.io.Serializable;

import io.reactivex.annotations.NonNull;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey
        (entity = InvoiceEntity.class,
                parentColumns = "InvoiceID",
                childColumns = "InvoiceID",
                onDelete = CASCADE))

public class InvoiceDetailEntity implements Serializable {
    @PrimaryKey
    @NonNull
    private Long InvoiceDetailID;
    private Long InvoiceID; //foreign key
    private Integer CostOfItem;
    private String ConceptDescription;
    private String Notes;
    private Integer Status;
    private short Version;

    public InvoiceDetailEntity(){
        setInvoiceDetailID(StringUtils.getUniqueID());
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

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    @NonNull
    public Long getInvoiceDetailID() {
        return InvoiceDetailID;
    }

    public void setInvoiceDetailID(@NonNull Long invoiceDetailID) {
        this.InvoiceDetailID = invoiceDetailID;
    }

    public Long getInvoiceID() {
        return InvoiceID;
    }

    public void setInvoiceID(Long invoiceID) {
        InvoiceID = invoiceID;
    }

    public String getConceptDescription() {
        return ConceptDescription;
    }

    public void setConceptDescription(String conceptDescription) {
        ConceptDescription = conceptDescription;
    }

    public Integer getCostOfItem() {
        return CostOfItem;
    }

    public void setCostOfItem(Integer costOfItem) {
        CostOfItem = costOfItem;
    }

}
