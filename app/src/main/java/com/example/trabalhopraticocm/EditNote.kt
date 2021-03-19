package com.example.trabalhopraticocm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView


class EditNote : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_note)

        var editTitle: EditText = findViewById(R.id.edit_title)
        var editContent: EditText = findViewById(R.id.edit_content)

        var titulo = intent.getStringExtra(PARAM1_TITLE)
        var content = intent.getStringExtra(PARAM2_CONTENT)

        editTitle.setText(titulo.toString())
        editContent.setText(content.toString())
        }
    }