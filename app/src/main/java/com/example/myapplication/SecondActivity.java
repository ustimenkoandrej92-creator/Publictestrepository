package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {

    private RecyclerView recyclerView2;
    DatabaseHealper myDb;
    ArrayList<String> id2, type, place;
    Defects_Adapter defectAdapter2;

    FloatingActionButton btn_go_to_defects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.wew_defects);

        recyclerView2 = findViewById(R.id.rvListDefects);
        myDb = new DatabaseHealper(SecondActivity.this);
        id2 = new ArrayList<>();
        type = new ArrayList<>();
        place = new ArrayList<>();

        btn_go_to_defects = findViewById(R.id.btn_go_to_defects);

        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this);
        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView2.setHasFixedSize(true);

        StoreDataArray2();
        defectAdapter2 = new Defects_Adapter(SecondActivity.this, id2, type, place);
        recyclerView2.setAdapter(defectAdapter2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(SecondActivity.this));

        btn_go_to_defects.setOnClickListener(v -> {
            Intent intent = new Intent(SecondActivity.this, CreateDefect.class);
            startActivity(intent);
        });
    }

    void StoreDataArray2(){
        Cursor cursor2 = myDb.readAllData2();  // ← readAllData2() для дефектов
        if(cursor2 == null || cursor2.getCount() == 0){
            Toast.makeText(this, "No defects data", Toast.LENGTH_LONG).show();
        }else{
            while (cursor2.moveToNext()){
                id2.add(cursor2.getString(0));
                type.add(cursor2.getString(1));  // ← TYPE_OF_DEFECT
                place.add(cursor2.getString(2)); // ← PLACE_OF_DEFECT
            }
        }
        if(cursor2 != null) cursor2.close();  // ← Закрыть курсор!
    }
}
