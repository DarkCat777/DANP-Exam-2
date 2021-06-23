package edu.unsa.exam2.service;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

public class MqttConstant {
    public static String clientId = "AndroidClient-57ee0171-de60-4e7a-86db-b2c58e2b034b";
    public static String mqttUsername = "username";
    public static String mqttPassword = "password";
    public static String mqttHost = "thingsboard.cloud";
    public static String mqttPort = "1883";
    public static String mqttTopic = "v1/devices/me/telemetry";
    public static Integer mqttQOS = 0;

    public static MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();

    static {
        mqttConnectOptions.setUserName(mqttUsername);
        mqttConnectOptions.setPassword(mqttPassword.toCharArray());
    }

}
