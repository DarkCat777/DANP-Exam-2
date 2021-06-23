package edu.unsa.exam2.service;

import android.util.Log;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttCallbackImpl implements MqttCallback {
    private String tag = MqttCallbackImpl.class.getName();

    @Override
    public void connectionLost(Throwable cause) {
        Log.i(tag, "connection lost");
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        Log.i(tag, "topic: " + topic + ", msg: " + message.getPayload().toString());
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        Log.i(tag, "msg delivered");
    }
}
