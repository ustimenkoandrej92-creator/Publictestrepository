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
    Button delete, defects, save, back;
    DatabaseHealper myDb;
    private String currentObjectId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_object);
        myDb = new DatabaseHealper(this);


        name = (EditText)findViewById(R.id.name);
        goal =(EditText)findViewById(R.id.goal);
        id =(EditText)findViewById(R.id.id);

        defects = (Button)findViewById(R.id.btn_show);
        delete = (Button)findViewById(R.id.btn_delete);
        save = (Button)findViewById(R.id.btn_save);
        back = (Button)findViewById(R.id.btn_close);

        if(getIntent().getStringExtra("ITEM_ID") != null){
            LoadItemData();
        }

        currentObjectId = getIntent().getStringExtra("ITEM_ID");
        SaveData();
        Delete();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        defects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentObjectId == null || currentObjectId.isEmpty()) {
                    Toast.makeText(CreateObgect.this, "Сначала сохраните объект!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(CreateObgect.this, SecondActivity.class);
                intent.putExtra("OBJECT_ID", currentObjectId);
                startActivity(intent);
                finish();
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


    public void Delete(){
        delete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Integer isDeleted = myDb.deleteData(id.getText().toString());

                        if(isDeleted > 0){
                            Toast.makeText(com.example.myapplication.CreateObgect.this, "Deleted", Toast.LENGTH_LONG).show();
                            finish();
                        } else{
                            Toast.makeText(com.example.myapplication.CreateObgect.this, "Not Deleted", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }

    public void SaveData() {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idText = id.getText().toString().trim();
                String nameText = name.getText().toString().trim();
                String goalText = goal.getText().toString().trim();

                if (idText.isEmpty() || nameText.isEmpty() || goalText.isEmpty()) {
                    Toast.makeText(CreateObgect.this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (currentObjectId == null || currentObjectId.isEmpty()) {

                    Cursor check = myDb.getDataById(idText);
                    if (check != null && check.getCount() > 0) {
                        check.close();
                        Toast.makeText(CreateObgect.this, "Номер договора " + idText + " уже существует!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (check != null) check.close();


                    boolean success = myDb.insertData(idText, nameText, goalText);
                    Toast.makeText(CreateObgect.this,
                            success ? "Объект сохранен" : "Ошибка сохранения",
                            Toast.LENGTH_LONG).show();

                    if (success) {
                        currentObjectId = idText;
                    }

                    return;
                }

                if (!idText.equals(currentObjectId)) {
                    Toast.makeText(CreateObgect.this,
                            "Номер договора должен быть " + currentObjectId + ", а не " + idText + "!",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                // Обновляем объект
                boolean success = myDb.updataData(currentObjectId, nameText, goalText);
                Toast.makeText(CreateObgect.this,
                        success ? "Обновлено" : "Ошибка обновления",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

}