package edu.unsa.exam2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.unsa.exam2.model.Filter;
import edu.unsa.exam2.model.StepsStorage;
import edu.unsa.exam2.service.MqttConfig;

public class MainActivity extends AppCompatActivity {

    SensorManager sensor_manager;
    Sensor acelerometer;
    double actualGeneralAceleration;
    SensorEventListener stepDetector;

    TextView tv_test;
    Button start_stop;
    Button exit;
    boolean on;

    MqttConfig mqtt_config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_test = (TextView) findViewById(R.id.label_test);
        start_stop = (Button) findViewById(R.id.bt_start_stop);
        exit = (Button) findViewById(R.id.bt_salir);

        sensor_manager = (SensorManager) getSystemService(SENSOR_SERVICE);
        acelerometer = sensor_manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mqtt_config = new MqttConfig(this.getApplicationContext());

        stepDetector = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event != null) {
                    actualGeneralAceleration = Filter.getGeneralAceleration(event);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        sensor_manager.registerListener(stepDetector, acelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        on = false;
    }

    public void startStopFunction(View v) {
        if (on) {
            pauseFunction();
            start_stop.setText("Iniciar");
        } else {
            resumeFunction();
            start_stop.setText("Detener");
        }
    }

    AsyncTask tarea;

    public void closeApp(View v) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void pauseFunction() {
        sensor_manager.unregisterListener(stepDetector);
        on = false;
        tarea.cancel(true);
        tarea = null;
        StepsStorage.setStepsCount(0);
    }

    public void resumeFunction() {
        sensor_manager.registerListener(stepDetector, acelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        tarea = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                while (on) {
                    publishProgress();
                    try {
                        Thread.sleep(500);
                        if (actualGeneralAceleration >= 10) {
                            StepsStorage.incrementSteps();
                            mqtt_config.publishMessage(StepsStorage.getStepsCount() + "");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(Object[] values) {
                super.onProgressUpdate(values);
                TextView acimut = findViewById(R.id.label_test);
                acimut.setText("Pasos: " + StepsStorage.getStepsCount());
            }
        };
        on = true;
        tarea.execute();
    }
}