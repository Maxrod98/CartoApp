package com.ecarto.cartoapp.web.DTOs;

import android.content.Context;

import com.ecarto.cartoapp.database.Entities.InvoiceEntity;
import com.ecarto.cartoapp.database.Repositories.InvoiceRepository;

import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.schedulers.Schedulers;

public class InvoiceDTO {
    private Long InvoiceID;
    private Long ProjectID; //foreign key
    private String UserID; //foreign
    private String Seller;
    private long Date;
    private String Latitude;
    private String Longitude;
    private String Description;
    private Integer Status;
    private short Version;

    List<InvoiceDetailDTO> InvoiceDetailDTOs;

    public InvoiceDTO(InvoiceEntity invoiceEntity, Context context) {
        InvoiceRepository invoiceRepository = new InvoiceRepository(context);

        setInvoiceID(invoiceEntity.getInvoiceID());
        setProjectID(invoiceEntity.getProjectID());
        setUserID(invoiceEntity.getUserID());
        setSeller(invoiceEntity.getSeller());
        setDate(invoiceEntity.getDate());
        setLatitude(invoiceEntity.getLatitude());
        setLongitude(invoiceEntity.getLongitude());
        setDescription(invoiceEntity.getDescription());
        setStatus(invoiceEntity.getStatus());
        setVersion(invoiceEntity.getVersion());

        setInvoiceDetailDTOs(
                invoiceRepository.findAllExtendedInvoiceDetailBy(null, invoiceEntity.getInvoiceID())
                        .subscribeOn(Schedulers.io()).blockingGet()
                        .stream().map(x -> new InvoiceDetailDTO(x)).collect(Collectors.toList())
        );
    }

    public Long getInvoiceID() {
        return InvoiceID;
    }

    public void setInvoiceID(Long invoiceID) {
        InvoiceID = invoiceID;
    }

    public Long getProjectID() {
        return ProjectID;
    }

    public void setProjectID(Long projectID) {
        ProjectID = projectID;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getSeller() {
        return Seller;
    }

    public void setSeller(String seller) {
        Seller = seller;
    }

    public long getDate() {
        return Date;
    }

    public void setDate(long date) {
        Date = date;
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

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
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

    public List<InvoiceDetailDTO> getInvoiceDetailDTOs() {
        return InvoiceDetailDTOs;
    }

    public void setInvoiceDetailDTOs(List<InvoiceDetailDTO> invoiceDetailDAOs) {
        InvoiceDetailDTOs = invoiceDetailDAOs;
    }
}
