package com.example.trabalhopraticocm


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trabalhopraticocm.adapters.NoteListAdapter
import com.example.trabalhopraticocm.entities.Note
import com.example.trabalhopraticocm.viewModel.NoteViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {
   private lateinit var noteViewModel: NoteViewModel
   private val newWordActivityRequestCode = 1

   override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
       setContentView(R.layout.activity_main)

       //recycler view
       val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
       val adapter = NoteListAdapter(this)
       recyclerView.adapter = adapter
       recyclerView.layoutManager = LinearLayoutManager(this)

       //view model
       noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
       noteViewModel.allNotes.observe(this, {notes ->
           notes?.let{adapter.setNotes(it)}
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
            data?.getStringExtra(AddNote.EXTRA_REPLY_CONTENT)?.let {
                val note = Note(title = "Título: " + it, content ="Observação: " + it)
            }
            data?.getStringExtra(AddNote.EXTRA_REPLY_TITLE)?.let {
                val note = Note(title = "Título: " + it, content ="Observação: " + it)
                noteViewModel.insert(note)
            }
        } else {
            Toast.makeText(
                applicationContext,
                "Nota Vazia: não inserida",
                Toast.LENGTH_LONG).show()
        }
    }
}