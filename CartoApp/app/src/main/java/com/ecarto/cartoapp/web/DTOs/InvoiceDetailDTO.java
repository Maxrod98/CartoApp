package com.ecarto.cartoapp.web.DTOs;

import android.content.Context;

import com.ecarto.cartoapp.database.Entities.InvoiceDetailEntity;

public class InvoiceDetailDTO {
    private Long InvoiceDetailID;
    private Long InvoiceID;
    private Integer CostOfItem;
    private String ConceptDescription;
    private String Notes;
    private Integer Status;
    private short Version;

    public InvoiceDetailDTO(){
    }

    public InvoiceDetailDTO(InvoiceDetailEntity invoiceDetailEntity) {
        setInvoiceDetailID(invoiceDetailEntity.getInvoiceDetailID());
        setInvoiceID(invoiceDetailEntity.getInvoiceID());
        setCostOfItem(invoiceDetailEntity.getCostOfItem());
        setConceptDescription(invoiceDetailEntity.getConceptDescription());
        setNotes(invoiceDetailEntity.getNotes());
        setStatus(invoiceDetailEntity.getStatus());
        setVersion(invoiceDetailEntity.getVersion());
    }

    public Long getInvoiceDetailID() {
        return InvoiceDetailID;
    }

    public void setInvoiceDetailID(Long invoiceDetailID) {
        InvoiceDetailID = invoiceDetailID;
    }

    public Long getInvoiceID() {
        return InvoiceID;
    }

    public void setInvoiceID(Long invoiceID) {
        InvoiceID = invoiceID;
    }

    public Integer getCostOfItem() {
        return CostOfItem;
    }

    public void setCostOfItem(Integer costOfItem) {
        CostOfItem = costOfItem;
    }

    public String getConceptDescription() {
        return ConceptDescription;
    }

    public void setConceptDescription(String conceptDescription) {
        ConceptDescription = conceptDescription;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    public Integer getStatus() {
        return Status;
    }

    public void setStatus(Integer status) {
        Status = status;
    }

    public short getVersion() {
        return Version;
    }

    public void setVersion(short version) {
        Version = version;
    }
}
