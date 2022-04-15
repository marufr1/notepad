package io.github.marufr1.notepad.data.local

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import io.github.marufr1.notepad.data.model.Note

@Dao
interface NoteDao {

    @Query("select * from Note")
    fun getAllNotes(): List<Note>

    @Insert(onConflict = REPLACE)
    fun insertNote(note: Note): Long

    @Update
    fun updateNote(note: Note)

    @Delete
    fun deleteNote(note: Note)

}