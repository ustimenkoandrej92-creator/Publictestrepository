package com.example.scanbuilder10

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity

class AddObject : ComponentActivity() {
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

            setContentView(R.layout.fio_layout) // подключение layout

           // Инициализируем Button
            val complete_data_entry: Button = findViewById(R.id.complete_data_entry)
            val start_edit: Button = findViewById(R.id.start_edit)

            // Инициализируем EditText
           val numberZakaza: EditText = findViewById(R.id.numberZakaza)
           val NameObject: EditText = findViewById(R.id.NameObject)
           val Status: EditText = findViewById(R.id.Status)


            // Проверка полей на наличие текста
            complete_data_entry.setOnClickListener {
                val textZakaza = numberZakaza.text.toString().trim()
                val textNameObject = NameObject.text.toString().trim()
                val textStatus = Status.text.toString().trim()


                if (textZakaza.isNotBlank() && textNameObject.isNotBlank() && textStatus.isNotBlank()) {

                    // Передача полей в MainActivity
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("zakaz", textZakaza)
                    intent.putExtra("name", textNameObject)
                    intent.putExtra("status", textStatus)
                    startActivity(intent)
                } else {
                    // Показываем Toast
                    Toast.makeText(this@AddObject, "Заполните все поля", Toast.LENGTH_SHORT).show()
                }
            }



        start_edit.setOnClickListener {
            val intent = Intent(this, AddDefects::class.java)
            startActivity(intent)
        }

    }

}