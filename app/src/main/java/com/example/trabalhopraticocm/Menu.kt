package com.example.trabalhopraticocm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast

class Menu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
    }

    fun mapaActivity(view: View){
        val intent = Intent(this@Menu, MapsActivity::class.java)
        startActivity(intent)
    }
}