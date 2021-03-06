package br.com.fleetmanager.model;

import br.com.fleetmanager.interfaces.IBaseModel;

public class Vehicle implements IBaseModel {

    private int id;
    private String placa;
    private String descricao;
    private int anoFabricacao;

    public Vehicle() {
    }

    public Vehicle(int id, String placa, String descricao, int anoFabricacao) {
        this.id = id;
        this.placa = placa;
        this.descricao = descricao;
        this.anoFabricacao = anoFabricacao;
    }

    public int getId() {
        return id;
    }

    public String getPlaca() {
        return placa;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getAnoFabricacao() {
        return anoFabricacao;
    }
}
