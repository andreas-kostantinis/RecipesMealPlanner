package com.example.tablayout.activities;

import static com.example.tablayout.utils.SharedPreferenceManager.TOKEN;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tablayout.R;
import com.example.tablayout.utils.SharedPreferenceManager;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        startActivity(new Intent(this ,
                (SharedPreferenceManager.getStringValue(this,TOKEN) == null) ? LoginActivity.class : MainActivity.class ));
        finish();
    }

}