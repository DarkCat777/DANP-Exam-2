package edu.unsa.exam2.service;

import android.content.Context;
import android.util.Log;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import static edu.unsa.exam2.service.MqttConstant.*;

public class MqttConfig {
    private Context context;
    private String tag = MqttConfig.class.getName();
    private MqttAndroidClient mqttAndroidClient;

    public MqttConfig(Context context) {
        this.context = context;
        this.mqttAndroidClient = new MqttAndroidClient(this.context, "tcp://" + mqttHost + ":" + mqttPort, clientId);
    }

    public void publishMessage(String message) {
        try {
            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.i(tag, "Connect succeed");
                    try {
                        if (!mqttAndroidClient.isConnected()) {
                            mqttAndroidClient.connect();
                        }
                        MqttMessage mqttMessage = new MqttMessage();
                        mqttMessage.setPayload(message.getBytes());
                        mqttMessage.setQos(mqttQOS);
                        mqttAndroidClient.publish(mqttTopic, mqttMessage, null, new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                Log.i(tag, "Publish succeed!");
                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                Log.i(tag, "Publish failed");
                            }
                        });
                    } catch (MqttException e) {
                        Log.e(tag, e.toString());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.i(tag, "Connect failed");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
