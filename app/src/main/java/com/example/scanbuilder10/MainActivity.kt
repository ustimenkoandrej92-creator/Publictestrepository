package com.example.scanbuilder10

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.ComponentActivity


class MainActivity : ComponentActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout) // подключение layout

        val a=30

        val addObject: Button = findViewById(R.id.add_object)
        val addDefects: Button = findViewById(R.id.add_defects)

            // принемаем данные полей с AddObject
        val zakaz = intent.getStringExtra("zakaz")
        val name = intent.getStringExtra("name")
        val status = intent.getStringExtra("status")

        if (!zakaz.isNullOrBlank() && !name.isNullOrBlank() && !status.isNullOrBlank()) {
        val container: LinearLayout = findViewById(R.id.buttonContainer)
           // Данные, которые мы вставляем
          // Создаём кнопку
        val newButton = Button(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = 10.dpToPx() // если хочешь отступ
            }

            text = "$zakaz\n $name\n $status"
            textSize = 16f
            setPadding(20, 20, 20, 20)
        }
            // Добавляем кнопку в контейнер
            container.addView(newButton)

            newButton.setOnClickListener {
                val intent = Intent(this, ActionMenu::class.java)
                startActivity(intent)
            }
        } else {
            // Кнопка пустая
        }

        addObject.setOnClickListener {
            val intent = Intent(this, AddObject::class.java)
            startActivity(intent)
        }

        addDefects.setOnClickListener {
            val intent = Intent(this, AddDefects::class.java)
            startActivity(intent)
        }

    }

}
          // Функция расширения для Int: преобразует dp в пиксели с учётом плотности экрана
       fun Int.dpToPx(): Int {
           return (this * Resources.getSystem().displayMetrics.density).toInt()
       }
