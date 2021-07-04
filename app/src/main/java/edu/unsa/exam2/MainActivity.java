package edu.unsa.exam2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import edu.unsa.exam2.model.Filter;
import edu.unsa.exam2.model.FilterHighPass;
import edu.unsa.exam2.model.StepsStorage;
import edu.unsa.exam2.service.MqttConfig;

public class MainActivity extends AppCompatActivity {

    SensorManager sensor_manager;
    Sensor acelerometer;
    double actualGeneralAceleration;
    SensorEventListener stepDetector;

    ProgressBar progressBarA;
    ProgressBar progressBarS;
    private int progressStatus = 0;
    private Handler handler = new Handler();

    TextView tv_test_a;
    TextView tv_test_s;
    Button start_stop_a, start_stop_s;
    Button exit;
    Button restart;
    boolean on;

    MqttConfig mqtt_config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_test_a = (TextView) findViewById(R.id.label_test_a);
        tv_test_s = (TextView) findViewById(R.id.label_test_s);
        start_stop_a = (Button) findViewById(R.id.bt_start_stop_a);
        start_stop_s = (Button) findViewById(R.id.bt_start_stop_s);
        restart = (Button) findViewById(R.id.reiniciar);
        exit = (Button) findViewById(R.id.bt_salir);
        progressBarA = (ProgressBar) findViewById(R.id.progressBarA);
        progressBarS = (ProgressBar) findViewById(R.id.progressBarS);


        sensor_manager = (SensorManager) getSystemService(SENSOR_SERVICE);
        acelerometer = sensor_manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mqtt_config = new MqttConfig(this.getApplicationContext());

        stepDetector = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event != null) {
                    //actualGeneralAceleration = Filter.getGeneralAceleration(event);
                    actualGeneralAceleration= FilterHighPass.getGeneralAceleration(event);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        sensor_manager.registerListener(stepDetector, acelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        on = false;
    }
    public void restartValues(View v){
        tv_test_a.setText(0+"");
        tv_test_s.setText(0+"");
        progressBarA.setProgress(0);
        progressBarS.setProgress(0);
    }

    public void startStopFunctionAcel(View v) {
        if (on) {
            pauseFunction();
            start_stop_a.setText("Iniciar\nAcelerometro");
        } else {
            resumeFunction();
            start_stop_a.setText("Detener\nAcelerometro");
        }
    }
    public void startStopFunctionSen(View v) {
        if (on) {
            pauseFunction();
            start_stop_s.setText("Iniciar\nSensor");
        } else {
            resumeFunction();
            start_stop_s.setText("Detener\nSensor");
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
                        if (actualGeneralAceleration >= 4) {
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
                TextView acimut = findViewById(R.id.label_test_a);
                acimut.setText(StepsStorage.getStepsCount()+"");
                progressBarA.setProgress(StepsStorage.getStepsCount());

            }
        };
        on = true;
        tarea.execute();
    }
}