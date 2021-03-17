package br.com.fleetmanager.utils;

import java.util.AbstractMap;
import java.util.HashMap;

public class FXMLEnum {

    public static enum Enum {
        MAINWINDOW, VEHICLE, FINANCIALCATEGORY
    }

    private static final HashMap<FXMLEnum.Enum, AbstractMap.SimpleEntry<String, String>> FXMLMapEnum = new HashMap<>();

    static {
        FXMLMapEnum.put(Enum.MAINWINDOW, new AbstractMap.SimpleEntry<>("mainWindow", "Gerenciador de Frota"));
        FXMLMapEnum.put(Enum.VEHICLE, new AbstractMap.SimpleEntry<>("vehicle", "Veículos"));
        FXMLMapEnum.put(Enum.FINANCIALCATEGORY, new AbstractMap.SimpleEntry<>("financialCategory", "Categorias financeiras"));
    }

    public static String getFXMLFile(FXMLEnum.Enum pFXML) {
        return FXMLMapEnum.get(pFXML).getKey();
    }

    public static String getFXMLTittle(FXMLEnum.Enum pFXML) {
        return FXMLMapEnum.get(pFXML).getValue();
    }


}
