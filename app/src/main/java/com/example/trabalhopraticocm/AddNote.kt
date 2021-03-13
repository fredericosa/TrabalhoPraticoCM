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
    private lateinit var editSubtitleView: EditText
    private lateinit var editContentView: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        editTitleView = findViewById(R.id.edit_title)
        editSubtitleView = findViewById(R.id.edit_subtitle)
        editContentView = findViewById(R.id.edit_content)

        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener{
            val replyIntent = Intent()
            val replyIntent2 = Intent()
            val replyIntent3 = Intent()
            if (TextUtils.isEmpty(editTitleView.text))
            else if (TextUtils.isEmpty(editSubtitleView.text))
            else if (TextUtils.isEmpty(editContentView.text))
            {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val title = editTitleView.text.toString()
                val subtitle = editSubtitleView.text.toString()
                val content = editContentView.text.toString()
                replyIntent.putExtra(EXTRA_REPLY, title)
                replyIntent2.putExtra(EXTRA_REPLY, subtitle)
                replyIntent3.putExtra(EXTRA_REPLY, content)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }

        }
        companion object {
        const val EXTRA_REPLY = "com.example.android.notelistsql.REPLY"
    }
}