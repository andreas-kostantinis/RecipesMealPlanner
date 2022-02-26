package com.example.tablayout.activities;

import static com.example.tablayout.SharedPreferenceManager.TOKEN;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tablayout.R;
import com.example.tablayout.SharedPreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {


    EditText editTextEmail, editTextPassword;
    Button btn_login;
    FirebaseAuth mAuth;
    FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.passww);
        btn_login = findViewById(R.id.button_login);

        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();



        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                String passw = editTextPassword.getText().toString();

                if(email.isEmpty()){
                    editTextEmail.setError("Email is required");
                    editTextEmail.requestFocus();

                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    editTextEmail.setError("Email is required");
                    editTextEmail.requestFocus();

                }
                if(passw.isEmpty()){
                    editTextPassword.setError("Please provide password");
                    editTextPassword.requestFocus();
                }
                if(passw.length()<6){
                    editTextPassword.setError("Please provide password");
                    editTextPassword.requestFocus();

                }

                mAuth.signInWithEmailAndPassword(email, passw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            String idToken = task.getResult().getUser().getIdToken(true).toString();
                            SharedPreferenceManager.setStringValue(LoginActivity.this,idToken,TOKEN);
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();

                        }else{
                            Toast.makeText(LoginActivity.this, "failed to log in!", Toast.LENGTH_SHORT).show();
                            SharedPreferenceManager.setStringValue(LoginActivity.this, null, TOKEN);
                        }


                    }
                });

            }




        });





    }
}