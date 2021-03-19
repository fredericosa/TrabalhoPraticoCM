package com.example.trabalhopraticocm

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText


class EditNote : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_note)

        var editTitle: EditText = findViewById(R.id.update_title)
        var editContent: EditText = findViewById(R.id.update_content)

        var id = intent.getStringExtra(PARAM_ID)
        var titulo = intent.getStringExtra(PARAM1_TITLE)
        var content = intent.getStringExtra(PARAM2_CONTENT)

        editTitle.setText(titulo.toString())
        editContent.setText(content.toString())

        val button_remove = findViewById<Button>(R.id.button_remove)
        button_remove.setOnClickListener {
            val replyIntent = Intent()
            replyIntent.putExtra(PARAM_ID, id.toString())
            replyIntent.setAction("REMOVE")
            setResult(Activity.RESULT_OK, replyIntent)
            finish()
        }

        val button_update = findViewById<Button>(R.id.button_update)
        button_update.setOnClickListener {
            val replyIntent = Intent()
            replyIntent.putExtra(PARAM_ID, id)
            if (TextUtils.isEmpty(editTitle.text) || TextUtils.isEmpty(editContent.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val titulo = editTitle.text.toString()
                replyIntent.putExtra(PARAM1_TITLE, titulo)

                val content = editContent.text.toString()
                replyIntent.putExtra(PARAM2_CONTENT, content)

                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }
}