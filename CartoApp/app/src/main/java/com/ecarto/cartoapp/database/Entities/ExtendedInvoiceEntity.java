package com.ecarto.cartoapp.database.Entities;

import com.ecarto.cartoapp.utils.StringUtils;

public class ExtendedInvoiceEntity extends InvoiceEntity {
    private Integer totalCost;

    public Integer getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Integer totalCost) {
        this.totalCost = totalCost;
    }

    @Override
    public String toString() {
        return getSeller() + ' ' + getDescription() + ' ' + StringUtils.formatDateFromLong(getDate()) + " " + StringUtils.formateDateToMonthAndWhetherIsToday(getDate());
    }
}
