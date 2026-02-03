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
    private String currentObjectId;
    Defects_Adapter defectAdapter2;

    FloatingActionButton btn_go_to_defects, btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.wew_defects);

        currentObjectId = getIntent().getStringExtra("OBJECT_ID");

        recyclerView2 = findViewById(R.id.rvListDefects);
        myDb = new DatabaseHealper(SecondActivity.this);
        id2 = new ArrayList<>();
        type = new ArrayList<>();
        place = new ArrayList<>();

        btn_go_to_defects = findViewById(R.id.btn_go_to_defects);
        btn_back = findViewById(R.id.btn_back);

        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        recyclerView2.setHasFixedSize(true);

        StoreDataArray2();
        defectAdapter2 = new Defects_Adapter(SecondActivity.this, id2, type, place);
        recyclerView2.setAdapter(defectAdapter2);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_go_to_defects.setOnClickListener(v -> {
            Intent intent = new Intent(SecondActivity.this, CreateDefect.class);
            intent.putExtra("OBJECT_ID", currentObjectId);
            startActivity(intent);
        });


        defectAdapter2.setOnDefectClickListener(new Defects_Adapter.OnDefectClickListener() {
            @Override
            public void onDefectClick(int position) {

                String defectId = id2.get(position);
                String defectType = type.get(position);

                if(defectId == null || defectId.isEmpty()){
                    Toast.makeText(SecondActivity.this, "Error Defect ID!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(SecondActivity.this, CreateDefect.class);
                intent.putExtra("ID2", defectId);
                intent.putExtra("DEFECT_TYPE", defectType);
                intent.putExtra("OBJECT_ID", currentObjectId);
                startActivity(intent);
            }
        });
    }

    private void clearLists() {
        id2.clear();
        type.clear();
        place.clear();
    }
    private void refreshList() {
        clearLists();
        StoreDataArray2();
        if (defectAdapter2 == null) {
            defectAdapter2 = new Defects_Adapter(SecondActivity.this, id2, type, place);
            recyclerView2.setAdapter(defectAdapter2);
        } else {
            defectAdapter2.notifyDataSetChanged();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();  // ✅ Обновлять каждый раз!
    }

    void StoreDataArray2(){
        if (currentObjectId == null || currentObjectId.isEmpty()) {
            Toast.makeText(this, "Нет ID объекта!", Toast.LENGTH_SHORT).show();
            return;
        }

        Cursor cursor2 = myDb.getReadableDatabase().rawQuery(
                "SELECT * FROM " + DatabaseHealper.TABLE_NAME_2 +
                        " WHERE " + DatabaseHealper.ID_OBJECT + " = ?",
                new String[]{currentObjectId}
        );

        if(cursor2 == null || cursor2.getCount() == 0){
            Toast.makeText(this, "Нет дефектов" + currentObjectId, Toast.LENGTH_LONG).show();
        }else{

            id2.clear();
            type.clear();
            place.clear();

            while (cursor2.moveToNext()){
                id2.add(cursor2.getString(0));     // ID2 (индекс 0)
                type.add(cursor2.getString(1));    // TYPE (индекс 1)
                place.add(cursor2.getString(2));   // PLACE (индекс 2)
            }
            Toast.makeText(this, "Загружено " + id2.size() + " дефектов", Toast.LENGTH_SHORT).show();
        }
        if(cursor2 != null) cursor2.close();
    }
}
