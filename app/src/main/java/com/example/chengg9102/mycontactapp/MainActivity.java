package com.example.chengg9102.mycontactapp;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper myDB;
    EditText editName;
    EditText editPhone;
    EditText editAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editName = findViewById(R.id.editText_name);
        editPhone = findViewById(R.id.editText_phone);
        editAddress = findViewById(R.id.editText_address);

        myDB = new DatabaseHelper(this);
        Log.d("MyContactApp","MainActivity: instantiated myDB");
    }

    public void addData(View view){
        Log.d("MyContactApp","MainActivity: Add contact button pressed");

        boolean isInserted = myDB.insertData(editName.getText().toString(),editPhone.getText().toString(),editAddress.getText().toString());

        if(isInserted){
            Toast.makeText(MainActivity.this,"Sucess - contact inserted",Toast.LENGTH_LONG);
          }
          else{
            Toast.makeText(MainActivity.this,"FAILED - contact inserted",Toast.LENGTH_LONG);
        }
    }
    public void viewData (View view){
        Cursor res = myDB.getAllData();
        Log.d("MyContactApp","MainActivity: viewData: received cursor");

        if(res.getCount() == 0){
            showMessage("Error","No data found in database");
            return;
        }

        StringBuffer buffer = new StringBuffer();
        while(res.moveToNext()){
            //append res column 0,1,2,3 to the buffer - see StringBuffer and Cursor api's
            //Delimit each of the "appends" with line feed "\n"

            buffer.append("Name = " + res.getString(1) + "\n Phone: " + res.getString(2) + "\n Address: " + res.getString(3)+ "\n" + "\n");

        }

        showMessage("Data",buffer.toString());

    }

    private void showMessage(String title, String message){
        Log.d("MyContactApp","MainActivity: showMessage: assembling AlertDialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public static final String EXTRA_MESSAGE = "com.example.chengg9102.mycontactapp_p2.MESSAGE";

    public void searchRecord(View view){
        Log.d("MyContactApp", "MainActivity: launching SearchActivity");
        Intent intent = new Intent(this,SearchActivity.class);

        Cursor res = myDB.getAllData();
        Log.d("MyContactApp", "MainActivity: viewData: received cursor");

        if(res.getCount() == 0){
            showMessage("Error","No data found in database");
            return;
        }

        StringBuffer s = new StringBuffer();
        while(res.moveToNext()){
            if(editName.getText().toString().equals(res.getString(1))){
                s.append( "\n Name: " + res.getString(1)+"\n Phone: " + res.getString(2)
                        + "\n Address: " + res.getString(3) + "\n" + "\n");
            }

        }
        if(s.toString() == ""){
            s.append("No entries found");
        }

        intent.putExtra(EXTRA_MESSAGE, s.toString());
        startActivity(intent);
    }

}