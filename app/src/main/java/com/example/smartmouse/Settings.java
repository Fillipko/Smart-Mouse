package com.example.smartmouse;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;

public class Settings extends AppCompatActivity {

    Switch debugToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ImageButton backBtn = findViewById(R.id.back_btn);
        debugToggle = findViewById(R.id.debug_console_switch);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                backToMouse();
            }
        });
    }

    public void backToMouse() {
        Intent intent = new Intent(this, MainActivity.class);
        if (debugToggle.isChecked()) {
            intent.putExtra("debug", VISIBLE);
        }
        else {
            intent.putExtra("debug", INVISIBLE);
        }
        startActivity(intent);
    }
}