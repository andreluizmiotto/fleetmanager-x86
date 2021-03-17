package br.com.fleetmanager.abstracts;

import br.com.fleetmanager.interfaces.IBaseModel;

public abstract class ABaseModel implements IBaseModel {

    protected int id;
    protected short status;

    public ABaseModel() {
    }

    public ABaseModel(int id, short status) {
        this.id = id;
        this.status = status;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public short getStatus() {
        return status;
    }

    @Override
    public void setId(int value) {
        this.id = value;
    }
}
