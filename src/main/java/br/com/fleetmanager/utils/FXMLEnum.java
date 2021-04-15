package br.com.fleetmanager.utils;

import java.util.AbstractMap;
import java.util.HashMap;

public class FXMLEnum {

    public enum Enum {
        MAINWINDOW, VEHICLE, FINANCIALCATEGORY, FINANCIALTRANSACTION
    }

    private static final HashMap<FXMLEnum.Enum, AbstractMap.SimpleEntry<String, String>> FXMLMapEnum = new HashMap<>();
    static {
        FXMLMapEnum.put(Enum.MAINWINDOW, new AbstractMap.SimpleEntry<>("mainWindow", "Gerenciador de Frota"));
        FXMLMapEnum.put(Enum.VEHICLE, new AbstractMap.SimpleEntry<>("vehicle", "Veículos"));
        FXMLMapEnum.put(Enum.FINANCIALCATEGORY, new AbstractMap.SimpleEntry<>("financialCategory", "Categorias financeiras"));
        FXMLMapEnum.put(Enum.FINANCIALTRANSACTION, new AbstractMap.SimpleEntry<>("financialTransactions", "Lançamentos financeiros"));
    }

    public static String getFXMLFile(FXMLEnum.Enum pFXML) {
        return FXMLMapEnum.get(pFXML).getKey();
    }

    public static String getFXMLTittle(FXMLEnum.Enum pFXML) {
        return FXMLMapEnum.get(pFXML).getValue();
    }

    private static final HashMap<FXMLEnum.Enum, String> FXMLIconEnum = new HashMap<>();
    static {
        FXMLIconEnum.put(Enum.MAINWINDOW, "semi_truck_16px");
        FXMLIconEnum.put(Enum.VEHICLE, "truck_16px");
        FXMLIconEnum.put(Enum.FINANCIALCATEGORY, "cashbook_16px");
        FXMLIconEnum.put(Enum.FINANCIALTRANSACTION, "transaction_16px");
    }

    public static String getFXMLIcon(FXMLEnum.Enum pFXML) {
        return FXMLIconEnum.get(pFXML) + ".png";
    }

}
