package com.example.ksatodolistapp;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    Button EnterBtn;
    Spinner ListSpin;
    SharedPreferences saveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EnterBtn = findViewById(R.id.enterBtn);
        ListSpin = findViewById(R.id.listSpinner);

        EnterBtn.setOnClickListener(this);
        ListSpin.setOnItemSelectedListener(this);


        saveData = getSharedPreferences("saveData", MODE_PRIVATE);

        //this generates the create new list option if it doesn't already exist
        if(!saveData.contains("Lists")){
            String listString = "*Create New List*|";
            SharedPreferences.Editor notepad = saveData.edit();
            notepad.putString("Lists", listString);
            notepad.commit();
        }

        //setting spinner values
        String listString = saveData.getString("Lists", "");
        String[] listArray = listString.split("[|]");
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listArray);
        listAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ListSpin.setAdapter(listAdapter);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.enterBtn:
                Intent i = new Intent(this, ViewList.class);
                startActivity(i);
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        SharedPreferences.Editor notepad = saveData.edit();
        notepad.putInt("SelectedList", ListSpin.getSelectedItemPosition());
        notepad.commit();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}