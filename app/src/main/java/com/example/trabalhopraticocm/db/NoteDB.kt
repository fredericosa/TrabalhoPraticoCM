package com.example.trabalhopraticocm.db
import android.content.Context
import androidx.room.CoroutinesRoom
import androidx.room.Database
import androidx.room.Room
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.trabalhopraticocm.dao.NoteDao
import com.example.trabalhopraticocm.entities.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class NoteDB {
    // Annotates class to be a Room Database with a table (entity) of the Word class
    @Database(entities = arrayOf(Note::class), version = 1, exportSchema = false)
    public abstract class NoteDB : RoomDatabase() {

        abstract fun noteDao(): NoteDao

        private class WordDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCE?.let { database ->
                    scope.launch {
                        var noteDao = database.noteDao()

                        noteDao.deleteAll()

                        var note = Note(1, "Título Nota 1", "Subtitulo Nota 1", "Conteúdo Nota 1")
                        noteDao.insert(note)
                        note = Note(2, "Título Nota 2", "Subtitulo Nota 2", "Conteúdo Nota 2")
                        noteDao.insert(note)
                    }
                }
            }
        }

        companion object {
            // Singleton prevents multiple instances of database opening at the
            // same time.
            @Volatile
            private var INSTANCE: NoteDB? = null

            fun getDatabase(context: Context, scope: CoroutineScope): NoteDB {
                val tempInstance = INSTANCE
                if (tempInstance != null) {
                    return tempInstance
                }
                synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        NoteDB::class.java,
                        "note_database"
                    )
                        //estratégia de destruição
                        //.fallbackToDestrutiveMigration()
                        .addCallback(WordDatabaseCallback(scope))
                        .build()
                        INSTANCE = instance
                    return instance
                }
            }
        }
    }
}