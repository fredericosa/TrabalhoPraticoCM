package com.example.trabalhopraticocm.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.trabalhopraticocm.entities.Note

@Dao
interface NoteDao {

    @Query("SELECT * from note_table ORDER BY title ASC")
    fun getAlphabetizedNotes(): LiveData<List<Note>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: Note)

    @Query("DELETE FROM note_table")
    suspend fun deleteAll()

}