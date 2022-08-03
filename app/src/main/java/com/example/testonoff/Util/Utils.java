package com.example.testonoff.Util;

import android.hardware.ConsumerIrManager;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.stream.Stream;

public class Utils {
    public static String POWER_ON = "PowerOn";
    public static String POWER_OFF = "PowerOff";
    public static String POWER = "Power";
    public static String FAN_AUTO = "FAN_AUTO";
    public static String FAN_HIGH = "FAN_HIGH";
    public static String FAN_MED = "FAN_MED";
    public static String FAN_LOW = "FAN_LOW";
    public static String MODE_COOL = "Mode_Cool";
    public static String MODE_HEAT = "Mode_Heat";
    public static String T_26 = "T_26";
    public static String T_25 = "T_25";
    public static String T_24 = "T_24";
    public static String T_23 = "T_23";
    public static String T_22 = "T_22";
    public static String T_21 = "T_21";
    public static String T_20 = "T_20";
    public static String T_19 = "T_19";
    public static String T_18 = "T_18";
    public static String T_17 = "T_17";
    public static String T_16 = "T_16";

    public static String SPEED_AUTO = "Speed Auto";
    public static String SPEED_HIGH = "Speed High";
    public static String SPEED_MEDIUM = "Speed Normal";
    public static String SPEED_LOW = "Speed Low";

    public static String MODE_HEAT_TEXT = "Heat";
    public static String MODE_COOLING_TEXT = "Cooling";


    public static boolean hasIrEmitter(ConsumerIrManager irManager) {
        return irManager.hasIrEmitter();
    }

    public static void sendDataIR(ConsumerIrManager irManager, int frequency, int[] pattern) {
        irManager.transmit(frequency, pattern);
    }

    public static int[] convertStringToArrayInt(String s) {
        String[] raw = s.replaceAll("\\s", "").split(",");
//        String[] parts = s.split(",");
//        int[] result = Stream.of(parts).mapToInt(Integer::parseInt).toArray()
        int[] results = new int[raw.length];
        for (int i = 0; i < raw.length; i++) {
            try {
                results[i] = Integer.parseInt(raw[i]);
            } catch (NumberFormatException nfe) {
            }
        }
        return results;
    }
}
