package com.example.trabalhopraticocm

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Adapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trabalhopraticocm.adapters.NoteListAdapter
import com.example.trabalhopraticocm.entities.Note
import com.example.trabalhopraticocm.viewModel.NoteViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

    var PARAM1_TITLE = "title"
    var PARAM2_CONTENT = "content"

    class MainActivity : AppCompatActivity(), CellClickListener {
        private lateinit var noteViewModel: NoteViewModel
        private val newWordActivityRequestCode = 1



    override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
       setContentView(R.layout.activity_main)

       //recycler view
       val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
       val adapter = NoteListAdapter(this, this)
       recyclerView.adapter = adapter
       recyclerView.layoutManager = LinearLayoutManager(this)

       //view model
       noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
       noteViewModel.allNotes.observe(this, { notes ->
           notes?.let { adapter.setNotes(it) }
       })

       //Fab
       val fab = findViewById<FloatingActionButton>(R.id.fab)
       fab.setOnClickListener {
           val intent = Intent(this@MainActivity, AddNote::class.java)
           startActivityForResult(intent, newWordActivityRequestCode)
       }

   }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newWordActivityRequestCode && resultCode == Activity.RESULT_OK) {
                var titulo = data?.getStringExtra(AddNote.EXTRA_REPLY_TITLE).toString()
                var content = data?.getStringExtra(AddNote.EXTRA_REPLY_CONTENT).toString()
                var note = Note(title = titulo, content = content)
                noteViewModel.insert(note)
                Toast.makeText(this, "Nota Guardada", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(
                    applicationContext,
                    "Nota Vazia: não inserida",
                    Toast.LENGTH_LONG).show()
        }
    }

        override fun onCellClickListener(data: Note) {
            val titulo = data.title
            val conteudo = data.content
            val intent = Intent(this, EditNote::class.java).apply {
                putExtra(PARAM1_TITLE, titulo)
                putExtra(PARAM2_CONTENT, conteudo)
            }
            startActivityForResult(intent, newWordActivityRequestCode)
        }
    }