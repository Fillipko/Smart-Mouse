package com.example.smartmouse;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    // Accelerometer Data
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private TextView xAccel, yAccel;
    private float x, y;

    ScrollView debuggingConsole;
    TextView debuggingLog;
    MouseBtn leftBtn;
    MouseBtn rightBtn;
    LinearLayout debugMenu;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences("preferences", 0);

        // Accelerometer
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        xAccel = findViewById(R.id.xAcceleration);
        yAccel = findViewById(R.id.yAcceleration);

        // UI elements
        leftBtn = findViewById(R.id.leftClickBtn);
        rightBtn = findViewById(R.id.rightClickBtn);
        ImageButton settingsBtn = findViewById(R.id.settings_btn);
        debuggingLog = findViewById(R.id.debug_text);
        debuggingConsole = findViewById(R.id.debug_console);
        debugMenu = findViewById(R.id.debugging_menu);

        setupMenu();

        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (preferences.getBoolean("invert", false)) {
                    rightClick();
                }
                else {
                    leftClick();
                }
            }
        });

        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (preferences.getBoolean("invert", false)) {
                    leftClick();
                }
                else {
                    rightClick();
                }
            }
        });

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettings();
            }
        });
    }

    public void openSettings() {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    private void consoleWrite(String msg) {
        String prevText = debuggingLog.getText().toString();
        debuggingLog.setText(prevText + msg + "\n");
        debuggingConsole.fullScroll(debuggingConsole.FOCUS_DOWN);
    }

    /*** Accelerometer Controls ***/

    //onResume() register the accelerometer for listening the events
    @Override
    protected void onResume() {
        super.onResume();
        setupMenu();
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
            xAccel.setText(String.format(Float.toString(x)));
        }
        if (Math.abs(y) > 0.2) {
            yAccel.setText(String.format(Float.toString(y)));
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void setupMenu() {
        if (preferences != null) {
            debugMenu.setVisibility(preferences.getInt("debug", View.GONE));
            rightBtn.setBackgroundTintList(getColorStateList(preferences.getInt("color", R.color.blue)));
            leftBtn.setBackgroundTintList(getColorStateList(preferences.getInt("color", R.color.blue)));
            rightBtn.setHapticFeedbackEnabled(preferences.getBoolean("haptics", false));
            leftBtn.setHapticFeedbackEnabled(preferences.getBoolean("haptics", false));
        }
    }

    public void leftClick() {
        consoleWrite("Left button pressed");
    }

    public void rightClick() {
        consoleWrite("Right button pressed");
    }
}