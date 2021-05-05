package com.example.trabalhopraticocm

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast

class Menu : AppCompatActivity() {
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        preferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
    }

    fun mapaActivity(view: View){
        val intent = Intent(this@Menu, MapsActivity::class.java)
        startActivity(intent)
    }

    fun logout(view: View){
        val preferences_edit : SharedPreferences.Editor = preferences.edit()
        preferences_edit.clear()
        preferences_edit.apply()

        val intent = Intent(this@Menu, Login::class.java)
        startActivity(intent)
        finish()
    }
}