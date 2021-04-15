package br.com.fleetmanager.model;

import br.com.fleetmanager.abstracts.ABaseModel;

import java.sql.Date;

public class FinancialTransaction extends ABaseModel {

    private Date date;
    private Vehicle vehicle;
    private FinancialCategory category;
    private double value;
    private String description;

    public FinancialTransaction() {
    }

    public FinancialTransaction(Date date, Vehicle vehicle, FinancialCategory category, double value, String description) {
        this.date = date;
        this.vehicle = vehicle;
        this.category = category;
        this.value = value;
        this.description = description;
    }

    public FinancialTransaction(int id, short status, Date date, Vehicle vehicle, FinancialCategory category, double value, String description) {
        super(id, status);
        this.date = date;
        this.vehicle = vehicle;
        this.category = category;
        this.value = value;
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public FinancialCategory getCategory() {
        return category;
    }

    public double getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }
}
