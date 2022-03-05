package com.example.bitirmeprojesi

import android.content.AbstractThreadedSyncAdapter
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

   private val splashScreentimeout : Long = 4500
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    Handler().postDelayed({
        startActivity(Intent(this,anaMenu::class.java))
        finish()
    },splashScreentimeout)



    }
}