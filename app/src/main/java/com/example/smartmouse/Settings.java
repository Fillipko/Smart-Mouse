package com.example.smartmouse;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;

public class Settings extends AppCompatActivity {

    ImageButton backBtn;
    Switch hapticsToggle, debugToggle;
    Button blueBtn, greenBtn, purpleBtn, yellowBtn, redBtn, grayBtn;
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Load preferences
        preferences = getSharedPreferences("preferences", 0);
        prefEditor = preferences.edit();

        // UI elements
        backBtn = findViewById(R.id.back_btn);
        hapticsToggle = findViewById(R.id.haptics_switch);
        debugToggle = findViewById(R.id.debug_console_switch);
        blueBtn = findViewById(R.id.blue);
        greenBtn = findViewById(R.id.green);
        purpleBtn = findViewById(R.id.purple);
        yellowBtn = findViewById(R.id.yellow);
        redBtn = findViewById(R.id.red);
        grayBtn = findViewById(R.id.gray);

        // Sets up default settings from last session
        setupMenu();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefEditor.commit();
                finish();
            }
        });

        debugToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (debugToggle.isChecked()) {
                    prefEditor.putInt("debug", VISIBLE);
                }
                else {
                    prefEditor.putInt("debug", GONE);
                }
                prefEditor.putBoolean("debug_toggle", debugToggle.isChecked());
            }
        });

        hapticsToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefEditor.putBoolean("haptics", hapticsToggle.isChecked());
                prefEditor.putBoolean("haptics_toggle", hapticsToggle.isChecked());
            }
        });

        /*** Color button callbacks ***/
        blueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefEditor.putInt("color", R.color.blue);
            }
        });

        greenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefEditor.putInt("color", R.color.green);
            }
        });

        purpleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefEditor.putInt("color", R.color.purple);
            }
        });

        yellowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefEditor.putInt("color", R.color.yellow);
            }
        });

        redBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefEditor.putInt("color", R.color.red);
            }
        });

        grayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefEditor.putInt("color", R.color.gray);
            }
        });
    }

    public void setupMenu() {
        debugToggle.setChecked(preferences.getBoolean("debug_toggle", false));
        hapticsToggle.setChecked(preferences.getBoolean("haptics_toggle", false));
    }
}