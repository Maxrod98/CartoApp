package com.ecarto.cartoapp.web.DTOs;

import android.content.Context;

import com.ecarto.cartoapp.database.Entities.ProjectEntity;
import com.ecarto.cartoapp.database.Repositories.InvoiceRepository;
import com.ecarto.cartoapp.database.Repositories.ProjectRepository;

import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.schedulers.Schedulers;

public class ProjectDTO {
    private Long ProjectID;
    private String Name;
    private long StartDate;
    private String Latitude;
    private String Longitude;
    private String Location;
    private Integer Status;
    private Integer UserID;

    List<InvoiceDTO> InvoiceDTOs;

    public ProjectDTO(){
    }

    public void forEachElement( ProjectDTO.ForEachElementListener forEachElementListener){
        forEachElementListener.onEachProjectDTO(this);

        this.getInvoiceDTOs().stream().forEach((invoice) -> {
            if (invoice != null) {
                forEachElementListener.onEachInvoiceDTO(invoice);
                if (invoice.getInvoiceDetailDTOs() != null) {
                    invoice.getInvoiceDetailDTOs().stream().forEach((invoiceDetail) -> {
                        forEachElementListener.onEachInvoiceDetailDTO(invoiceDetail);
                    });
                }
            }
        });
    }

    public ProjectDTO(ProjectEntity projectEntity, Context context){
        InvoiceRepository invoiceRepository = new InvoiceRepository(context);

        setProjectID(projectEntity.getProjectID());
        setName(projectEntity.getName());
        setStartDate(projectEntity.getStartDate());
        setLatitude(projectEntity.getLatitude());
        setLongitude(projectEntity.getLongitude());
        setLocation(projectEntity.getLocation());
        setStatus(projectEntity.getStatus());
        setUserID(projectEntity.getUserID());

        setInvoiceDTOs(
                invoiceRepository.findAllExtendedInvoiceByParams(null, projectEntity.getProjectID(), null, null, null, null, null)
                .subscribeOn(Schedulers.io()).blockingGet()
                .stream().map(x -> new InvoiceDTO(x, context)).collect(Collectors.toList())
        );
    }

    public Integer getUserID() {
        return UserID;
    }

    public void setUserID(Integer userID) {
        UserID = userID;
    }

    public Long getProjectID() {
        return ProjectID;
    }

    public void setProjectID(Long projectID) {
        ProjectID = projectID;
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

    public List<InvoiceDTO> getInvoiceDTOs() {
        return InvoiceDTOs;
    }

    public void setInvoiceDTOs(List<InvoiceDTO> invoiceDTOs) {
        InvoiceDTOs = invoiceDTOs;
    }

    public interface ForEachElementListener {
        void onEachProjectDTO(ProjectDTO projectDTO);
        void onEachInvoiceDTO(InvoiceDTO invoiceDTO);
        void onEachInvoiceDetailDTO(InvoiceDetailDTO invoiceDetailDTO);
    }
}
