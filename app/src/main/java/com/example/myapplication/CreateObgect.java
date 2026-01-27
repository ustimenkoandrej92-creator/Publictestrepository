package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class  CreateObgect extends AppCompatActivity {
    EditText name, goal, id;
    Button add, delete, show, update, test;
    DatabaseHealper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_object);
        myDb = new DatabaseHealper(this);

        name = (EditText)findViewById(R.id.name);
        goal =(EditText)findViewById(R.id.goal);
        id =(EditText)findViewById(R.id.id);

        add = (Button)findViewById(R.id.btn_add);
        show = (Button)findViewById(R.id.btn_show);
        update = (Button)findViewById(R.id.btn_update);
        delete = (Button)findViewById(R.id.btn_delete);

        test = (Button)findViewById(R.id.btn_test);

        AddData();
        ShowAll();
        updateData();
        Delete();
        Read();
    }

    public void goBack(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }
    public void Read(){
        test.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String targetId = id.getText().toString().trim();
                        Cursor res = myDb.getDataById(targetId);
                        if(res.getCount() == 0) {
                            ShowMassage("Error", "ID " + targetId + " не найден");
                            return;
                        }

                        if(res.moveToFirst()) {
                            id.setText(res.getString(0));
                            name.setText(res.getString(1));
                            goal.setText(res.getString(2));
                        }
                        res.close();
                    }
                }
        );
    }

    public void AddData(){
        add.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isInserted = myDb.insertData(name.getText().toString(), goal.getText().toString());
                        if(isInserted == true){
                            Toast.makeText(com.example.myapplication.CreateObgect.this, "Saved", Toast.LENGTH_LONG).show();
                        } else{
                            Toast.makeText(com.example.myapplication.CreateObgect.this, "Not saved", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }


    public void ShowAll(){
        show.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Cursor res = myDb.getAllData();
                        if(res.getCount() == 0){
                            ShowMassage("Error", "Nothing found");
                            return;
                        }

                        StringBuffer stringBuffer = new StringBuffer();
                        while (res.moveToNext()){
                            stringBuffer.append("Id :"+ res.getString(0) + "\n");
                            stringBuffer.append("Name :"+ res.getString(1) + "\n");
                            stringBuffer.append("Email :"+ res.getString(2) + "\n\n");
                        }

                        ShowMassage("Data", stringBuffer.toString());
                    }
                }
        );
    }



    public void ShowMassage(String title, String massage){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(massage);
        builder.show();
    }



    public void updateData(){
        update.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isUpdated = myDb.updataData(id.getText().toString(), name.getText().toString(), goal.getText().toString());

                        if(isUpdated){
                            Toast.makeText(com.example.myapplication.CreateObgect.this, "Updated", Toast.LENGTH_LONG).show();
                        } else{
                            Toast.makeText(com.example.myapplication.CreateObgect.this, "Not Updated", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }

    public void Delete(){
        delete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Integer isDeleted = myDb.deleteData(id.getText().toString());

                        if(isDeleted > 0){
                            Toast.makeText(com.example.myapplication.CreateObgect.this, "Deleted", Toast.LENGTH_LONG).show();
                        } else{
                            Toast.makeText(com.example.myapplication.CreateObgect.this, "Not Deleted", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }



}

