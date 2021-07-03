package edu.unsa.exam2.service;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

public class MqttConstant {
    public static String clientId = "AndroidClient"; // JavaClient
    public static String mqttUsername = "android"; // java
    public static String mqttPassword = "12345678"; // 12345678
    public static String mqttHost = "node02.myqtthub.com";
    public static Integer mqttPort = 1883;
    public static String sensorName = "acelerometerSensor";
    public static String mqttTopic = "unsa/devices/android/" + sensorName;
    public static Integer mqttQOS = 0;

    public static MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();

    static {
        mqttConnectOptions.setUserName(mqttUsername);
        mqttConnectOptions.setPassword(mqttPassword.toCharArray());
    }

}
