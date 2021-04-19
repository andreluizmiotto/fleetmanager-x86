package br.com.fleetmanager.model;

import br.com.fleetmanager.abstracts.ABaseModel;

public class Vehicle extends ABaseModel {

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

    public Vehicle(int id, short status, String plate, String description, String yearFabricated) {
        super(id, status);
        this.plate = plate;
        this.description = description;
        this.yearFabricated = yearFabricated;
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

    @Override
    public String toString() {
        return id + ". " + plate + " - " + description;
    }
}
