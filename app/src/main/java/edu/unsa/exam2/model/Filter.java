package edu.unsa.exam2.model;

import android.hardware.SensorEvent;

public class Filter {
    static float alpha = (float) 0.8;

    public static double getGeneralAceleration(SensorEvent aceleration_event) {

        float gravity[] = new float[3], linear_aceleration[] = new float[3];
        gravity[0] = alpha * gravity[0] + (1 - alpha) * aceleration_event.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * aceleration_event.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * aceleration_event.values[2];

        linear_aceleration[0] = aceleration_event.values[0] - gravity[0];
        linear_aceleration[1] = aceleration_event.values[1] - gravity[1];
        linear_aceleration[2] = aceleration_event.values[2] - gravity[2];

        return Math.sqrt(Math.pow(linear_aceleration[0], 2) + Math.pow(linear_aceleration[1], 2) + Math.pow(linear_aceleration[2], 2));
    }
}
