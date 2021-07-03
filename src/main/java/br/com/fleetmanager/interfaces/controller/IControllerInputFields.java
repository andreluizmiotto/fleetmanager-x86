package br.com.fleetmanager.interfaces.controller;

public interface IControllerInputFields extends IControllerBase{

    boolean missingRequiredFields();
    void clearFields();

}
