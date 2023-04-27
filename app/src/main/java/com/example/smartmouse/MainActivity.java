package com.example.smartmouse;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    // Accelerometer Data
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private TextView xAccel, yAccel;
    private float x, y;

    // UI Elements
    ScrollView debuggingConsole;
    TextView debuggingLog;
    Button leftBtn;
    Button rightBtn;
    LinearLayout debugMenu;

    // Network variables
    private Socket client;
    DataOutputStream DOS;
    private PrintWriter printwriter;
    private String message;

    // Saved preferences
    SharedPreferences preferences;

    @SuppressLint("ClickableViewAccessibility")
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

        leftBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (preferences.getBoolean("invert", false)) {
                    rightClick(v, event);
                }
                else {
                    leftClick(v, event);
                }
                return true;
            }
        });

        rightBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (preferences.getBoolean("invert", false)) {
                    leftClick(v, event);
                }
                else {
                    rightClick(v, event);
                }
                return true;
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
        x = event.values[0] + (float)(preferences.getInt("sensitivity", 0)) / 10;
        y = event.values[1] + (float)(preferences.getInt("sensitivity", 0)) / 10;
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

    // Button click actions
    public void leftClick(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                consoleWrite("left button pressed");
                break;
            case MotionEvent.ACTION_UP:
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                consoleWrite("left button released");
                break;
        }
    }

    public void rightClick(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                consoleWrite("right button pressed");
                break;
            case MotionEvent.ACTION_UP:
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                consoleWrite("right button released");
                break;
        }
    }

    // The ClientThread class performs the networking operations
    class ClientThread implements Runnable {
        private final String message;

        ClientThread(String message) {
            this.message = message;
        }
        @Override
        public void run() {
            try {
                // the IP and port should be correct to have a connection established
                // Creates a stream socket and connects it to the specified port number on the named host.
                client = new Socket("192.168.110.32", 4444);  // connect to server
                printwriter = new PrintWriter(client.getOutputStream(),true);
                printwriter.write(message);  // write the message to output stream

                printwriter.flush();
                printwriter.close();

                // closing the connection
                client.close();

            } catch (IOException e) {
                consoleWrite("network error");
                e.printStackTrace();
            }

            // updating the UI
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });
        }
    }
}
