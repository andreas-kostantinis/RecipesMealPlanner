package com.example.tablayout;

import static com.example.tablayout.SharedPreferenceManager.TOKEN;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class Launcher extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        SharedPreferenceManager.setStringValue(this, "hello" , TOKEN);
        startActivity(new Intent(this ,
                (SharedPreferenceManager.getStringValue(this,TOKEN) == null) ? Login.class : MainActivity.class ));
        finish();



    }
}