package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    DatabaseHealper myDb;
    ArrayList<String> id, name, email;
    Item_Adapter itemAdapter;

    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rvList);
        myDb = new DatabaseHealper(MainActivity.this);
        id = new ArrayList<>();
        name = new ArrayList<>();
        email = new ArrayList<>();

        fab = findViewById(R.id.btn_toCreateObgect);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);


        StoreDataArray();
        itemAdapter = new Item_Adapter(MainActivity.this, id, name);
        recyclerView.setAdapter(itemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        itemAdapter.setOnItemClickListener(new Item_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                String itemId = id.get(position);
                String itemName = name.get(position);

                if(itemId == null || itemId.isEmpty()){
                    Toast.makeText(MainActivity.this, "Error ID!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(MainActivity.this, CreateObgect.class);
                intent.putExtra("ITEM_ID", itemId);
                intent.putExtra("ITEM_NAME", itemName);
                startActivity(intent);
            }
        });

        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CreateObgect.class);
            startActivity(intent);
        });

    }
    private void clearLists() {
        id.clear();
        name.clear();
        email.clear();
    }
    private void refreshList() {
        clearLists();        // Очистить старые данные
        StoreDataArray();    // Загрузить новые из БД
        itemAdapter.notifyDataSetChanged();  // ❌ СКАЗАТЬ АДАПТЕРУ!
    }
    @Override
    protected void onResume() {
        super.onResume();
        refreshList();  // ✅ Обновлять каждый раз!
    }
    public void goToLayout2(View view) {
        Intent intent = new Intent(MainActivity.this, CreateObgect.class);
        startActivity(intent);

    }

    void StoreDataArray(){
        Cursor cursor = myDb.readAllData();
        if(cursor == null || cursor.getCount() == 0){
            Toast.makeText(this, "No data", Toast.LENGTH_LONG).show();
        }else{
            while (cursor.moveToNext()){
                id.add(cursor.getString(0));
                name.add(cursor.getString(1));
                email.add(cursor.getString(2));
            }
        }
    }


}