package com.example.trabalhopraticocm.db

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.trabalhopraticocm.dao.NoteDao
import com.example.trabalhopraticocm.entities.Note
import kotlinx.coroutines.flow.Flow


class NoteRepository(private val noteDao: NoteDao) {

    val allNotes: LiveData<List<Note>> = noteDao.getAlphabetizedNotes()

    suspend fun insert(note: Note) {
        noteDao.insert(note)
    }

    fun deleteById(id: Int) {
        noteDao.deleteById(id)
    }
    suspend fun updateById(title: String, content: String, id: Int){
        noteDao.updateById(title, content, id)
    }
}
