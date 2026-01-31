package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class CreateDefect extends AppCompatActivity {
    EditText type, place, id;
    Button add2, delete2, update2, test2, goToPhoto;
    DatabaseHealper myDb;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_defect);
        myDb = new DatabaseHealper(this);

        type = (EditText)findViewById(R.id.type);
        place =(EditText)findViewById(R.id.place);
        //int id = ;

        add2 = (Button)findViewById(R.id.btn_add2);
        update2 = (Button)findViewById(R.id.btn_update2);
        delete2 = (Button)findViewById(R.id.btn_delete2);

        test2 = (Button)findViewById(R.id.btn_test2);

        goToPhoto = findViewById(R.id.btn_photo);


        if(getIntent().getStringExtra("ITEM_ID") != null){
            LoadItemData();
        }
        AddData();
        updateData();
        Delete();
        Read();

        goToPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateDefect.this, CreatePhoto.class);
                startActivity(intent);
            }
        });

    }



    public void LoadItemData(){
        String targetId = getIntent().getStringExtra("ITEM_ID");

        Cursor res = myDb.getDataById2(targetId);
        if(res.getCount() == 0) {
            Toast.makeText(this, "ID " + targetId + " не найден", Toast.LENGTH_SHORT).show();
            res.close();
            return;
        }

        if(res.moveToFirst()) {
            //id.setText(res.getString(0));
            place.setText(res.getString(1));
            place.setText(res.getString(2));
        }
        res.close();
    }


    public void goToLayoutMain2(View view) {
        finish(); // Закрыть CreateObgect → покажет MainActivity
    }
    public void goBack(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }
    public void Read(){
        test2.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String targetId = id.getText().toString().trim();
                        Cursor res = myDb.getDataById2(targetId);
                        if(res.getCount() == 0) {
                            ShowMassage("Error", "ID " + targetId + " не найден");
                            return;
                        }

                        if(res.moveToFirst()) {
                            id.setText(res.getString(0));
                            type.setText(res.getString(1));
                            place.setText(res.getString(2));
                        }
                        res.close();
                    }
                }
        );

    }

    public void AddData(){
        add2.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isInserted = myDb.insertData2(type.getText().toString(), place.getText().toString());
                        if(isInserted == true){
                            Toast.makeText(com.example.myapplication.CreateDefect.this, "Saved", Toast.LENGTH_LONG).show();
                        } else{
                            Toast.makeText(com.example.myapplication.CreateDefect.this, "Not saved", Toast.LENGTH_LONG).show();
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
        update2.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isUpdated = myDb.updataData2(id.getText().toString(), type.getText().toString(), place.getText().toString());

                        if(isUpdated){
                            Toast.makeText(com.example.myapplication.CreateDefect.this, "Updated", Toast.LENGTH_LONG).show();
                        } else{
                            Toast.makeText(com.example.myapplication.CreateDefect.this, "Not Updated", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }

    public void Delete(){
        delete2.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Integer isDeleted = myDb.deleteData2(id.getText().toString());

                        if(isDeleted > 0){
                            Toast.makeText(com.example.myapplication.CreateDefect.this, "Deleted", Toast.LENGTH_LONG).show();
                            goToLayoutMain2(v);
                        } else{
                            Toast.makeText(com.example.myapplication.CreateDefect.this, "Not Deleted", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }



}