package com.example.scanbuilder10

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity

class ActionMenu : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.actionmenu_layoult)

        val photos: Button = findViewById(R.id.photos)
        val gotodefects: Button = findViewById(R.id.gotodefects)

        gotodefects.setOnClickListener {
            val intent = Intent(this, AddDefects::class.java)
            startActivity(intent)
        }


    }

}