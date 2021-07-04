package edu.unsa.exam2.model;

import android.hardware.SensorEvent;

public class FilterHighPass {
    static float alpha= (float) 0.8;
    static float [] previous_values;
    static float [] high_pass_values;

    public static double getGeneralAceleration(SensorEvent aceleration_event){
        float x=aceleration_event.values[0];
        float y=aceleration_event.values[1];
        float z=aceleration_event.values[2];

        if(previous_values==null){
            previous_values=new float[3];
            previous_values[0]=x;
            previous_values[1]=y;
            previous_values[2]=z;
            if(high_pass_values==null){
                high_pass_values=new float[3];
                high_pass_values[0]=x;
                high_pass_values[1]=y;
                high_pass_values[2]=z;
            }
        }

        high_pass_values[0]=highPass(x,previous_values[0],high_pass_values[0]);
        high_pass_values[1]=highPass(y,previous_values[1],high_pass_values[1]);
        high_pass_values[2]=highPass(z,previous_values[2],high_pass_values[2]);

        previous_values[0]=x;
        previous_values[1]=y;
        previous_values[2]=z;

        return Math.sqrt(Math.pow(high_pass_values[0],2)+Math.pow(high_pass_values[1],2)+Math.pow(high_pass_values[2],2));
    }
    private static float highPass(float current,float last, float filtered){
        return alpha * (filtered+current-last);
    }
}
