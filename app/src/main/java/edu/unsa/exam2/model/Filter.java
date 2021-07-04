package edu.unsa.exam2.model;

import android.hardware.SensorEvent;

public class Filter {
    static float alpha= (float) 0.8;
    static float previous_values[];
    public static double getGeneralAceleration(SensorEvent aceleration_event){

        if(previous_values==null){
            previous_values=new float[3];
            previous_values[0]=aceleration_event.values[0];
            previous_values[1]=aceleration_event.values[1];
            previous_values[2]=aceleration_event.values[2];
        }

        float linear_aceleration[]=new float[3];
        previous_values[0] = alpha * previous_values[0] + (1 - alpha) * aceleration_event.values[0];
        previous_values[1] = alpha * previous_values[1] + (1 - alpha) * aceleration_event.values[1];
        previous_values[2] = alpha * previous_values[2] + (1 - alpha) * aceleration_event.values[2];

        linear_aceleration[0] = aceleration_event.values[0] - previous_values[0];
        linear_aceleration[1] = aceleration_event.values[1] - previous_values[1];
        linear_aceleration[2] = aceleration_event.values[2] - previous_values[2];

        return Math.sqrt(Math.pow(linear_aceleration[0],2)+Math.pow(linear_aceleration[1],2)+Math.pow(linear_aceleration[2],2));
    }
}
