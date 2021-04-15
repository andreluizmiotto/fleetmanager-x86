package br.com.fleetmanager.utils;

import javafx.scene.image.ImageView;

public class Functions {

    public static boolean isNotNull(String value) {
        return ((value != null) && (!value.isBlank()));
    }

    public static String formatMonetary(final String pValue, final int pDigit) {
        return String.valueOf(formatMonetary(Double.parseDouble(pValue), pDigit));
    }

    public static double formatMonetary(final double pValue, final int digit) {
        double result = pValue * 10;
        result += digit * 0.01;
        return result;
    }

    public static ImageView getImageView(Class<?> pClass, String pImageName) {
        return new ImageView(pClass.getResource(Constants.sIconsFolder) + pImageName);
    }

}
