package com.example.trabalhopraticocm.db

import androidx.annotation.WorkerThread
import com.example.trabalhopraticocm.dao.NoteDao
import com.example.trabalhopraticocm.entities.Note
import kotlinx.coroutines.flow.Flow


class NoteRepository(private val noteDao: NoteDao) {

    val allNotes: Flow<List<Note>> = noteDao.getAlphabetizedNotes()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(note: Note) {
        noteDao.insert(note)
    }
}
