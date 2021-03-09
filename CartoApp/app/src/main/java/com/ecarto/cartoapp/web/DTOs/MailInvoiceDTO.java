package com.ecarto.cartoapp.web.DTOs;

import com.ecarto.cartoapp.database.Entities.ExtendedInvoiceEntity;
import com.ecarto.cartoapp.database.Entities.InvoiceEntity;
import com.ecarto.cartoapp.utils.StringUtils;

public class MailInvoiceDTO {
    String Receptor;
    String Fecha;
    Integer Total;
    String Folio;

    public ExtendedInvoiceEntity toExtendedInvoiceEntity(){
        ExtendedInvoiceEntity invoiceEntity = new ExtendedInvoiceEntity();
        invoiceEntity.setTotalCost(Total);
        invoiceEntity.setDate(StringUtils.formatDateFromString(Fecha));
        invoiceEntity.setSeller( Receptor );
        invoiceEntity.setDescription("Folio: "  + Folio);

        return invoiceEntity;
    }

    public String getReceptor() {
        return Receptor;
    }

    public void setReceptor(String receptor) {
        Receptor = receptor;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public Integer getTotal() {
        return Total;
    }

    public void setTotal(Integer total) {
        Total = total;
    }

    public String getFolio() {
        return Folio;
    }

    public void setFolio(String folio) {
        Folio = folio;
    }
}
