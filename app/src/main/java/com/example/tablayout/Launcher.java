package com.example.tablayout;

import static com.example.tablayout.SharedPreferenceManager.TOKEN;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class Launcher extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);


        startActivity(new Intent(this ,
                (SharedPreferenceManager.getStringValue(this,TOKEN) == null) ? LoginActivity.class : MainActivity.class ));
        finish();

        
    }
}