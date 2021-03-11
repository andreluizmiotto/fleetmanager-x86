package br.com.fleetmanager.model;

import br.com.fleetmanager.interfaces.IBaseModel;

public class Vehicle implements IBaseModel {

    private int id;
    private String plate;
    private String description;
    private String yearFabricated;

    public Vehicle() {
    }

    public Vehicle(String plate, String description, String yearFabricated) {
        this.plate = plate;
        this.description = description;
        this.yearFabricated = yearFabricated;
    }

    public Vehicle(int id, String plate, String description, String yearFabricated) {
        this.id = id;
        this.plate = plate;
        this.description = description;
        this.yearFabricated = yearFabricated;
    }

    public int getId() {
        return id;
    }

    public String getPlate() {
        return plate;
    }

    public String getDescription() {
        return description;
    }

    public String getYearFabricated() {
        return yearFabricated;
    }

    public void setId(int id) {
        this.id = id;
    }
}
