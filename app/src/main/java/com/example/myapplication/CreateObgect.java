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
    Button add, delete, defects, update, test;
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
        defects = (Button)findViewById(R.id.btn_show);
        update = (Button)findViewById(R.id.btn_update);
        delete = (Button)findViewById(R.id.btn_delete);

        test = (Button)findViewById(R.id.btn_test);

        if(getIntent().getStringExtra("ITEM_ID") != null){
            LoadItemData();
        }
        AddData();
        updateData();
        Delete();
        Read();

        defects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
                startActivity(intent);
            }
        });

    }


    public void LoadItemData(){
        String targetId = getIntent().getStringExtra("ITEM_ID");

        Cursor res = myDb.getDataById(targetId);
        if(res.getCount() == 0) {
            Toast.makeText(this, "ID " + targetId + " не найден", Toast.LENGTH_SHORT).show();
            res.close();
            return;
        }

        if(res.moveToFirst()) {
            id.setText(res.getString(0));
            name.setText(res.getString(1));
            goal.setText(res.getString(2));
        }
        res.close();
    }


    public void goToLayoutMain(View view) {
        finish(); // Закрыть CreateObgect → покажет MainActivity
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
                            goToLayoutMain(v);
                        } else{
                            Toast.makeText(com.example.myapplication.CreateObgect.this, "Not Deleted", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }







}

