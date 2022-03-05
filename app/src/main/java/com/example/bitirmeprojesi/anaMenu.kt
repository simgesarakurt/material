package com.example.bitirmeprojesi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button


class anaMenu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ana_menu)


   val button1= findViewById<Button>(R.id.btn1)
        button1.setOnClickListener {
            val intent = Intent(this, nesnetanima::class.java)
            startActivity(intent)
        }



        val button2 = findViewById<Button>(R.id.btn2)
        button2.setOnClickListener {
            val intent = Intent(this, puzzle::class.java)
            startActivity(intent)
        }


    }
}
