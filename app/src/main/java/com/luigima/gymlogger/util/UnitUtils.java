package com.luigima.gymlogger.util;

/**
 * Created by Luke on 19.03.2015.
 * 5 Feet 3 Inches=5'3"=5.30(IMPERAL) = 159 (METRIC) cm
 */
public final class UnitUtils {

    public enum Unit {
        METRIC, IMPERAL
    }

    private static Unit Unit_SYSTEM = Unit.METRIC;
    private static float height = 185;
    private static float weight = 75;

    private UnitUtils(){}

    public static Unit getUnit_system() {
        return Unit_SYSTEM;
    }

    public static synchronized void setUnit_system(Unit unit_system) {
        UnitUtils.Unit_SYSTEM = unit_system;

        float feet, inches;
        // Conversion
        switch (unit_system){
            case METRIC:
                // the values are IMPERAL atm
                //e.g 4.115 => 4 feet and 11.5 inch
                height = height_imperal2metric(height);
                weight = weight_imperal2metric(weight);
                break;

            case IMPERAL:
                height = heigth_metric2imperal(height);
                weight = weight_metric2imperal(weight);
                break;
        }
    }

    public static float height_imperal2metric(float value) {
        float res, feet, inches;
        //e.g 4.115 => 4 feet and 11.5 inch
        feet = Math.round(value - 0.5f); // -0,5 -> truncate, do not round! (happily it never happens)
        inches = (value - feet) * 100f;
        res = feet * 30.48f;
        res += inches * 2.54f;
        res = Math.round(res * 1000f) / 1000f;

        return res;
    }

    public static float weight_imperal2metric(float value) {
        return  Math.round((value / 2.2046f) * 100f) / 100f;
    }

    public static float heigth_metric2imperal(float value) {
        float feet, inches;

        feet = value * 0.032808f;
        inches = (value/2.54f) - ((int)feet * 12);

        float res = (int) feet + (inches)/100f;
        // 2-3 digits precision
        res = Math.round(res * 1000f) / 1000f;

        return res;
    }

    public static float weight_metric2imperal(float value) {
        return Math.round(value * 2.2046f * 100f) / 100f;
    }

    public static float getHeight() {
        return height;
    }

    public static void setHeight(float height) {
        // Checks the input format
        UnitUtils.height = height;
    }

    public static float getWeight() {
        return weight;
    }

    public static void setWeight(float weight) {
        UnitUtils.weight = weight;
    }
}