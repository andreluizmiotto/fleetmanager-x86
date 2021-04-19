package br.com.fleetmanager.model;

import br.com.fleetmanager.abstracts.ABaseModel;
import br.com.fleetmanager.utils.Constants;

import java.util.HashMap;

public class FinancialCategory extends ABaseModel {

    private String description;
    private short type;

    private static final HashMap<Short, String> typeEnum = new HashMap<>();
    static {
        typeEnum.put(Constants.cIncome, "Receita");
        typeEnum.put(Constants.cExpense, "Despesa");
    }

    public FinancialCategory() {
    }

    public FinancialCategory(String description, short type) {
        this.description = description;
        this.type = type;
    }

    public FinancialCategory(int id, short status, String description, short type) {
        super(id, status);
        this.description = description;
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public short getType() {
        return type;
    }

    public String getTypeToString() {
        return typeEnum.get(this.type);
    }

    @Override
    public String toString() {
        return id + ". " + description;
    }
}
