package com.example.trabalhopraticocm.db
import android.content.Context
import androidx.room.CoroutinesRoom
import androidx.room.Database
import androidx.room.Room
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.example.trabalhopraticocm.dao.NoteDao
import com.example.trabalhopraticocm.entities.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
                    //Delete all content here.
                    noteDao.deleteAll()

                    //Add sample words.
                    var note = Note(1, "Titulo", "Subtitulo", "Conteudo")
                        noteDao.insert(note)
                        note = Note(2, "Titulo 2", "Subtitulo 2", "Conteudo 2")
                        noteDao.insert(note)

                }
            }
        }
    }

    companion object {
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
                    "notes_database",
                    )
                    //.fallbackToDestructiveMigration()
                    .addCallback(WordDatabaseCallback(scope))
                    .build()

                    INSTANCE = instance
                    return instance
            }
        }
    }
}