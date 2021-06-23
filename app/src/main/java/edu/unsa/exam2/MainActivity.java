package edu.unsa.exam2;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.gson.Gson;
import edu.unsa.exam2.service.MqttConfig;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Example of use
//        new MqttConfig(this.getApplicationContext()).publishMessage(
//                new Gson().toJson(object)
//        );
    }
}