package com.example.trabalhopraticocm.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.example.trabalhopraticocm.db.NoteDB
import com.example.trabalhopraticocm.db.NoteRepository
import com.example.trabalhopraticocm.entities.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NoteRepository

    val allNotes: LiveData<List<Note>>
    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    init {
        val notesDao = NoteDB.getDatabase(application, viewModelScope).noteDao()
        repository = NoteRepository(notesDao)
        allNotes = repository.allNotes
    }

    fun insert(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(note)
    }

    fun deleteById(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteById(id)
    }

    fun updateById(title: String, content: String, id: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateById(title, content, id)
    }
}