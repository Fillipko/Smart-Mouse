package com.example.smartmouse;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private TextView xAccel, yAccel;
    private float x, y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        xAccel = findViewById(R.id.xAcceleration);
        yAccel = findViewById(R.id.yAcceleration);
    }

    //onResume() register the accelerometer for listening the events
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener((SensorEventListener) this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    //onPause() unregister the accelerometer for stop listening the events
    //(Helps avoid using battery when mouse is not being used)
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener((SensorEventListener) this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void onSensorChanged(SensorEvent event) {
        x = event.values[0];
        y = event.values[1];
        //Tolerance of 0.2 to start moving cursor
        if (Math.abs(x) > 0.2) {
            xAccel.setText((Float.toString(x)));
        }
        if (Math.abs(y) > 0.2) {
            yAccel.setText((Float.toString(y)));
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}