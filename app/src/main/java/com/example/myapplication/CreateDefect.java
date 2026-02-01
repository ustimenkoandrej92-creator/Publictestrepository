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
    EditText type, place;
    Button save, delete2, goToPhoto;
    DatabaseHealper myDb;
    private String currentDefectId;
    private String currentObjectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_defect);
        myDb = new DatabaseHealper(this);

        type = (EditText) findViewById(R.id.type);
        place = (EditText) findViewById(R.id.place);

        save = (Button) findViewById(R.id.btn_back2);
        delete2 = (Button) findViewById(R.id.btn_delete2);
        goToPhoto = (Button) findViewById(R.id.btn_photo);

        String id2 = getIntent().getStringExtra("ID2");
        String objectId = getIntent().getStringExtra("OBJECT_ID");
        String objectIdFromIntent = getIntent().getStringExtra("OBJECT_ID");


        if (id2 != null && !id2.isEmpty()) {
            currentDefectId = id2;
            LoadItemData();
        }
        currentObjectId = objectIdFromIntent;

        SaveData();  //
        Delete();

        goToPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateDefect.this, CreatePhoto.class);
                startActivity(intent);
            }
        });
    }

    public void LoadItemData() {

        Cursor res = myDb.getDataById2(currentDefectId);

        if (res != null && res.getCount() > 0 && res.moveToFirst()) {
            type.setText(res.getString(1));    // TYPE
            place.setText(res.getString(2));   // PLACE
        }
        if (res != null) res.close();
    }



    public void SaveData() {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String typeText = type.getText().toString().trim();
                String placeText = place.getText().toString().trim();

                if (typeText.isEmpty() || placeText.isEmpty()) {
                    Toast.makeText(CreateDefect.this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean success = false;

                if (currentDefectId != null && !currentDefectId.isEmpty()) {

                    success = myDb.updataData2(currentDefectId, typeText, placeText, currentObjectId);
                    Toast.makeText(CreateDefect.this,

                            success ? "Обновлено" : "Ошибка, обновить не удалось",
                            Toast.LENGTH_LONG).show();
                } else {

                    success = myDb.insertData2(typeText, placeText, currentObjectId);
                    Toast.makeText(CreateDefect.this,
                            success ? "Сохранен новый дефект" : "Ошибка сохранения",
                            Toast.LENGTH_LONG).show();
                }

                if (success) finish();
            }
        });
    }

    public void Delete() {
        delete2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentDefectId != null && !currentDefectId.isEmpty()) {
                    int deleted = myDb.deleteData2(currentDefectId);

                    if (deleted > 0) {
                        Toast.makeText(CreateDefect.this, "Deleted", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(CreateDefect.this, "Not Deleted", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(CreateDefect.this, "ID не найден", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}