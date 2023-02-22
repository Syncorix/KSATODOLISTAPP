package com.example.ksatodolistapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ViewList extends AppCompatActivity implements View.OnClickListener{
    Button AddTaskBtn, SaveBtn, DeleteBtn;
    EditText ListNameText, TaskNameText;
    LinearLayout TaskLayout;
    SharedPreferences saveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewlist);

        AddTaskBtn = findViewById(R.id.addTaskBtn);
        SaveBtn = findViewById(R.id.saveBtn);
        DeleteBtn = findViewById(R.id.deleteBtn);
        ListNameText = findViewById(R.id.listNameText);
        TaskNameText = findViewById(R.id.taskNameText);
        TaskLayout = findViewById(R.id.taskLayout);

        AddTaskBtn.setOnClickListener(this);
        SaveBtn.setOnClickListener(this);
        DeleteBtn.setOnClickListener(this);


        saveData = getSharedPreferences("saveData", MODE_PRIVATE);

        if(saveData.getInt("SelectedList", 0) == 0){
            ListNameText.setText("New List");
        }
        else{
            //Grabbing lists
            String listString = saveData.getString("Lists", "");
            String[] listArray = listString.split("[|]");
            ListNameText.setText(listArray[saveData.getInt("SelectedList", 0)]);

            //adding existing tasks
            String taskString = saveData.getString(ListNameText.getText().toString() + "Tasks", "");
            String[] taskArray = taskString.split("[|]");

            //creating layout

            for (int i = 0; i < taskArray.length - 1; i++) {
                LinearLayout newLayout = new LinearLayout(this);
                newLayout.setOrientation(LinearLayout.HORIZONTAL);
                TextView newTextView = new TextView(this);
                newTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
                newTextView.setText(taskArray[i]);
                newLayout.addView(newTextView);

                i++;

                CheckBox newCheckbox = new CheckBox(this);

                if(taskArray[i].equals("true")){
                    newCheckbox.setChecked(true);
                }

                newLayout.addView(newCheckbox);

                Button newButton = new Button(this);
                newButton.setText("Remove");

                newButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TaskLayout.removeView((View) v.getParent());
                    }
                });
                newLayout.addView(newButton);
                TaskLayout.addView(newLayout);
            }
        }
        SharedPreferences.Editor notepad = saveData.edit();
        notepad.putString("CurrentName", ListNameText.getText().toString());
        notepad.commit();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.addTaskBtn:
                LinearLayout newLayout = new LinearLayout(this);
                newLayout.setOrientation(LinearLayout.HORIZONTAL);

                TextView newTextView = new TextView(this);
                newTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
                newTextView.setText(TaskNameText.getText());
                newLayout.addView(newTextView);

                CheckBox newCheckbox = new CheckBox(this);

                newLayout.addView(newCheckbox);

                Button newButton = new Button(this);
                newButton.setText("Remove");

                newButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TaskLayout.removeView((View) v.getParent());
                    }
                });
                newLayout.addView(newButton);
                TaskLayout.addView(newLayout);


                break;

            case R.id.saveBtn:
                String taskString = "";
                //iterating through nested linear layouts
                for (int i = 2; i < TaskLayout.getChildCount(); i++) {
                    View v = TaskLayout.getChildAt(i);
                    for (int j = 0; j < 3; j++){
                        View l = ((LinearLayout) v).getChildAt(j);

                        if((j % 3) == 0){
                            taskString += (((TextView) l).getText().toString()) + "|";

                        }
                        else if ((j % 3) == 1){
                            if (((CheckBox) l).isChecked()) {
                                taskString += ("true") + "|";
                            }
                            else {
                                taskString += ("false") + "|";
                            }

                        }
                    }
                }
                String listString = saveData.getString("Lists", "");
                String[] listArray = listString.split("[|]");


                if (listArray[saveData.getInt("SelectedList", 0)].equals(ListNameText.getText().toString())) {
                    //this code updates an existing list with the same name
                    SharedPreferences.Editor notepad = saveData.edit();

                    notepad.putString(ListNameText.getText().toString() + "Tasks", taskString);
                    notepad.commit();

                } else if (listArray[saveData.getInt("SelectedList", 0)].equals(saveData.getString("CurrentName", ""))) {
                    //This code updates an existing list with a new name

                    //Removing tasks that reference old list name
                    SharedPreferences.Editor notepad = saveData.edit();

                    notepad.remove(listArray[saveData.getInt("SelectedList", 0)] + "Tasks");

                    //Updating list name
                    listArray[saveData.getInt("SelectedList", 0)] = ListNameText.getText().toString();
                    String newListString = "";
                    for (int i = 0; i < listArray.length; i++){
                        newListString += listArray[i] + "|";
                    }
                    notepad.putString("Lists", newListString);
                    //Adding tasks that reference new list name
                    notepad.putString(ListNameText.getText().toString() + "Tasks", taskString);
                    notepad.commit();

                } else {
                    //this code create a new list

                    //updating list of lists
                    SharedPreferences.Editor notepad = saveData.edit();
                    String tempTempListString = saveData.getString("Lists", "");

                    tempTempListString += ListNameText.getText().toString() + "|";
                    notepad.putString("Lists", tempTempListString);

                    //adding tasks
                    notepad.putString(ListNameText.getText().toString() + "Tasks", taskString);
                    notepad.commit();

                }

                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                finish();

                break;

            case R.id.deleteBtn:
                //removes tasks
                String tempListString = saveData.getString("Lists", "");
                String[] listArray2 = tempListString.split("[|]");
                SharedPreferences.Editor notepad = saveData.edit();
                notepad.remove(listArray2[saveData.getInt("SelectedList", 0)] + "Tasks");
                //removes the deleted list from the list of lists
                ArrayList<String> tempList = new ArrayList<>();
                for (int j = 0; j < listArray2.length - 1; j++){
                    if(!listArray2[j].equals(listArray2[saveData.getInt("SelectedList", 0)])){
                        tempList.add(listArray2[j]);
                    }
                }
                tempListString = "";
                for (int k = 0; k < tempList.size(); k++){
                    tempListString += tempList.get(k) + "|";
                }
                notepad.putString("Lists", tempListString);
                notepad.commit();


                Intent j = new Intent(this, MainActivity.class);
                startActivity(j);
                finish();
                break;
        }
    }
}
