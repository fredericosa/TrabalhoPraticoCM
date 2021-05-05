package com.example.trabalhopraticocm

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast

class Menu : AppCompatActivity() {
    private lateinit var shared_preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
    }

    fun mapaActivity(view: View){
        val intent = Intent(this@Menu, MapsActivity::class.java)
        startActivity(intent)
    }

    fun logout(view: View){
        val shared_preferences_edit : SharedPreferences.Editor = shared_preferences.edit()
        shared_preferences_edit.clear()
        shared_preferences_edit.apply()

        val intent = Intent(this@Menu, Login::class.java)
        startActivity(intent)
        finish()
    }
}