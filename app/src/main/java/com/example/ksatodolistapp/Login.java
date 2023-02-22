package com.example.ksatodolistapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity {
    private Button LoginBtn;
    private EditText LoginNamePt;
    private EditText LoginPasswordPt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LoginBtn = findViewById(R.id.loginbtn);
        LoginNamePt = findViewById(R.id.editTextLoginName);
        LoginPasswordPt = findViewById(R.id.editTextPassword);
        LoginBtn.setOnClickListener((v) -> {
            Intent intent = new Intent(Login.this,MainActivity.class);
            startActivity(intent);
        });

    }
    private Boolean validateOrder(String Name,String Password){
        if (Name.length()==0){
            LoginNamePt.requestFocus();
            LoginNamePt.setError("Login Name field cannot be empty!");
            return false;
        }
        else if (Password.length()==0){
            LoginPasswordPt.requestFocus();
            LoginPasswordPt.setError("Login Password field cannot be empty!");
            return false;
        }
        else{
            return true;
        }
    }
}