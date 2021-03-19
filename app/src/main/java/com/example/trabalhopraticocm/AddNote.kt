package com.example.trabalhopraticocm

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText

class AddNote : AppCompatActivity() {
    private lateinit var editTitleView: EditText
    private lateinit var editContentView: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        editTitleView = findViewById(R.id.edit_title)
        editContentView = findViewById(R.id.edit_content)

        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener{
            val replyIntent = Intent()
            if (TextUtils.isEmpty(editTitleView.text) && TextUtils.isEmpty(editContentView.text)){
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val id = editTitleView.id
                replyIntent.putExtra(EXTRA_REPLY_ID, id)
                val title = editTitleView.text.toString()
                replyIntent.putExtra(EXTRA_REPLY_TITLE, title)
                val content = editContentView.text.toString()
                replyIntent.putExtra(EXTRA_REPLY_CONTENT, content)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }

        }
        companion object {
        const val EXTRA_REPLY_ID = "id"
        const val EXTRA_REPLY_TITLE = "title"
        const val EXTRA_REPLY_CONTENT = "content"
    }
}